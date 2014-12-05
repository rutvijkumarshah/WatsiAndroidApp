package codepath.watsiapp;

import android.app.Application;
import codepath.watsiapp.activities.WatsiMainActivity;
import codepath.watsiapp.api.Services;
import codepath.watsiapp.models.Donor;
import codepath.watsiapp.models.MedicalPartner;
import codepath.watsiapp.models.NewsItem;
import codepath.watsiapp.models.Patient;

import com.activeandroid.ActiveAndroid;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.PushService;

public class WatsiApplication extends Application {	
	
	@Override
	public void onCreate() {
		super.onCreate();
		parsePreInit();

		activeAndroidInit();
		
		String apiKey=getString(R.string.parse_REST_Api_Key);
		String clientKey=getString(R.string.parse_client_key);
		String appId=getString(R.string.parse_app_id);
		
		// Add your initialization code here
	    Parse.initialize(this, appId,clientKey);
	    
	    ParseFacebookUtils.initialize(getString(R.string.facebook_app_id));
		configurePush();
		
		Services.init(appId,apiKey);
		
		/**
		 * We dont need automatic user for our app.
		 * It creates problem as well for My profile view as PareLogin consider it valid user and does not force user to login.
		 */
		//ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();

		// If you would like all objects to be private by default, remove this
		// line.
		//These line MUST be commented out if anonymous user needs to save any object in future.
		defaultACL.setPublicReadAccess(true);
		ParseACL.setDefaultACL(defaultACL, true);
        
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
		.cacheInMemory()
		.cacheOnDisc()
		.build();
		
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
        	.defaultDisplayImageOptions(defaultOptions)
            .build();
        ImageLoader.getInstance().init(config);
     
	}

	private void configurePush() {
		// Specify an Activity to handle all pushes by default.
		PushService.subscribe(this, "NewsFeed", WatsiMainActivity.class);  
	}
  
	private void activeAndroidInit() {
		ActiveAndroid.initialize(this);

	}
	/**
	 * Parse has many configurations that can be turned on or off before
	 * initialization. All of them can go inside this function.
	 */
	private void parsePreInit() {
		// Register Models
		ParseObject.registerSubclass(Donor.class);
		ParseObject.registerSubclass(Patient.class);
//		ParseObject.registerSubclass(Donation.class);
		ParseObject.registerSubclass(NewsItem.class);
		ParseObject.registerSubclass(MedicalPartner.class);
		// useful for temporarily storing data to local datastore so that it can
		// be synced later.
		//Parse.enableLocalDatastore(getApplicationContext());
		Parse.enableLocalDatastore(this);
	}

}
