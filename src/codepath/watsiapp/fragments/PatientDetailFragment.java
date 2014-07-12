package codepath.watsiapp.fragments;

import com.parse.ParseObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import codepath.watsiapp.R;
import codepath.watsiapp.models.Patient;

public class PatientDetailFragment extends Fragment {
	private String patientId;
	private Patient patientObj;
    @Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		patientId = getArguments().getString("patient_id", "");
	    // Get patient object based on id that we have got.
	    patientObj = ParseObject.createWithoutData(Patient.class, patientId);
	    Log.d("patient Detail", patientObj.toString());
	}


	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
      // Defines the xml file for the fragment
      View view = inflater.inflate(R.layout.fragment_patient_detail, container, false);
      // Setup handles to view objects here
      // etFoo = (EditText) v.findViewById(R.id.etFoo);
      return view;
    }
    
    
    public static PatientDetailFragment newInstance(String patientId) {
    	PatientDetailFragment fragmentObj = new PatientDetailFragment();
        Bundle args = new Bundle();
        args.putString("patient_id", patientId);
        fragmentObj.setArguments(args);
        return fragmentObj;
    }
}
