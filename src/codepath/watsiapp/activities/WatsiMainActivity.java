package codepath.watsiapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import codepath.watsiapp.R;
import codepath.watsiapp.fragments.PatientFeedFragment;
import codepath.watsiapp.fragments.PatientListFragment;
import codepath.watsiapp.fragments.PaymentAmountFragment;
import codepath.watsiapp.utils.Util;

import com.astuetz.PagerSlidingTabStrip;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.parse.ParseAnalytics;
import com.paypal.android.sdk.payments.PayPalService;

public class WatsiMainActivity extends BaseFragmentActivity {
	private ViewPager vpPager;
	private PatientsPagerAdapter adapterViewPager;
	private PagerSlidingTabStrip tabs;

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ParseAnalytics.trackAppOpened(getIntent());
		vpPager = (ViewPager) findViewById(R.id.viewPager);
		adapterViewPager = new PatientsPagerAdapter(getSupportFragmentManager());
		vpPager.setAdapter(adapterViewPager);
		setupSlidingTabs(vpPager);
		DoTest();
	}

	private void DoTest() {
		String sample= "{\n" +
	              "    \"age\": 1,\n" +
	              "    \"country\": \"Kenya\",\n" +
	              "    \"firstName\": \"Michael\",\n" +
	              "    \"isFullyFunded\": true,\n" +
	              "    \"medicalNeed\": \"Michael needs spina bifida closure surgery.\",\n" +
	              "    \"medicalPartner\": {\n" +
	              "                \"name\": \"African Mission Healthcare Foundation\",\n" +
	              "                \"websiteUrl\": \"http://www.amhf.us/\",\n" +
	              "                \"createdAt\": \"2014-07-10T01:18:32.807Z\",\n" +
	              "                \"updatedAt\": \"2014-07-10T01:19:01.898Z\",\n" +
	              "                \"objectId\": \"bpQh0xZkuS\",\n" +
	              "                \"__type\": \"Object\",\n" +
	              "                \"className\": \"MedicalPartner\"\n" +
	              "    },\n" +
	              "    \"photoUrl\": \"https://d3w52z135jkm97.cloudfront.net/uploads/profile/image/2224/profile_Michael_Ngari_Muchiri_pre_op_picture1_AMHF_BKKH_.jpg\",\n" +
	              "    \"profileUrl\": \"https://watsi.org/profile/d2b79fee498b-michael\",\n" +
	              "    \"receivedDonation\": 980,\n" +
	              "    \"story\": \"The days treatment!\",\n" +
	              "    \"targetDonation\": 980,\n" +
	              "    \"createdAt\": \"2014-08-04T05:08:49.475Z\",\n" +
	              "    \"updatedAt\": \"2014-08-04T20:07:12.902Z\",\n" +
	              "    \"objectId\": \"Gibye7bUBM\"\n" +
	              "}";
		
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        
		codepath.watsiapp.modelsv2.Patient patient = gson.fromJson(sample, codepath.watsiapp.modelsv2.Patient.class);
        patient.persist();
	}
	public void showDonateDialog(String dialogHeader, String patientId,
			String donateTo) {
		FragmentManager fm = getSupportFragmentManager();
		PaymentAmountFragment paymenttDialog = PaymentAmountFragment
				.newInstance(dialogHeader, patientId, donateTo);
		paymenttDialog.show(fm, "fragment_payment_amount");

	}

	private void setupSlidingTabs(ViewPager vpPager2) {
		tabs = (PagerSlidingTabStrip) findViewById(R.id.slidingTabStrip);
		tabs.setShouldExpand(true);

		tabs.setAllCaps(false);
		float dimension = getResources().getDimensionPixelSize(
				R.dimen.fontsize_small);
		tabs.setTextSize((int) dimension);
		Util.applyPrimaryFont(getApplicationContext(), tabs);
		tabs.setViewPager(vpPager);

		tabs.setIndicatorColor(getResources().getColor(R.color.watsi_blue));

	}

	public void hidePager() {
		tabs.setVisibility(View.GONE);
	}

	public void showPager() {
		tabs.setVisibility(View.VISIBLE);
	}

	@Override
	public void onDestroy() {
		stopService(new Intent(this, PayPalService.class));
		super.onDestroy();
	}

	public static class PatientsPagerAdapter extends FragmentPagerAdapter {

		private static final String LABLES[] = { "Patients", "News" };

		public PatientsPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);

		}

		@Override
		public int getCount() {
			return LABLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				return PatientListFragment.newInstance();
			case 1:
				return PatientFeedFragment.newInstance();

			default:
				return null;
			}
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return LABLES[position];
		}

	}

}
