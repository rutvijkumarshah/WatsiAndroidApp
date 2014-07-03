package codepath.watsiapp;

import android.app.Activity;
import android.os.Bundle;

import com.parse.ParseAnalytics;
import com.parse.PushService;

public class WatsiMainActivity extends Activity {
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ParseAnalytics.trackAppOpened(getIntent());
		testParsePushNotification();
		
	}
	
	private void testParsePushNotification() {
		PushService.setDefaultPushCallback(this, WatsiMainActivity.class);
		ParseAnalytics.trackAppOpened(getIntent());
	}
}
