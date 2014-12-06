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
import codepath.watsiapp.api.Services;
import codepath.watsiapp.modelsv2.NewsItem;
import codepath.watsiapp.modelsv2.NewsItemsResponse;
import codepath.watsiapp.modelsv2.Patient;
import codepath.watsiapp.utils.EndlessScrollListener;

import com.activeandroid.util.Log;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class PatientFeedFragment extends Fragment {

	private codepath.watsiapp.adaptersv2.HomeFeedAdapter adapter;
	private eu.erikw.PullToRefreshListView listView;
	private ProgressBar progressBar;
	
	private static final String TAG="PATIENT_FEED";
	private ArrayList<codepath.watsiapp.modelsv2.NewsItem> newsItems=new ArrayList<codepath.watsiapp.modelsv2.NewsItem>();
	
	public static final int PAGE_SIZE = 20; 

	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter=new codepath.watsiapp.adaptersv2.HomeFeedAdapter(getActivity(), newsItems);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_patient_feed, container,
				false);

		progressBar=(ProgressBar)v.findViewById(R.id.pf_progressBar);
		listView = (PullToRefreshListView) v.findViewById(R.id.patient_feed_list);
		SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(adapter);
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
		setupIintialViews();
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
				adapter.clear();
				
				populateData(PAGE_SIZE,0,true);
			}
		});
	}

	private void populateData(int pageSize,int skip) {
		populateData(pageSize,skip,false);
	}
	private void populateData(final int pageSize,final int skip,final boolean isPulledToRefresh) {
		Services.getInstance().getNewsItemService().getNewsItems(pageSize, skip, new Callback<NewsItemsResponse>() {

			@Override
			public void failure(RetrofitError err) {

				Log.e("Error while loading page :"+pageSize+" and skip="+skip,err);
				Toast.makeText(getActivity(), "Error:"+err, Toast.LENGTH_LONG).show();
				progressBar.setVisibility(View.INVISIBLE);
				
			}

			@Override
			public void success(NewsItemsResponse response, Response arg1) {
				
				if(isPulledToRefresh) {
					NewsItem.deleteAll(NewsItem.class);
				}
				
				for (NewsItem obj : response.results) {
					adapter.add(obj);
					obj.persist();
				}
				adapter.notifyDataSetChanged();
				progressBar.setVisibility(View.INVISIBLE);
				if(isPulledToRefresh) {
					listView.onRefreshComplete();
				}
				
			}
		});
		
	}
	public static PatientFeedFragment newInstance() {
		PatientFeedFragment fragment = new PatientFeedFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}
}
