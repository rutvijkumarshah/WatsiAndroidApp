package codepath.watsiapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import codepath.watsiapp.R;
import codepath.watsiapp.models.MedicalPartner;
import codepath.watsiapp.models.Patient;
import codepath.watsiapp.utils.Util;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.IconPagerAdapter;

public class NewPatientDetailFragment extends Fragment {
	
	private String patientId;
	private Patient patientObj;
	
	private TextView name;
	private TextView donationToGo;
	private ProgressBar donationProgress;
	private TextView medicalPartner;
	private TextView story;
	private ImageView fullyFundedCheckMark;
	
	private TestFragmentAdapter mAdapter;
	private ViewPager mPager;
	private CirclePageIndicator mIndicator;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		patientId = getArguments().getString("patient_id", "");
		// Get patient object based on id that we have got.
		patientObj = ParseObject.createWithoutData(Patient.class, patientId);
		try {
			patientObj = patientObj.fetchIfNeeded();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Defines the xml file for the fragment
		View view = inflater.inflate(R.layout.fragment_new_patient_detail,container, false);
		
		fullyFundedCheckMark=(ImageView)view.findViewById(R.id.isFullyFunded);
		// Assign views to local variables
		mAdapter = new TestFragmentAdapter(getActivity().getSupportFragmentManager());

		mPager = (ViewPager) view.findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		mIndicator = (CirclePageIndicator)  view.findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);

		if(patientObj.isFullyFunded()) {
			fullyFundedCheckMark.setVisibility(View.VISIBLE);
		}
		name = (TextView) view.findViewById(R.id.name);
		donationToGo = (TextView) view.findViewById(R.id.donationToGo);
		donationProgress = (ProgressBar) view.findViewById(R.id.donationProgress);
		medicalPartner = (TextView) view.findViewById(R.id.medicalPartnerVal);
		story = (TextView) view.findViewById(R.id.patientStory);
		// Set views to with values
		name.setText(patientObj.getFullName());
		int donationProgressPecentage = patientObj.getDonationProgressPecentage();
		donationToGo.setText("$ " + patientObj.getDonationToGo()+ " to go");
		donationProgress.setProgress(donationProgressPecentage);
		story.setText(patientObj.getStory().replace("#$#$", "<p>"));
		patientObj.getMedicalPartner(new GetCallback<MedicalPartner>() {
			@Override
			public void done(final MedicalPartner object, ParseException arg1) {
				medicalPartner.setText(object.getName());
				medicalPartner.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Util.starShowMedicalPartnerIntent(getActivity(), object);
					}
				});
				Util.makeTextViewHyperlink(medicalPartner);
			}
		});

		return view;
	}

	public static NewPatientDetailFragment newInstance(String patientId) {
		NewPatientDetailFragment fragmentObj = new NewPatientDetailFragment();
		Bundle args = new Bundle();
		args.putString("patient_id", patientId);
		fragmentObj.setArguments(args);
		return fragmentObj;
	}
	
	class TestFragmentAdapter extends FragmentPagerAdapter  implements IconPagerAdapter{

		public TestFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if (position == 0) {
				return PatientImageViewFragment.newInstance(patientObj.getPhotoUrl());
			} else {
				String ageStr=null;
				int age=patientObj.getAge();
				if(age < 0) {
					ageStr="baby";
				}
				if(age == 1) {
					ageStr=age+" year old";
				}else {
					ageStr=age+" years old";
				}
				return PatientSummaryFragment.newInstance(patientObj.getMedicalNeed(), ageStr, patientObj.getCountry(), patientObj.getDonationProgressPecentage()+"% funded");
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
			// TODO Auto-generated method stub
			return 0;
		}


	}
}
