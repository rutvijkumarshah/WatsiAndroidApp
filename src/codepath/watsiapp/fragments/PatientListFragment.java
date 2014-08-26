package codepath.watsiapp.fragments;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import codepath.watsiapp.R;
import codepath.watsiapp.adapters.PatientAdapter;
import codepath.watsiapp.models.Patient;
import codepath.watsiapp.utils.EndlessScrollListener;

import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.parse.ParseException;
import com.parse.ParseQueryAdapter.OnQueryLoadListener;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class PatientListFragment extends Fragment {
	
	private PatientAdapter patientAdapter;
	private eu.erikw.PullToRefreshListView listView;
	private ProgressBar progressBar;
	
	private static final String TAG="PATIENT_LIST";
	
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
				listView.onRefreshComplete();
				if(exp == null) {
					try {
						Patient.pinAll(patients);
					} catch (ParseException e) {
						Log.e(TAG, "Exception while storing patients object to db :"+e,e);
					}
				}else {
					Toast.makeText(getActivity(), "Failure. Please check network connection", Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onLoading() {
				progressBar.setVisibility(View.VISIBLE);
			}
		});
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
	}

	private void setupIintialViews() {

		listView.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				if(totalItemsCount > 0) {
					/***
					 * FIX_REQUIRED : throws IndexOutOfBound alternatively.
					 */
					//patientAdapter.loadNextPage();
				}
			}

		});
		listView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				patientAdapter.clear();
				patientAdapter.loadObjects();
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
