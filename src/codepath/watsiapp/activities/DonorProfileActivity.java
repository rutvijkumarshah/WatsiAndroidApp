package codepath.watsiapp.activities;

import static codepath.watsiapp.utils.Util.applyPrimaryFont;
import retrofit.Callback;
import retrofit.RetrofitError;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import codepath.watsiapp.R;
import codepath.watsiapp.api.Services;
import codepath.watsiapp.fragmentsv2.PatientFeedFragment;
import codepath.watsiapp.interfaces.OnDonationStatsCalculatedListener;
import codepath.watsiapp.modelsv2.*;
import codepath.watsiapp.utils.Util;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class DonorProfileActivity extends BaseFragmentActivity implements
		OnDonationStatsCalculatedListener {

	private TextView donarFullName;
	private TextView memberSinceDate;
	private TextView totalDonationsAmount;
	private TextView totalTreatmentsFunded;
	private ImageView profilePicture;
	// private String donorId;
	private TextView donatedForText;
	private TextView treatmetnsText;
	private Donor donor;
	
	private static final String TAG_IGNORE_EXP="IGNORE_EXP";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_donor_profile);
		profilePicture = (ImageView) findViewById(R.id.profilePic);
		donarFullName = (TextView) findViewById(R.id.fullname);
		memberSinceDate = (TextView) findViewById(R.id.tv_membersince_dt);
		totalTreatmentsFunded = (TextView) findViewById(R.id.tv_noof_treatments);
		totalDonationsAmount = (TextView) findViewById(R.id.total_donation);
		donatedForText = (TextView) findViewById(R.id.medicalNeed);
		treatmetnsText = (TextView) findViewById(R.id.location);

		applyPrimaryFont(getApplicationContext(), donarFullName);
		applyPrimaryFont(getApplicationContext(), memberSinceDate);
		applyPrimaryFont(getApplicationContext(), totalTreatmentsFunded);
		applyPrimaryFont(getApplicationContext(), totalDonationsAmount);

		applyPrimaryFont(getApplicationContext(),
				(TextView) findViewById(R.id.medicalNeed));// Donated for text
		applyPrimaryFont(getApplicationContext(),
				(TextView) findViewById(R.id.tv_membersince));// Member Since
																// Text
		applyPrimaryFont(getApplicationContext(),
				(TextView) findViewById(R.id.location));// Treatments text

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
			String email = user.getString("email");
			getActionBar().setTitle("Profile");

			
			boolean isFacebookLinkedUser=ParseFacebookUtils.isLinked(user);
			email = user.getString("email");

			if (isFacebookLinkedUser) {
				setEmail();
			} else {
				showDonorInformaiton(fullName,email,false);
			}

		}
	}

	private void showDonorInformaiton(String fullName,String email,final boolean isFacebookLinkedUser) {
		
		donarFullName.setText(fullName);
		String whereClause="{\"email\" : \"%s\" }";
		
		Services.getInstance().getDonorService().findById(String.format(whereClause, email), new Callback<DonarsResponse>() {
			
			@Override
			public void failure(RetrofitError arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void success(DonarsResponse response, retrofit.client.Response arg1) {
				
				donor=response.results.get(0);
				if (donor != null) {
					showDetailsForDonor(isFacebookLinkedUser);
				} else {
					showDetailsForNonDonor();
				}
			}
		});
		
		
	}
	private void setDefaultUserPic() {
		String uri = "@drawable/profile_img";
		int imageResource = getResources().getIdentifier(uri, null,
				getPackageName());
		Drawable res = getResources().getDrawable(imageResource);
		profilePicture.setImageDrawable(res);
	}
	private void showDetailsForNonDonor() {
		setDefaultUserPic();
		profilePicture.setVisibility(View.VISIBLE);
		memberSinceDate.setText(Util.getFormatedDate(ParseUser.getCurrentUser()
				.getCreatedAt()));
		totalDonationsAmount.setText("");
		donatedForText.setText("");
		treatmetnsText.setText("");
		totalTreatmentsFunded.setText("Make a difference");
		setNewsFragment();
	}

	private void showDetailsForDonor(boolean isFBuser) {

		if(!isFBuser) {
			setDefaultUserPic();
		}
		profilePicture.setVisibility(View.VISIBLE);
		memberSinceDate.setText(Util.getFormatedDate(donor.getMemberSince()));
		setDonationsFragment();

	}

	public void setEmail() {
		Request.newMeRequest(ParseFacebookUtils.getSession(),
				new Request.GraphUserCallback() {

					@Override
					public void onCompleted(GraphUser user, Response response) {
						ImageLoader imgImageLoader = ImageLoader.getInstance();
						String profileImgUrl=
								"http://graph.facebook.com/" + user.getId()
								+ "/picture?type=large";
						imgImageLoader.displayImage(profileImgUrl, profilePicture);
						String email = user.asMap().get("email").toString();
						showDonorInformaiton(user.getName(),email,true);
					}

				}).executeAsync();

	}

	private void setDonationsFragment() {

		// Begin the transaction
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		// Replace the container with the new fragment
		ft.replace(R.id.donations,
				codepath.watsiapp.fragmentsv2.DonationListFragment.newInstance(donor.getObjectId()));
		ft.commit();
	}

	private void setNewsFragment() {

		// Begin the transaction
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		// Replace the container with the new fragment
		ft.replace(R.id.donations, PatientFeedFragment.newInstance());
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
