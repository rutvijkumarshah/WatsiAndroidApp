/*
 *  Copyright (c) 2014, Facebook, Inc. All rights reserved.
 *
 *  You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
 *  copy, modify, and distribute this software in source code or binary form for use
 *  in connection with the web services and APIs provided by Facebook.
 *
 *  As with any software that integrates with the Facebook platform, your use of
 *  this software is subject to the Facebook Developer Principles and Policies
 *  [http://developers.facebook.com/policy/]. This copyright notice shall be
 *  included in all copies or substantial portions of the software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 *  FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 *  COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 *  IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package codepath.watsiapp.activities;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import codepath.watsiapp.models.Patient;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseQuery;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;

public class DonarProfileActivity extends FragmentActivity {
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
				donorId = user.getString("donorId");
				email = user.getString("email");

				donarFullName.setText(fullName);

				if (ParseFacebookUtils.isLinked(user)) {
					// /find email of parseUser from FB
					// "https://graph.facebook.com/%s/picture",
					// assuming will get email in email field
					setEmail();

				} else if (ParseTwitterUtils.isLinked(user)) {

				} else {
					user.getUsername();
				}
				
				setDonationsFragment(donorId);
				
			}

		}
	}

	private void showDetailsForNonDonor() {
		totalDonationsAmount.setText("NOT A DONOR");
	}
	
	private void showDetailsForDonor(Donor donor) {
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
			totalDonationsAmount.setText(String.valueOf(totalDonations));
			totalTreatmentsFunded.setText(String.valueOf(treatments.size()));
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
