package codepath.watsiapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import codepath.watsiapp.R;
import codepath.watsiapp.fragmentsv2.NewPatientDetailFragment;
import codepath.watsiapp.fragmentsv2.PaymentAmountFragment;
import codepath.watsiapp.fragmentsv2.NewPatientDetailFragment.OnDonateClickedListener;

public class PatientDetailActivity extends BaseFragmentActivity implements OnDonateClickedListener {
	private String patientId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient_detail);
		// Get patient id from intent
		patientId = getIntent().getStringExtra("patient_id");
		// Starting the fragment with patient id.
		// Begin the transaction
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		// create object of patientDetailFragment and pass patientId
		NewPatientDetailFragment patientDetailFragObj = NewPatientDetailFragment
				.newInstance(patientId);
		// Replace the container with the new fragment
		ft.replace(R.id.flPatientDetailHolder, patientDetailFragObj);
		// or ft.add(R.id.your_placeholder, new FooFragment());
		// Execute the changes specified
		ft.commit();
	}

	public static void showPatientDetailsActivity(Activity currentActivity,
			String patientId) {
		Intent detailsIntent = new Intent(currentActivity,
				PatientDetailActivity.class);
		detailsIntent.putExtra("patient_id", patientId);
		currentActivity.startActivity(detailsIntent);
		currentActivity.overridePendingTransition(R.anim.slide_in_right,
				R.anim.slide_out_left);
	}

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	@Override
	public void onDonateClicked(String dialogHeader,String patientId,String donateTo) {
	  	FragmentManager fm = getSupportFragmentManager();
	  	PaymentAmountFragment paymenttDialog = PaymentAmountFragment.newInstance(dialogHeader,patientId,donateTo);
	  	paymenttDialog.show(fm, "fragment_payment_amount");
	}

	
}
