package codepath.watsiapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import codepath.watsiapp.R;

public class PatientDetailFragment extends Fragment {
	private String patientId;
    @Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		patientId = getArguments().getString("patient_id", "");
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
