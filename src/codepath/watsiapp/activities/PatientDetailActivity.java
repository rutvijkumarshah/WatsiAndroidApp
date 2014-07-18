package codepath.watsiapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import codepath.watsiapp.R;
import codepath.watsiapp.fragments.NewPatientDetailFragment;

public class PatientDetailActivity extends FragmentActivity {
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
		NewPatientDetailFragment patientDetailFragObj = NewPatientDetailFragment.newInstance(patientId);
		// Replace the container with the new fragment
		ft.replace(R.id.flPatientDetailHolder, patientDetailFragObj);
		// or ft.add(R.id.your_placeholder, new FooFragment());
		// Execute the changes specified
		ft.commit();
	}

	public static void getPatientDetailsIntent(Activity currentActivity,
			String patientId) {
		Intent detailsIntent = new Intent(currentActivity,
				PatientDetailActivity.class);
		detailsIntent.putExtra("patient_id", patientId);
		currentActivity.startActivity(detailsIntent);
	}
}
