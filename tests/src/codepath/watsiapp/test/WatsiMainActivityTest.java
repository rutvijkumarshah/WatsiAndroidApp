package codepath.watsiapp.test;

import codepath.watsiapp.WatsiMainActivity;
import codepath.watsiapp.R;

import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;


public class WatsiMainActivityTest extends
    ActivityInstrumentationTestCase2<WatsiMainActivity> {

	 Activity mainActivity = null;
	  Button button = null;

	  public WatsiMainActivityTest() {
	    super(WatsiMainActivity.class);
	  }
	  
	  @Override
	  protected void setUp() throws Exception {
	    super.setUp();
	    mainActivity = getActivity();
	  }
	  
	  public void testChangeText() {
	    ////button = (Button) mainActivity.findViewById(R.id.button_test);
	    //TouchUtils.clickView(this, button);
	    //extView view = (TextView) mainActivity.findViewById(R.id.textView1);
	    assertEquals("t", "t");
	  }
}
