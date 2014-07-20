package codepath.watsiapp.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import codepath.watsiapp.R;
import codepath.watsiapp.fragments.PatientFeedFragment;
import codepath.watsiapp.fragments.PatientListFragment;
import codepath.watsiapp.utils.Util;

import com.astuetz.PagerSlidingTabStrip;
import com.parse.ParseAnalytics;

public class WatsiMainActivity extends BaseFragmentActivity implements PagerListener {
	private ViewPager vpPager;
	private PatientsPagerAdapter adapterViewPager;
	private PagerSlidingTabStrip tabs;
	
	
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ParseAnalytics.trackAppOpened(getIntent());
		vpPager = (ViewPager) findViewById(R.id.viewPager);
		adapterViewPager = new PatientsPagerAdapter(getSupportFragmentManager(), this);
		vpPager.setAdapter(adapterViewPager);
		setupSlidingTabs(vpPager);
	}
	
	
	private void setupSlidingTabs(ViewPager vpPager2) {
		tabs = (PagerSlidingTabStrip) findViewById(R.id.slidingTabStrip);
		tabs.setShouldExpand(true);
        //tabs.setActivated(false);
        tabs.setAllCaps(false); 
        float dimension = getResources().getDimensionPixelSize(R.dimen.fontsize_small);
        tabs.setTextSize((int)dimension);
        Util.applyPrimaryFont(getApplicationContext(), tabs);
        tabs.setViewPager(vpPager);
        //tabs.setTextColor(getResources().getColor(R.color.watsi_blue));
        tabs.setIndicatorColor(getResources().getColor(R.color.watsi_blue));
        //tabs.setDividerColor(getResources().getColor(R.color.watsi_blue));
        
	}  
	
	public void hidePager() {
		tabs.setVisibility(View.GONE);
	}
	
	public void showPager() {
		tabs.setVisibility(View.VISIBLE);
	}
	
	public static class PatientsPagerAdapter extends FragmentPagerAdapter {
		
		private static final String LABLES[]= {"Patients","News"};
		private PagerListener pagerListener;

		public PatientsPagerAdapter(FragmentManager fragmentManager, PagerListener pagerListener) {
			super(fragmentManager);
			this.pagerListener = pagerListener;
		}

		@Override
		public int getCount() {
			return LABLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				return PatientListFragment.newInstance(pagerListener);
				//return PatientFeedFragment.newInstance();
			case 1: 
				//return PatientListFragment.newInstance();
				return PatientFeedFragment.newInstance(pagerListener);

			default:
				return null;
			}
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return LABLES[position];
		}

	}
}
