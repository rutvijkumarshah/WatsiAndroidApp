package codepath.watsiapp.notifications;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import codepath.watsiapp.R;
import codepath.watsiapp.activities.PatientDetailActivity;
import codepath.watsiapp.api.Services;
import codepath.watsiapp.modelsv2.Donation;
import codepath.watsiapp.modelsv2.DonationsResponse;
import codepath.watsiapp.modelsv2.Patient;
import codepath.watsiapp.modelsv2.PatientsResponse;
import codepath.watsiapp.utils.Util;

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

	private void handleDonationRaisedNotification(final Context context, final String donationId) {
		
		Services.getInstance().getDonationService().findById(donationId, new Callback<DonationsResponse>() {
			
			@Override
			public void success(DonationsResponse response, Response arg1) {
				Donation donation =response.results.get(0);
				
				Patient p = donation.getPatient();
				// Now open the patient details as a new activity.
				String msg = Util.formatAmount(donation.getDonationAmount()) +
						     " was donated to " + p.getFirstName();
			}
			
			@Override
			public void failure(RetrofitError e) {
				Toast.makeText(context, "Failed to open patient details", Toast.LENGTH_SHORT).show();
				Log.e(TAG, "Excepton while geting donation info from donation Id="+donationId);
				
			}
		});
		
		
	}
	
	private void handleNewsForPatient(final Context context, final String patientId, final String news)
	{
		Services.getInstance().getPatientService().findById(patientId, new Callback<Patient>() {

			@Override
			public void failure(RetrofitError e) {
				Log.e(TAG, "Exception while getting patient  from patientId="+patientId);
				
			}

			@Override
			public void success(Patient patient, Response arg1) {
				String msg = patient.getFirstName() + news; 
				postNotification(context, msg, patientId);
				
			}
		});
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