package codepath.watsiapp.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import codepath.watsiapp.R;
import codepath.watsiapp.fragments.PatientFeedFragment;
import codepath.watsiapp.fragments.PatientListFragment;

import com.astuetz.PagerSlidingTabStrip;
import com.parse.ParseAnalytics;

public class WatsiMainActivity extends BaseFragmentActivity {
	private ViewPager vpPager;
	private PatientsPagerAdapter adapterViewPager;
	private PagerSlidingTabStrip tabs;
	

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ParseAnalytics.trackAppOpened(getIntent());
		vpPager = (ViewPager) findViewById(R.id.viewPager);
		adapterViewPager = new PatientsPagerAdapter(getSupportFragmentManager());
		vpPager.setAdapter(adapterViewPager);
		setupSlidingTabs(vpPager);
	}
	
	
	private void setupSlidingTabs(ViewPager vpPager2) {
		tabs = (PagerSlidingTabStrip) findViewById(R.id.slidingTabStrip);
        tabs.setViewPager(vpPager);
        tabs.setTextColor(getResources().getColor(R.color.watsi_blue));
        tabs.setIndicatorColor(getResources().getColor(R.color.watsi_blue));
        tabs.setDividerColor(getResources().getColor(R.color.watsi_blue));
        tabs.setShouldExpand(true);
        tabs.setAllCaps(false); 
        
        
		
	}
	
	public static class PatientsPagerAdapter extends FragmentPagerAdapter {
		
		private static final String LABLES[]= {"Watsi News","Help"};
		

		public PatientsPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public int getCount() {
			return LABLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0: 		
				return PatientFeedFragment.newInstance();
			case 1: 
				return PatientListFragment.newInstance();
				
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
