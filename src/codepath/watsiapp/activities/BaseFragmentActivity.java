/***

The MIT License (MIT)
Copyright � 2014 Rutvijkumar Shah
 
Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the �Software�), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
 
The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.
 
THE SOFTWARE IS PROVIDED �AS IS�, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 ***/

package codepath.watsiapp.activities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import codepath.watsiapp.R;
import codepath.watsiapp.api.Services;
import codepath.watsiapp.interfaces.DonationInfoStorage;
import codepath.watsiapp.modelsv2.PaymentConfirmation;
import codepath.watsiapp.utils.PrefsHelper;
import codepath.watsiapp.utils.Util;

import com.parse.ParseUser;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.ca.mdo.CAMobileDevOps;

public class BaseFragmentActivity extends FragmentActivity implements
		DonationInfoStorage {

	protected MenuItem myprofile;
	private MenuItem logout;
	private PrefsHelper prefs;

	private static PayPalConfiguration payPalConfig;
	private static final String TAG_PAYPAL = "PAYPAL_PROCESSING";

	private void setupPayPalConfig() {
		try {

			payPalConfig = new PayPalConfiguration().environment(
					PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(
					getString(R.string.paypal_client_id));
		} catch (Exception e) {
			Log.e(TAG_PAYPAL, "Exception while paypal init configuration" + e,
					e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.common_menus, menu);

		myprofile = menu.findItem(R.id.action_profileView);
		logout = menu.findItem(R.id.action_logout);
		checkLogOutVisible();
		return true;
	}

	protected void checkLogOutVisible() {
		if (logout != null) {
			if (ParseUser.getCurrentUser() == null) {
				logout.setVisible(false);
			} else {
				logout.setVisible(true);
			}
		}

	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		CAMobileDevOps.initialize(this);
		prefs = new PrefsHelper(this);
		setupPayPalConfig();
		Intent intent = new Intent(this, PayPalService.class);
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfig);
		startService(intent);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final int menuItemId = item.getItemId();
		if (menuItemId == R.id.action_profileView) {
			Util.showMyProfileActivity(this);
		}
		if (menuItemId == R.id.action_logout) {
			logout();
			logout.setVisible(false);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkLogOutVisible();
		try {
			CAMobileDevOps.activityResume(this);
		} catch (Exception e) {
			Log.e("CA_MAA_SDK", e.getLocalizedMessage(), e);
		}

	}

	@Override
	public void onPause() {
		super.onPause();
		try {
			CAMobileDevOps.activityPause(this);
		} catch (Exception e) {
			Log.e("CA_MAA_SDK", e.getLocalizedMessage(), e);
		}
	}

	private void logout() {
		prefs.clearAll();
		ParseUser.logOut();

		// FLAG_ACTIVITY_CLEAR_TASK only works on API 11, so if the user
		// logs out on older devices, we'll just exit.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			Intent intent = new Intent(BaseFragmentActivity.this,
					WatsiMainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		} else {
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			com.paypal.android.sdk.payments.PaymentConfirmation confirm = data
					.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
			if (confirm != null) {
				try {
					showThankYouNote();
					PaymentConfirmation confirmation = new PaymentConfirmation();

					confirmation.setDonorName(getUserFullName());
					confirmation.setDonorEmail(getUserEmail());
					confirmation.setPatientObjectId(getPatientId());
					confirmation.setAmount(getDonationAmount());
					confirmation.setPayPalConfirmation(confirm.toJSONObject()
							.toString(4));
					confirmation.setAnonymous(isAnonymousDonation());

					Services.getInstance()
							.getPaymentConfirmatons()
							.postConfirmation(confirmation,
									new Callback<PaymentConfirmation>() {

										@Override
										public void failure(RetrofitError arg0) {
											// TODO Auto-generated method stub

										}

										@Override
										public void success(
												PaymentConfirmation confirmation,
												Response arg1) {
											prefs.clear();

										}
									});

				} catch (JSONException e) {
					Log.e(TAG_PAYPAL,
							"an extremely unlikely failure occurred: ", e);
				}
			}
		} else if (resultCode == Activity.RESULT_CANCELED) {
			Log.i(TAG_PAYPAL, "The user canceled.");
		} else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
			Log.i(TAG_PAYPAL,
					"An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
		}
	}

	@Override
	public float getDonationAmount() {
		return prefs.getDonationAmount();
	}

	@Override
	public String getPatientId() {
		return prefs.getPatientId();
	}

	@Override
	public String getUserFullName() {
		return prefs.getUserFullName();
	}

	@Override
	public String getUserEmail() {
		return prefs.getUserEmail();
	}

	@Override
	public void setUserFullName(String fullName) {
		prefs.setUserFullName(fullName);

	}

	@Override
	public void setUserEmailAddress(String email) {
		prefs.setUserEmailAddress(email);
	}

	@Override
	public void setDonationAmount(float value) {
		prefs.setDonationAmount(value);

	}

	@Override
	public void setPatientId(String value) {
		prefs.setPatientId(value);

	}

	@Override
	public boolean isAnonymousDonation() {
		return prefs.isAnonymousDonation();
	}

	@Override
	public void setAnonymousDonation(boolean isAnonymousDonation) {
		prefs.setAnonymousDonation(isAnonymousDonation);
	}

	private void showThankYouNote() {
		LayoutInflater inflater = this.getLayoutInflater();
		View view = inflater.inflate(R.layout.toast_thanks_for_donation,
				(ViewGroup) this.findViewById(R.id.thanks_note));
		Toast toast = new Toast(this);
		toast.setView(view);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.show();

	}

	@Override
	public void set(float donationAmount, String patientId,
			boolean isAnonymousDonation, String fullName, String email) {
		prefs.set(donationAmount, patientId, isAnonymousDonation, fullName,
				email);

	}

	protected void generateFacebookKey() {
		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					"codepath.watsiapp", PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d(">>>>>                     KeyHash:",
						Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

}
