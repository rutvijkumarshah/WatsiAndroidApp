package codepath.watsiapp.fragments;

import static codepath.watsiapp.utils.Util.startFundTreatmentIntent;
import static codepath.watsiapp.utils.Util.startShareIntent;
import static codepath.watsiapp.utils.Util.startShareIntentWithFaceBook;
import static codepath.watsiapp.utils.Util.startShareIntentWithTwitter;
import static com.nineoldandroids.view.ViewPropertyAnimator.animate;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import codepath.watsiapp.R;
import codepath.watsiapp.models.MedicalPartner;
import codepath.watsiapp.models.Patient;
import codepath.watsiapp.utils.OnSwipeTouchListener;
import codepath.watsiapp.utils.Util;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.IconPagerAdapter;

public class NewPatientDetailFragment extends Fragment {

	private String patientId;
	private Patient patientObj;

	//private TextView name;
	private TextView donationToGo;
	private ProgressBar donationProgress;
	private TextView medicalPartner;
	private TextView story;
	private ImageView fullyFundedCheckMark;

	private TestFragmentAdapter mAdapter;
	private ViewPager mPager;
	private CirclePageIndicator mIndicator;

	private ImageView shareOnTwitter;
	private ImageView shareOnFacebook;
	private ImageView donateView;
	private ImageView shareAction;
	private ScrollView scrollView;
	private boolean isStoryFullScreen;
	private LinearLayout profileInfo;
	private LinearLayout donateAndShare;
	private View divder_down;
	private LayoutParams savedScrollViewParams;

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
		final boolean isFullyFunded = patientObj.isFullyFunded();
		// Defines the xml file for the fragment
		View view = inflater.inflate(R.layout.fragment_new_patient_detail,
				container, false);

		profileInfo=(LinearLayout) view.findViewById(R.id.profileInfo);
		donateAndShare=(LinearLayout) view.findViewById(R.id.donateAndShare);
		divder_down=(View) view.findViewById(R.id.divder_down);
		
		fullyFundedCheckMark = (ImageView) view
				.findViewById(R.id.isFullyFunded);
		// Assign views to local variables
		mAdapter = new TestFragmentAdapter(getActivity()
				.getSupportFragmentManager());

		mPager = (ViewPager) view.findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		mIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);

	
		//name = (TextView) view.findViewById(R.id.name);
		donationToGo = (TextView) view.findViewById(R.id.donationToGo);
//		donationProgress = (ProgressBar) view
//				.findViewById(R.id.donationProgress);
		medicalPartner = (TextView) view.findViewById(R.id.medicalPartnerVal);
		story = (TextView) view.findViewById(R.id.patientStory);
		Util.applyPrimaryFont(getActivity(), story);
		// Set views to with values
		getActivity().getActionBar().setTitle(patientObj.getFullName());
		
		int donationProgressPecentage = patientObj
				.getDonationProgressPecentage();
		//donationProgress.setProgress(donationProgressPecentage);
		
		shareAction = (ImageView) view.findViewById(R.id.shareIv);
		shareOnFacebook =(ImageView)view.findViewById(R.id.share_fb);
		donateView=(ImageView)view.findViewById(R.id.fund_treatment);
		shareOnTwitter=(ImageView) view.findViewById(R.id.share_tw);
		
		story.setText(patientObj.getStory().replace("#$#$", ""));
		scrollView=(ScrollView) view.findViewById(R.id.scrollableStoryContainer);
		isStoryFullScreen=false;
		scrollView.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
			  @Override
			  public void onSwipeDown() {
			    collapseScrollView();
			  }
			  
			

			@Override
			  public void onSwipeLeft() {
			    
			  }
			  
			  @Override
			  public void onSwipeUp() {
			    makeScrollViewFullScreen();
			  }
			  
			  @Override
			  public void onSwipeRight() {
			    
			  }
			});
		
//		scrollView.getViewTreeObserver().addOnScrollChangedListener(new OnScrollChangedListener() {
//
//		    @Override
//		    public void onScrollChanged() {
//		    	if(!isStoryFullScreen) {
//		    		makeScrollViewFullScreen();
//		    	
//		    		//make it fullscreen
//		    	}else {
//		    		//move back to org position;
//		    	}
//
//		    }
//
//			
//		});
		
		
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
					startFundTreatmentIntent(getActivity(),(Patient)v.getTag());
				}
			});
			
		}
		return view;
	}

	  private void collapseScrollView() {
		  if(patientObj.isFullyFunded()) {
		  fullyFundedCheckMark.setVisibility(View.VISIBLE);
		  }else {
			  fullyFundedCheckMark.setVisibility(View.INVISIBLE);
		  }
			profileInfo.setVisibility(View.VISIBLE);
			donateAndShare.setVisibility(View.VISIBLE);
			donationToGo.setVisibility(View.VISIBLE);
			divder_down.setVisibility(View.VISIBLE);
			scrollView.setLayoutParams(savedScrollViewParams);
			savedScrollViewParams=null;
		}
	  
	private void makeScrollViewFullScreen() {

		animate(scrollView)
		.setDuration(500).setStartDelay(100)
		.scaleX(1.0f)
		.scaleY(1.0f)
		
				.setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationStart(Animator animation) {
						// Toast.makeText(getActivity(), "Started...",
						// Toast.LENGTH_SHORT).show();
					};

					@Override
					public void onAnimationEnd(Animator animation) {
						// TODO Auto-generated method stub
						super.onAnimationEnd(animation);						
						hideOtherThanStory();
					}
				}).start();;

	}
	
	private void hideOtherThanStory() {
		fullyFundedCheckMark.setVisibility(View.GONE);
		profileInfo.setVisibility(View.GONE);
		donateAndShare.setVisibility(View.GONE);
		donationToGo.setVisibility(View.GONE);
		divder_down.setVisibility(View.GONE);
		savedScrollViewParams = scrollView.getLayoutParams();
		scrollView
		.setLayoutParams(new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		
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
						patientObj.getDonationProgressPecentage() + "% funded");
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
