package codepath.watsiapp;

import android.app.Application;
import codepath.watsiapp.activities.WatsiMainActivity;
import codepath.watsiapp.models.Donor;

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
		defaultACL.setPublicReadAccess(true);

		ParseACL.setDefaultACL(defaultACL, true);
		testParseIntegration();
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
		// useful for temporarily storing data to local datastore so that it can
		// be synced later.
		Parse.enableLocalDatastore(getApplicationContext());

	}

	private void testParseIntegration() {
		ParseObject testObject = new ParseObject("TestObject");
		testObject.put("foo", "bar");
		testObject.saveInBackground();
	}

}
