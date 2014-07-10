package codepath.watsiapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import codepath.watsiapp.R;
import codepath.watsiapp.adapters.PatientAdapter;

public class PatientListFragment extends Fragment {
	
	private PatientAdapter patientAdapter;
	private ListView listView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_patient_list, container,
				false);
		
		patientAdapter = new PatientAdapter(getActivity());

		// Initialize ListView and set initial view to patientAdapter
		listView = (ListView) v.findViewById(R.id.patient_list);
		listView.setAdapter(patientAdapter);
		patientAdapter.loadObjects();

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setupIintialViews();
	}

	private void setupIintialViews() {

	}

	public void loadUsers() {
	}

	public static PatientListFragment newInstance() {
		PatientListFragment fragment = new PatientListFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}
	

}
