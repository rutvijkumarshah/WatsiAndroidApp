/*
 *  Copyright (c) 2014, Facebook, Inc. All rights reserved.
 *
 *  You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
 *  copy, modify, and distribute this software in source code or binary form for use
 *  in connection with the web services and APIs provided by Facebook.
 *
 *  As with any software that integrates with the Facebook platform, your use of
 *  this software is subject to the Facebook Developer Principles and Policies
 *  [http://developers.facebook.com/policy/]. This copyright notice shall be
 *  included in all copies or substantial portions of the software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 *  FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 *  COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 *  IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package codepath.watsiapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import codepath.watsiapp.R;

import com.facebook.Session;
import com.facebook.android.Facebook;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;


public class DonarProfileActivity extends Activity {
  private TextView titleTextView;
  private TextView emailTextView;
  private TextView nameTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_donor_profile);
//    titleTextView = (TextView) findViewById(R.id.profile_title);
//    emailTextView = (TextView) findViewById(R.id.profile_email);
//    nameTextView = (TextView) findViewById(R.id.profile_name);
//    titleTextView.setText(R.string.profile_title_logged_in);

    Session session = ParseFacebookUtils.getSession();
    ParseUser u=null;
    
  }

  @Override
  protected void onStart() {
    super.onStart();
    // Set up the profile page based on the current user.
    ParseUser user = ParseUser.getCurrentUser();
    showProfile(user);
  }

  /**
   * Shows the profile of the given user.
   *
   * @param user
   */
  private void showProfile(ParseUser user) {
//    if (user != null) {
//      emailTextView.setText(user.getEmail());
//      String fullName = user.getString("name");
//      if (fullName != null) {
//        nameTextView.setText(fullName);
//      }
//    }
  }
}
