package codepath.watsiapp.fragmentsv2;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import codepath.watsiapp.R;
import codepath.watsiapp.adaptersv2.PatientAdapter;
import codepath.watsiapp.api.Services;
import codepath.watsiapp.modelsv2.Patient;
import codepath.watsiapp.modelsv2.PatientsResponse;
import codepath.watsiapp.utils.EndlessScrollListener;

import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class PatientListFragment extends Fragment {
	
	private PatientAdapter patientAdapter;
	private eu.erikw.PullToRefreshListView listView;
	private ProgressBar progressBar;
	private ArrayList<Patient> patients=new ArrayList<Patient>();
	/***
	 */
	/**
	 * Trade-off here. 
	 * 
	 * Keeping lower number improves startup time specially loading images and animation.
	 * But will increase no of requests app makes.
	 * 
	 */
	public static final int PAGE_SIZE = 5; 

	
	private static final String TAG="PATIENT_LIST";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		patientAdapter=new PatientAdapter(getActivity(), patients);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_patient_list, container,
				false);
		
		progressBar=(ProgressBar)v.findViewById(R.id.progressBar);
		
		listView = (PullToRefreshListView) v.findViewById(R.id.patient_list);
		
		
		SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(patientAdapter);
		swingBottomInAnimationAdapter.setInitialDelayMillis(0);
		swingBottomInAnimationAdapter.setAbsListView(listView);
		swingBottomInAnimationAdapter.setAnimationDurationMillis(5000);
		listView.setAdapter(swingBottomInAnimationAdapter);

		setupIintialViews();
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		progressBar.setVisibility(View.VISIBLE);
		populateData(PAGE_SIZE,0);
	}

	private void setupIintialViews() {

		listView.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				if(totalItemsCount > 0) {
					populateData(PAGE_SIZE,totalItemsCount);
					
				}
			}

		});
		listView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				progressBar.setVisibility(View.VISIBLE);
				patientAdapter.clear();
				
				populateData(PAGE_SIZE,0,true);
			}
		});
	}


	private void populateData(int pageSize,int skip) {
		populateData(pageSize,skip,false);
	}
	
	private void populateData(int pageSize,int skip,final boolean isPulledToRefresh) {
		Services.getInstance().getPatientService().getPatients(pageSize, skip, new Callback<PatientsResponse>() {
			
			@Override
			public void success(PatientsResponse patientsResponse, Response arg1) {
				if(isPulledToRefresh) {
					listView.onRefreshComplete();
				}
				patientAdapter.addAll(patientsResponse.results);
				patientAdapter.notifyDataSetChanged();
				progressBar.setVisibility(View.INVISIBLE);
				
			}
			
			@Override
			public void failure(RetrofitError err) {
			
				Toast.makeText(PatientListFragment.this.getActivity(), "Error:"+err.getLocalizedMessage(), Toast.LENGTH_LONG).show();
				progressBar.setVisibility(View.INVISIBLE);
			}
		});
	}
	public static PatientListFragment newInstance() {
		PatientListFragment fragment = new PatientListFragment();
		
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}
	

}
