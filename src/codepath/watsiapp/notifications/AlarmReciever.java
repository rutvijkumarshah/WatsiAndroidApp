package codepath.watsiapp.notifications;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import codepath.watsiapp.R;
import codepath.watsiapp.R.drawable;
import codepath.watsiapp.activities.WatsiMainActivity;

public class AlarmReciever extends BroadcastReceiver {
	private static final String TAG = "AlarmReciever";

	@Override
	public void onReceive(Context context, Intent intent) {
	     Log.i(TAG, "Got intent :" + intent.getAction());

		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			// Set the alarm to trigger at 10 AM
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.set(Calendar.HOUR_OF_DAY, 10);

			// Put a boolean in the intent when the alarm triggers
			Intent actIntent = new Intent(context, AlarmReciever.class);
			actIntent.setAction("codepath.watsiapp.DAILY_ALARM");
			PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

			AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
			        					 AlarmManager.INTERVAL_DAY, alarmIntent); 
			
		} else if (intent.getAction().equals("codepath.watsiapp.DAILY_ALARM")) {
			// This is an alarm. If it is the first day of the month
			// then send out a notification
			Calendar cal = Calendar.getInstance();
			int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
			if (dayOfMonth == 1) {
				triggerNotificationToDonate(context, "First day of the month!");
			}
			// else ignore it
		}  
	}

	private void triggerNotificationToDonate(Context context, String donationReason) {
		 String msg = "It's a good time to donate!";
	     Log.i(TAG, "message: " + msg);     

	     NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
	     .setContentTitle(msg)
	     .setContentText(donationReason)
	     .setSmallIcon(R.drawable.ic_launcher)
	     .setTicker(donationReason)
	     .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
	     .setAutoCancel(true)
	     .setOnlyAlertOnce(true)
	     .setDefaults(Notification.DEFAULT_VIBRATE); 
	     
	     Intent intent = new Intent(context, WatsiMainActivity.class);  
	     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
	     intent.addCategory(Intent.CATEGORY_LAUNCHER);
	     PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 
	    		  PendingIntent.FLAG_CANCEL_CURRENT);
	     builder.setContentIntent(contentIntent);

	     NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
	     notificationManager.notify(0, builder.build());
	}
}
