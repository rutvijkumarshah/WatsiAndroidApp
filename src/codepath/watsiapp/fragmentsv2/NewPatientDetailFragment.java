package codepath.watsiapp.fragmentsv2;

import static codepath.watsiapp.utils.Util.startShareIntent;
import static codepath.watsiapp.utils.Util.startShareIntentWithFaceBook;
import static codepath.watsiapp.utils.Util.startShareIntentWithTwitter;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import codepath.watsiapp.R;
import codepath.watsiapp.fragments.PatientImageViewFragment;
import codepath.watsiapp.fragments.PatientSummaryFragment;
import codepath.watsiapp.models.Patient;
import codepath.watsiapp.utils.Util;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.IconPagerAdapter;

public class NewPatientDetailFragment extends Fragment {

	private String patientId;
	private Patient patientObj;
	private TextView donationToGo;
	private TextView story;
	private ImageView fullyFundedCheckMark;

	private TestFragmentAdapter mAdapter;
	private ViewPager mPager;
	private CirclePageIndicator mIndicator;

	private ImageView shareOnTwitter;
	private ImageView shareOnFacebook;
	private ImageView donateView;
	private ImageView shareAction;
	
	private static final String TAG="PATIENT_DETAILS";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		patientId = getArguments().getString("patient_id", "");
		// Get patient object based on id that we have got.
		patientObj = ParseObject.createWithoutData(Patient.class, patientId);
		try {
			patientObj = patientObj.fetchIfNeeded();
		} catch (ParseException e) {
			Log.e(TAG, "Exception while getting patient by id="+patientId+" "+e,e);
		}
	}

	  // Define the listener of the interface type
	  // listener is the activity itself
	  private OnDonateClickedListener listener;
	  
	  // Define the events that the fragment will use to communicate
	  public interface OnDonateClickedListener {
	    public void onDonateClicked(String dialogHeader,String patientId,String donateTo);
	  }
	
	  
	  // Store the listener (activity) that will have events fired once the fragment is attached
	  @Override
	  public void onAttach(Activity activity) {
	    super.onAttach(activity);
	      if (activity instanceof OnDonateClickedListener) {
	        listener = (OnDonateClickedListener) activity;
	      } else {
	        throw new ClassCastException(activity.toString()
	            + " must implement MyListFragment.OnItemSelectedListener");
	      }
	  }
	 
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final boolean isFullyFunded = patientObj.isFullyFunded();
		// Defines the xml file for the fragment
		View view = inflater.inflate(R.layout.fragment_new_patient_detail,
				container, false);

		
		fullyFundedCheckMark = (ImageView) view
				.findViewById(R.id.isFullyFunded);

	
		donationToGo = (TextView) view.findViewById(R.id.donationToGo);
		story = (TextView) view.findViewById(R.id.patientStory);
		Util.applyPrimaryFont(getActivity(), story);

		// Set views to with values
		getActivity().getActionBar().setTitle(patientObj.getFullName());
		
		shareAction = (ImageView) view.findViewById(R.id.shareIv);
		shareOnFacebook =(ImageView)view.findViewById(R.id.share_fb);
		donateView=(ImageView)view.findViewById(R.id.fund_treatment);
		shareOnTwitter=(ImageView) view.findViewById(R.id.share_tw);
		
		story.setText(patientObj.getStory().replace("#$#$", ""));

		shareAction.setTag(patientObj);
		shareAction.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startShareIntent(getActivity(),(Patient)v.getTag());

			}
		});
		shareOnTwitter.setTag(patientObj);
		shareOnTwitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startShareIntentWithTwitter(getActivity(),(Patient)v.getTag());

			}
		});

		shareOnFacebook.setTag(patientObj);
		shareOnFacebook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startShareIntentWithFaceBook(getActivity(),(Patient)v.getTag());

			}
		});

		if (isFullyFunded) {
			fullyFundedCheckMark.setVisibility(View.VISIBLE);
			donateView.setVisibility(View.INVISIBLE);
		}else {
			Util.applyPrimaryFont(getActivity(), donationToGo);
			donationToGo
					.setText(Util.formatAmount(patientObj.getDonationToGo())
							+ " to go");
			donateView.setVisibility(View.VISIBLE);
			
			donateView.setTag(patientObj);
			donateView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					//Put donate dialog here
					listener.onDonateClicked("Donate for "+patientObj.getFullName(),patientObj.getObjectId(),patientObj.getFullName());
				}
			});
			
		}
		
		// Assign views to local variables
		mAdapter = new TestFragmentAdapter(getActivity()
				.getSupportFragmentManager());

		mPager = (ViewPager) view.findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		mIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);

		return view;
	}

	public static NewPatientDetailFragment newInstance(String patientId) {
		NewPatientDetailFragment fragmentObj = new NewPatientDetailFragment();
		Bundle args = new Bundle();
		args.putString("patient_id", patientId);
		fragmentObj.setArguments(args);
		return fragmentObj;
	}

	class TestFragmentAdapter extends FragmentPagerAdapter implements
			IconPagerAdapter {

		public TestFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if (position == 0) {
				return PatientImageViewFragment.newInstance(patientObj
						.getPhotoUrl());
			} else {
				String ageStr = null;
				int age = patientObj.getAge();
				if (age == 0) {
					ageStr = "a cute little baby";
				}
				else if (age == 1) {
					ageStr ="a year old";
				} else {
					ageStr = age + " years old";
				}
				return PatientSummaryFragment.newInstance(
						patientObj.getMedicalNeed(), ageStr,
						patientObj.getCountry(),
						patientObj.getDonationProgressPecentage() + "% funded",
						patientId
						);
			}
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "";
		}

		@Override
		public int getIconResId(int index) {
			return 0;
		}

	}
}
