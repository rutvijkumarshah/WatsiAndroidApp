package codepath.watsiapp.fragments;

import java.util.List;

import com.parse.ParseException;
import com.parse.ParseQueryAdapter.OnQueryLoadListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import codepath.watsiapp.R;
import codepath.watsiapp.adapters.HomeFeedAdapter;
import codepath.watsiapp.adapters.PatientAdapter;
import codepath.watsiapp.models.NewsItem;
import codepath.watsiapp.models.Patient;
import codepath.watsiapp.utils.EndlessScrollListener;
import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class PatientFeedFragment extends Fragment {

	private HomeFeedAdapter patientFeedAdapter;
	private eu.erikw.PullToRefreshListView listView;
	private ProgressBar progressBar;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_patient_feed, container,
				false);
		
		progressBar=(ProgressBar)v.findViewById(R.id.pf_progressBar);
		patientFeedAdapter = new HomeFeedAdapter(getActivity());
		patientFeedAdapter.addOnQueryLoadListener(new OnQueryLoadListener<NewsItem>() {

			@Override
			public void onLoaded(List<NewsItem> patients, Exception exp) {
				progressBar.setVisibility(View.INVISIBLE);
				listView.onRefreshComplete();
				if(exp == null) {
					try {
						NewsItem.pinAll(patients);
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
		listView = (PullToRefreshListView) v.findViewById(R.id.patient_feed_list);
		listView.setAdapter(patientFeedAdapter);
		//patientAdapter.loadObjects();
		setupIintialViews();
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setupIintialViews();
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
				patientFeedAdapter.clear();
				patientFeedAdapter.loadObjects();
			}
		});

	}

	public void loadUsers() {
	}

	public static PatientFeedFragment newInstance() {
		PatientFeedFragment fragment = new PatientFeedFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}
}
