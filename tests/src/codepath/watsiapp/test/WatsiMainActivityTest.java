package codepath.watsiapp.test;

import codepath.watsiapp.WatsiMainActivity;
import codepath.watsiapp.R;

import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;

public class WatsiMainActivityTest extends
    ActivityInstrumentationTestCase2<WatsiMainActivity> {
  
  Activity mainActivity = null;
  Button button = null;

  public WatsiMainActivityTest() {
    super(MainActivity.class);
  }
  
  @Override
  protected void setUp() throws Exception {
    super.setUp(); 
  }
  
  public void testChangeText() {
  }

}
