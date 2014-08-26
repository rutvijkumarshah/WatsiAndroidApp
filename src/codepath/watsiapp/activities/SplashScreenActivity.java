package codepath.watsiapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import codepath.watsiapp.R;

public class SplashScreenActivity extends Activity {

	 // Splash screen timer
	
	/****
	 * Keeping it higher value will be helpful during testing.
	 * 
	 * Ideally should be 2-3 sec.
	 */
    private static int SPLASH_TIME_OUT = 2500; 
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ImageView logo=(ImageView)findViewById(R.id.logo);
        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        logo.startAnimation(rotate);

        new Handler().postDelayed(new Runnable() {
 
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
 
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreenActivity.this, WatsiMainActivity.class);
                startActivity(i);
 
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
