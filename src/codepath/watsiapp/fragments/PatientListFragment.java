package codepath.watsiapp.fragments;

import java.util.List;

import com.parse.ParseException;
import com.parse.ParseQueryAdapter.OnQueryLoadListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import codepath.watsiapp.R;
import codepath.watsiapp.adapters.PatientAdapter;
import codepath.watsiapp.models.Patient;

public class PatientListFragment extends Fragment {
	
	private PatientAdapter patientAdapter;
	private ListView listView;
	private ProgressBar progressBar;
	//private PullToRefreshLayout pullToRefreshLayout;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_patient_list, container,
				false);
		
		progressBar=(ProgressBar)v.findViewById(R.id.progressBar);
		patientAdapter = new PatientAdapter(getActivity());
		patientAdapter.addOnQueryLoadListener(new OnQueryLoadListener<Patient>() {

			@Override
			public void onLoaded(List<Patient> patients, Exception exp) {
				progressBar.setVisibility(View.INVISIBLE);
				if(exp == null) {
					try {
						Patient.pinAll(patients);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}else {
					//TODO:RUTVIJ
					//SHOW Failure Toast
				}
			}

			@Override
			public void onLoading() {
				progressBar.setVisibility(View.VISIBLE);
			}
		});
	    //pullToRefreshLayout = (PullToRefreshLayout) v.findViewById(R.id.ptr_layout);
	    
	    //pullToRefreshAttacher = pullToRefreshLayout.createPullToRefreshAttacher(getActivity(), null);
	    
		// Initialize ListView and set initial view to patientAdapter
		listView = (ListView) v.findViewById(R.id.patient_list);
		listView.setAdapter(patientAdapter);
		//patientAdapter.loadObjects();
		setupIintialViews();
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	private void setupIintialViews() {
		

		
//		 ActionBarPullToRefresh.from(getActivity())
//         // Mark All Children as pullable
//         .allChildrenArePullable()
//         // Set a OnRefreshListener
//         // Finally commit the setup to our PullToRefreshLayout
//         .setup(pullToRefreshLayout);
	}


	public static PatientListFragment newInstance() {
		PatientListFragment fragment = new PatientListFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}
	

}
