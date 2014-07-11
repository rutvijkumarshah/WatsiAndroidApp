package codepath.watsiapp;

import android.app.Application;
import android.graphics.Bitmap;
import codepath.watsiapp.activities.WatsiMainActivity;
import codepath.watsiapp.models.Donor;
import codepath.watsiapp.models.Patient;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.PushService;

public class WatsiApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		parsePreInit();

		// Add your initialization code here
		Parse.initialize(this, "tezu5wFV9Z59i6HRSmKdfbcfv3wuUh84QlLrFOjJ",
				"SfhVI8iQxgdS63YMEPABsLOTaHoEoc78wJOQJFyS");

		configurePush();

		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();

		// If you would like all objects to be private by default, remove this
		// line.
		//These line MUST be commented out if anonymous user needs to save any object in future.
		defaultACL.setPublicReadAccess(true);
		ParseACL.setDefaultACL(defaultACL, true);
		
		//Create global configuration and initialize ImageLoader with this configuration
        DisplayImageOptions options = new DisplayImageOptions.Builder()
        	.displayer(new RoundedBitmapDisplayer(100))
        	.cacheInMemory()
        	.cacheOnDisc()
        	.imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
        	.build();
        
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
            .defaultDisplayImageOptions(options)
            .build();
        ImageLoader.getInstance().init(config);
	}

	private void configurePush() {
		// Specify an Activity to handle all pushes by default.
		PushService.setDefaultPushCallback(this, WatsiMainActivity.class);

	}

	/**
	 * Parse has many configurations that can be turned on or off before
	 * initialization. All of them can go inside this function.
	 */
	private void parsePreInit() {
		// Register Models
		ParseObject.registerSubclass(Donor.class);
		ParseObject.registerSubclass(Patient.class);
		// useful for temporarily storing data to local datastore so that it can
		// be synced later.
		//Parse.enableLocalDatastore(getApplicationContext());
		Parse.enableLocalDatastore(this);
	}

	

}
