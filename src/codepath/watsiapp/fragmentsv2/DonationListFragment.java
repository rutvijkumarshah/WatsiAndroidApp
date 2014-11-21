/***

The MIT License (MIT)
Copyright © 2014 Rutvijkumar Shah
 
Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the “Software”), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
 
The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.
 
THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 ***/

package codepath.watsiapp.fragmentsv2;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import codepath.watsiapp.R;
import codepath.watsiapp.adapters.DonationAdapter;
import codepath.watsiapp.interfaces.OnDonationStatsCalculatedListener;
import codepath.watsiapp.models.Donation;
import codepath.watsiapp.utils.EndlessScrollListener;

import com.parse.ParseQueryAdapter.OnQueryLoadListener;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class DonationListFragment extends Fragment {

	private DonationAdapter donationAdapter;
	private eu.erikw.PullToRefreshListView listView;
	private ProgressBar progressBar;
	private String donorId;
	private static final String DONOR_ID = "DONOR_ID";
	private OnDonationStatsCalculatedListener listener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		String donor__id = bundle.getString(DONOR_ID);
		this.donorId = donor__id;
	}

	@Override
	public void onAttach(Activity activity) {
		listener = (OnDonationStatsCalculatedListener) activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_donation_list, container,
				false);

		progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
		donationAdapter = new DonationAdapter(getActivity(), donorId);
		donationAdapter
				.addOnQueryLoadListener(new OnQueryLoadListener<Donation>() {

					@Override
					public void onLoaded(List<Donation> donations, Exception exp) {
						progressBar.setVisibility(View.INVISIBLE);
						listView.onRefreshComplete();
						if (exp == null) {
							
							double totalDonations = 0.00;
							final Set<String> treatments = new HashSet<String>();
							for (Donation donation : donations) {
								totalDonations += donation.getDonationAmount();
								treatments.add(donation.getPatient()
										.getObjectId());
							}
							listener.totalDonationsCalculated(totalDonations);
							listener.totalTreatmentsCalculated(treatments
									.size());
							Donation.pinAllInBackground(donations);	
						}
					}

					@Override
					public void onLoading() {
						progressBar.setVisibility(View.VISIBLE);
					}
				});
		listView = (PullToRefreshListView) v.findViewById(R.id.donation_list);
		listView.setAdapter(donationAdapter);
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
				if (totalItemsCount > 0) {
				}
			}

		});
		listView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				donationAdapter.clear();
				donationAdapter.loadObjects();
			}
		});

	}

	public static DonationListFragment newInstance(String donorId) {
		DonationListFragment fragment = new DonationListFragment();
		Bundle args = new Bundle();
		args.putString(DONOR_ID, donorId);
		fragment.setArguments(args);
		return fragment;
	}

}
