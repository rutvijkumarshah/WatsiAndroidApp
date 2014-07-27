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

package codepath.watsiapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsHelper {

	private Context ctx;
	private SharedPreferences prefs;

	private static final String PACKAGE="codepath.watsiapp";
	private static final String USER_FULL_NAME=PACKAGE+".PREF_USER_FULL_NAME";
	private static final String USER_EMAIL=PACKAGE+".PREF_USER_EMAIL_NAME";
	private static final String PATIENT_ID=PACKAGE+".PREF_PATIENT_ID";
	private static final String DONATAION_AMOUNT=PACKAGE+".PREF_DONATION_AMOUNT";

	public PrefsHelper(Context ctx) {
		this.ctx=ctx;
		prefs = this.ctx.getSharedPreferences(
			      PACKAGE, Context.MODE_PRIVATE);

	}
	public float getDonationAmount() {
		return prefs.getFloat(DONATAION_AMOUNT, 0.0f);
	}

	public String getPatientId() {
		return prefs.getString(PATIENT_ID,null);
	}

	public String getUserFullName() {
		return prefs.getString(USER_FULL_NAME,"");
	}
	
	public String getUserEmail() {
		return prefs.getString(USER_EMAIL,"");
	}
	
	public void setUserFullName(String fullName) {
		prefs.edit().putString(USER_FULL_NAME, fullName).apply();
		
	}
	public void setUserEmailAddress(String email) {
		prefs.edit().putString(USER_EMAIL, email).apply();
	}
	
	public void setDonationAmount(float value) {
		prefs.edit().putFloat(DONATAION_AMOUNT, value).apply();
	}
	public void setPatientId(String value) {
		prefs.edit().putString(PATIENT_ID, value).apply();
	}
}
