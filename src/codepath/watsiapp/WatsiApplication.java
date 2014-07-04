package codepath.watsiapp;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class WatsiApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		// useful for temporarily storing data to local datastore so that it can be synced later.
		 Parse.enableLocalDatastore(getApplicationContext());
		// Add your initialization code here
		Parse.initialize(this, "tezu5wFV9Z59i6HRSmKdfbcfv3wuUh84QlLrFOjJ", "SfhVI8iQxgdS63YMEPABsLOTaHoEoc78wJOQJFyS");


		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
	    
		// If you would like all objects to be private by default, remove this line.
		defaultACL.setPublicReadAccess(true);
		
		ParseACL.setDefaultACL(defaultACL, true);
		testParseIntegration();
	}
	
	private void testParseIntegration() {
		ParseObject testObject = new ParseObject("TestObject");
		testObject.put("foo", "bar");
		testObject.saveInBackground();
	}

}
