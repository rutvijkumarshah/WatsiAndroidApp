package codepath.watsiapp.activities;

import static codepath.watsiapp.utils.Util.applyPrimaryFont;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import codepath.watsiapp.ParseHelper;
import codepath.watsiapp.R;
import codepath.watsiapp.fragments.DonationListFragment;
import codepath.watsiapp.fragments.PatientFeedFragment;
import codepath.watsiapp.models.Donor;
import codepath.watsiapp.models.OnDonationStatsCalculatedListener;
import codepath.watsiapp.utils.Util;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseQuery;
import com.parse.ParseUser;
public class DonorProfileActivity extends FragmentActivity implements OnDonationStatsCalculatedListener {
	
	private TextView donarFullName;
	private TextView memberSinceDate;
	private TextView totalDonationsAmount;
	private TextView totalTreatmentsFunded;
	private ImageView profilePicture;
	private String donorId;
	private TextView donatedForText;
	private TextView treatmetnsText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_donor_profile);
		profilePicture = (ImageView) findViewById(R.id.profilePic);
		donarFullName = (TextView) findViewById(R.id.fullname);
		memberSinceDate = (TextView) findViewById(R.id.tv_membersince_dt);
		totalTreatmentsFunded = (TextView) findViewById(R.id.tv_noof_treatments);
		totalDonationsAmount = (TextView) findViewById(R.id.total_donation);
		donatedForText=(TextView)findViewById(R.id.medicalNeed);
		treatmetnsText=(TextView)findViewById(R.id.location);

		applyPrimaryFont(getApplicationContext(), donarFullName);
		applyPrimaryFont(getApplicationContext(), memberSinceDate);
		applyPrimaryFont(getApplicationContext(), totalTreatmentsFunded);
		applyPrimaryFont(getApplicationContext(), totalDonationsAmount);
		
		applyPrimaryFont(getApplicationContext(), (TextView)findViewById(R.id.medicalNeed));//Donated for text
		applyPrimaryFont(getApplicationContext(), (TextView)findViewById(R.id.tv_membersince));//Member Since Text
		applyPrimaryFont(getApplicationContext(), (TextView)findViewById(R.id.location));//Treatments text
		
		profilePicture.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onStart() {
		super.onStart();
		// Set up the profile page based on the current user.
		ParseUser user = ParseUser.getCurrentUser();

		showProfile(user);
	}

	/**
	 * Shows the profile of the given user.
	 * 
	 * @param user
	 */
	private void showProfile(ParseUser user) {
		if (user != null) {
			String fullName = user.getString("name");
			String email = null;
			//if (fullName != null) {
				getActionBar().setTitle("Profile");
				//
				donorId = user.getString("donorId");
				email = user.getString("email");

				if (ParseFacebookUtils.isLinked(user)) {
					// /find email of parseUser from FB
					// "https://graph.facebook.com/%s/picture",
					// assuming will get email in email field
					setEmail();

				} else {
					donarFullName.setText(fullName);
					memberSinceDate.setText(Util.getFormatedDate(user.getCreatedAt()));
					showDetailsForNonDonor();
				}
				
				if(donorId!=null) {
					setDonationsFragment(donorId);
				}
				
				
			//}

		}
	}

	private void showDetailsForNonDonor() {
		String uri = "@drawable/profile_img";
		int imageResource = getResources().getIdentifier(uri, null, getPackageName());		
		Drawable res = getResources().getDrawable(imageResource);
		profilePicture.setImageDrawable(res);
		
		profilePicture.setVisibility(View.VISIBLE);
		memberSinceDate.setText(Util.getFormatedDate(ParseUser.getCurrentUser().getCreatedAt()));
		totalDonationsAmount.setText("");
		donatedForText.setText("");
		treatmetnsText.setText("");
		totalTreatmentsFunded.setText("Make a difference");
		setNewsFragment();
	}
	
	private void showDetailsForDonor(Donor donor) {
		
		profilePicture.setVisibility(View.VISIBLE);
		memberSinceDate.setText(Util.getFormatedDate(donor.getMemberSince()));
		donorId=donor.getObjectId();
		setDonationsFragment(donorId);
		
	}
	
	public void setEmail() {
		Request.newMeRequest(ParseFacebookUtils.getSession(),
				new Request.GraphUserCallback() {

					@Override
					public void onCompleted(GraphUser user, Response response) {
						ImageLoader imgImageLoader = ImageLoader.getInstance();
						imgImageLoader.displayImage(
								"http://graph.facebook.com/" + user.getId()
										+ "/picture?type=large", profilePicture);
						String email=user.asMap().get("email").toString();
						//store facebook image url
						//store donorId
						
						donarFullName.setText(user.getName());
						ParseHelper parseHelper = new ParseHelper(getApplicationContext());
						ParseQuery<Donor> query = parseHelper.findDonorByEmail(email);
						try {
							Donor donor = query.getFirst();
							if(donor == null) {
								showDetailsForNonDonor();
							}else {
								showDetailsForDonor(donor);
							}
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}

				}).executeAsync();

	}

	private void setDonationsFragment(String donorId) {

		// Begin the transaction
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		// Replace the container with the new fragment
		ft.replace(R.id.donations,
				DonationListFragment.newInstance(donorId));
		ft.commit();
	}

	private void setNewsFragment() {

		// Begin the transaction
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		// Replace the container with the new fragment
		ft.replace(R.id.donations,
				PatientFeedFragment.newInstance(null));
		ft.commit();
	}

	@Override
	public void totalDonationsCalculated(double totalDonationAmount) {
		totalDonationsAmount.setText(Util.formatAmount(totalDonationAmount));
	}

	@Override
	public void totalTreatmentsCalculated(Integer totalTreatments) {
		totalTreatmentsFunded.setText(String.valueOf(totalTreatments));
		
	}
}
