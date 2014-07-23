/***

The MIT License (MIT)
Copyright © 2014 Rutvijkumar Shah
 
Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the “Software”), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
 
The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.
 
THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 ***/

package codepath.watsiapp.activities;

import com.parse.ParseUser;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import codepath.watsiapp.R;
import codepath.watsiapp.utils.Util;

public class BaseFragmentActivity extends FragmentActivity {

	protected MenuItem myprofile;
	private MenuItem logout;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.common_menus, menu);

		myprofile = menu.findItem(R.id.action_profileView);
		logout = menu.findItem(R.id.action_logout);
		checkLogOutVisible();
		return true;
	}

	protected void checkLogOutVisible() {
		if (logout != null) {
			if (ParseUser.getCurrentUser() == null) {
				logout.setVisible(false);
			} else {
				logout.setVisible(true);
			}
		}

	}

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final int menuItemId = item.getItemId();
		if (menuItemId == R.id.action_profileView) {
			Util.showMyProfileActivity(this);
		}
		if (menuItemId == R.id.action_logout) {
			logout();
			logout.setVisible(false);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		checkLogOutVisible();
	}

	private void logout() {
		ParseUser.logOut();

		// FLAG_ACTIVITY_CLEAR_TASK only works on API 11, so if the user
		// logs out on older devices, we'll just exit.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			Intent intent = new Intent(BaseFragmentActivity.this,
					WatsiMainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		} else {
			finish();
		}
	}

}
