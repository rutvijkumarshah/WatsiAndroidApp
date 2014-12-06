/***

The MIT License (MIT)
Copyright � 2014 Rutvijkumar Shah
 
Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the �Software�), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
 
The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.
 
THE SOFTWARE IS PROVIDED �AS IS�, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 ***/

package codepath.watsiapp.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.URLSpan;
import android.util.TypedValue;
import android.widget.TextView;
import android.widget.Toast;
import codepath.watsiapp.activities.ParseDispatchActivity;
import codepath.watsiapp.interfaces.DonationInfoStorage;
import codepath.watsiapp.modelsv2.MedicalPartner;

import com.activeandroid.util.Log;
import com.astuetz.PagerSlidingTabStrip;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PaymentActivity;

public class Util {

	private static final String DATE_FORMAT = "MMM dd yyyy";
	private static String PRIMARY_FONT = "Roboto-Regular.ttf";
	
	private static final Set<String> FB_ACTIVITIES=new HashSet<String>(
			Arrays.asList(
					"com.facebook.composer.shareintent.ImplicitShareIntentHandler",
					"com.facebook.katana.ShareLinkActivity"));

	private static final Set<String> TWITTER_ACTIVITIES=new HashSet<String>(Arrays.asList("com.twitter.android.composer.ComposerActivity"));
	public static final String UNIVERSAL_FUND_URL = "https://watsi.org/funds/universal-fund";

	
	
	private static class UniversalFeedItem implements ShareableItem {
		public UniversalFeedItem() {
		}

		@Override
		public String getShareableUrl() {
			return UNIVERSAL_FUND_URL;
		}
	}

	public static ShareableItem getUniversalShareableItem() {
		return new UniversalFeedItem();
	}

	public static void startFundTreatmentIntent(Activity activity,
			ShareableItem patient) {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(patient
				.getShareableUrl()));
		activity.startActivity(browserIntent);
		// overridePendingTransition(R.anim.right_in, R.anim.left_out);
		// activity.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
	}

	public static Intent getFundTreatmentIntent(Activity activity,DonationInfoStorage donationInfo,String donationTo) {
		
		float amount=donationInfo.getDonationAmount();
		BigDecimal donation=BigDecimal.valueOf(amount);
		
		PayPalPayment payment = new PayPalPayment(donation,
				"USD", donationTo, PayPalPayment.PAYMENT_INTENT_SALE);
		Intent intent = new Intent(activity, PaymentActivity.class);
		intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
		return intent;
	}

	public interface ShareableItem {
		public String getShareableUrl();
	}

	/**
	 * Sets a hyperlink style to the textview.
	 */
	public static void makeTextViewHyperlink(TextView tv) {
		SpannableStringBuilder ssb = new SpannableStringBuilder();
		ssb.append(tv.getText());
		ssb.setSpan(new URLSpan("#"), 0, ssb.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv.setText(ssb, TextView.BufferType.SPANNABLE);
	}

	public static void starShowMedicalPartnerIntent(Activity activity,
			String url) {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		activity.startActivity(browserIntent);
	}

	public static void starShowMedicalPartnerIntent(Activity activity,
			MedicalPartner medicalPartner) {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW,
				Uri.parse(medicalPartner.getWebsiteUrl()));
		activity.startActivity(browserIntent);
	}

	public static void showMyProfileActivity(FragmentActivity activity) {

		activity.startActivity(new Intent(activity, ParseDispatchActivity.class));
	}

	public static void startShareIntent(Activity activity, ShareableItem patient) {
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_TEXT, patient.getShareableUrl());
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Fund Treatment");
		activity.startActivity(Intent.createChooser(shareIntent, "Share Story"));
	}

	private static void startShareIntentWithExplicitSocialActivity(
			Activity ctx, ShareableItem patient, Set<String> socialActivitiesName,
			String displayName) {
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, patient.getShareableUrl());

		try {
			final PackageManager pm = ctx.getPackageManager();
			@SuppressWarnings("rawtypes")
			final List activityList = pm.queryIntentActivities(shareIntent, 0);
			int len = activityList.size();
			for (int i = 0; i < len; i++) {
				final ResolveInfo app = (ResolveInfo) activityList.get(i);
				if (socialActivitiesName.contains(app.activityInfo.name)) {
					final ActivityInfo activity = app.activityInfo;
					final ComponentName name = new ComponentName(
							activity.applicationInfo.packageName, activity.name);
					shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
					shareIntent
							.putExtra(Intent.EXTRA_SUBJECT, "Fund Treatment");
					shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
					shareIntent.setComponent(name);
					ctx.startActivity(shareIntent);
					break;
				}
			}
		} catch (final ActivityNotFoundException e) {
			Toast.makeText(ctx, "Could not find " + displayName + " app",
					Toast.LENGTH_SHORT).show();
		}

	}

	public static void startShareIntentWithTwitter(Activity ctx,
			ShareableItem patient) {
		startShareIntentWithExplicitSocialActivity(ctx, patient,
				TWITTER_ACTIVITIES, "Twitter");
	}

	public static void startShareIntentWithFaceBook(Activity ctx,
			ShareableItem patient) {
		startShareIntentWithExplicitSocialActivity(ctx, patient, FB_ACTIVITIES,
				"Facebook");
	}

	public static Boolean isNetworkAvailable(Context activity) {
		ConnectivityManager connectivityManager = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		boolean isNetworkAvailable = true;
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		isNetworkAvailable = (activeNetworkInfo != null && activeNetworkInfo
				.isConnectedOrConnecting());
		return isNetworkAvailable;
	}

	@SuppressLint("SimpleDateFormat")
	public static String getFormatedDate(Date date) {

		SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMAT);
		sf.setLenient(true);
		return sf.format(date);

	}

	public static float getPixels(Activity activity, float dp) {
		Resources r = activity.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				r.getDisplayMetrics());
		return px;
	}

	public static float getPixelsFont(Activity activity, float sp) {
		Resources r = activity.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
				r.getDisplayMetrics());
		return px;
	}

	public static String formatAmount(double amount) {
		if(amount == 0){
			Log.d("Ohh Hello");
		}
		return String.valueOf("$" + Math.ceil(amount)).split("\\.")[0];
	}

	public static void applyPrimaryFont(Context ctx, TextView textView) {
		Typeface typeface = Typeface.createFromAsset(ctx.getAssets(), "fonts/"
				+ PRIMARY_FONT);
		textView.setTypeface(typeface);
	}

	public static void applyPrimaryFont(Context ctx, PagerSlidingTabStrip view) {
		Typeface typeface = Typeface.createFromAsset(ctx.getAssets(), "fonts/"
				+ PRIMARY_FONT);
		view.setTypeface(typeface, Typeface.BOLD);
	}
	
	public static boolean isValidEmail(CharSequence target) {
		  return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	
	
}