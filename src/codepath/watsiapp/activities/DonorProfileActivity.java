package codepath.watsiapp.activities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;
import android.widget.TextView;
import codepath.watsiapp.ParseHelper;
import codepath.watsiapp.R;
import codepath.watsiapp.fragments.DonationListFragment;
import codepath.watsiapp.models.Donation;
import codepath.watsiapp.models.Donor;
import codepath.watsiapp.utils.Util;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseQuery;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;

public class DonorProfileActivity extends FragmentActivity {
	private TextView donarFullName;
	private TextView memberSinceDate;
	private TextView totalDonationsAmount;
	private TextView totalTreatmentsFunded;
	private ImageView profilePicture;
	private String donorId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_donor_profile);
		profilePicture = (ImageView) findViewById(R.id.profilePic);
		donarFullName = (TextView) findViewById(R.id.fullname);
		memberSinceDate = (TextView) findViewById(R.id.tv_membersince_dt);
		totalTreatmentsFunded = (TextView) findViewById(R.id.tv_noof_treatments);
		totalDonationsAmount = (TextView) findViewById(R.id.total_donation);

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
			if (fullName != null) {
				donarFullName.setText(fullName);
				getActionBar().setTitle("Donor Profile");
				//
				donorId = user.getString("donorId");
				email = user.getString("email");

				donarFullName.setText(fullName);

				if (ParseFacebookUtils.isLinked(user)) {
					// /find email of parseUser from FB
					// "https://graph.facebook.com/%s/picture",
					// assuming will get email in email field
					setEmail();

				} else {
					user.getUsername();
					memberSinceDate.setText(Util.getFormatedDate(user.getCreatedAt()));
					
				}
				
				if(donorId!=null) {
					setDonationsFragment(donorId);
				}
				
				
			}

		}
	}

	private void showDetailsForNonDonor() {
		memberSinceDate.setText(Util.getFormatedDate(ParseUser.getCurrentUser().getCreatedAt()));
		totalDonationsAmount.setText("NOT A DONOR");
	}
	
	private void showDetailsForDonor(Donor donor) {
		memberSinceDate.setText(Util.getFormatedDate(donor.getMemberSince()));
		donorId=donor.getObjectId();
		ParseHelper parseHelper = new ParseHelper(getApplicationContext());
		try {
			double totalDonations=0.00;
			List<Donation> donationList = parseHelper.getDonationsByDonor(donor).find();
			final Set<String> treatments=new HashSet<String>();
			for (Donation donation : donationList) {
				totalDonations+=donation.getDonationAmount();
				treatments.add(donation.getPatient().getObjectId());
			}
			totalDonationsAmount.setText("$ "+String.valueOf(totalDonations));
			totalTreatmentsFunded.setText(String.valueOf(treatments.size()));
			setDonationsFragment(donorId);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
}
