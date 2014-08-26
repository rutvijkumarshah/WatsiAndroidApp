package codepath.watsiapp;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import codepath.watsiapp.activities.PatientDetailActivity;
import codepath.watsiapp.models.Donation;
import codepath.watsiapp.models.Patient;
import codepath.watsiapp.utils.Util;

import com.parse.ParseException;
import com.parse.ParseQuery;

public class PushReceiver extends BroadcastReceiver {
	private static final String TAG = "PushReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

			if (json.has("notif_type")) {
				String notifType = json.getString("notif_type");  
				
				if (notifType.equals("donation_raised")) {
					handleDonationRaisedNotification(context,   
							                         json.getString("donationId"));
				} else if (notifType.equals("fully_funded")) {
					handleNewsForPatient(context, json.getString("patientId"),
							             " has been fully funded !"); 
				} else if (notifType.equals("on_boarded")) {
					handleNewsForPatient(context, json.getString("patientId"),
										 " has joined Watsi as a new patient"); 
				}
			}

		} catch (JSONException e) {
			Log.e(TAG, "JSONException: " + e.getMessage());
		}
	}   

	private void handleDonationRaisedNotification(Context context, String donationId) {
		ParseHelper parseHelper = new ParseHelper(context);
		ParseQuery<Donation> query = parseHelper.findDonationById(donationId);
		try {
			Donation donation = query.getFirst();
			Patient p = donation.getPatient();
			// Now open the patient details as a new activity.
			String msg = Util.formatAmount(donation.getDonationAmount()) +
					     " was donated to " + p.getFirstName();
			postNotification(context, msg, p.getObjectId());
		} catch (ParseException e) {
			Toast.makeText(context, "Failed to open patient details", Toast.LENGTH_SHORT).show();
			Log.e(TAG, "Excepton while geting donation info from donation Id="+donationId);
		}
		
	}
	
	private void handleNewsForPatient(Context context, String patientId, String news)
	{
		ParseQuery<Patient> query = ParseQuery.getQuery(Patient.class);
		try {
			Patient p = query.get(patientId);
			p.fetchIfNeeded();
			String msg = p.getFirstName() + news; 
			postNotification(context, msg, patientId);
		} catch (ParseException e) {
			Log.e(TAG, "Exception while getting patient  from patientId="+patientId);
		}
	}

	private void postNotification(Context ctx, String title, String patientId) {
	     String msg = title;
	     Log.i(TAG, "message: " + msg);

	     NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx)
	     .setContentTitle("Watsi Update")
	     .setContentText(msg)
	     .setSmallIcon(R.drawable.ic_launcher)
	     .setTicker(msg)
	     .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
	     .setAutoCancel(true)
	     .setOnlyAlertOnce(true)
	     .setDefaults(Notification.DEFAULT_VIBRATE); 
	     
	     Intent intent = new Intent(ctx, PatientDetailActivity.class);  
	     intent.putExtra("patient_id", patientId);
	     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | 
	        Intent.FLAG_ACTIVITY_CLEAR_TASK);
	     intent.setAction("codepath.watsiapp.NEWSFEED");
	     intent.addCategory(Intent.CATEGORY_LAUNCHER);
	     PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, intent, 
	    		  PendingIntent.FLAG_CANCEL_CURRENT);
	     builder.setContentIntent(contentIntent);
	     
	     NotificationManager notificationManager = (NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);
	     notificationManager.notify(0, builder.build());
	  }

}