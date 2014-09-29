package codepath.watsiapp.modelsv2;

import static codepath.watsiapp.utils.GsonHelper.getGson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;

/**
 * Represents Donation fund for Treatment of Patient.
 * Non persisted object
 * 
 * @author Rutvijkumar Shah
 * 
{
    "patient": {
        ....
    },
    "donor": {
        ....
    },
    "donationAmount": 117,
    "donationDate": {
        "__type": "Date",
        "iso": "2014-08-17T23:25:57.347Z"
    },
    "isAnonymous": true,
    "createdAt": "2014-08-17T23:25:57.422Z",
    "updatedAt": "2014-08-17T23:25:57.422Z",
    "objectId": "YrHwFSTvVs"
} 
 
 
 */

public class Donation  {

	public Patient patient;
	public Donor donor;
	public double donationAmount;
	public boolean isAnonymous;
	public String objectId;
	public Date createdAt;    
	public Date updatedAt;
	public Date donationDateTime;

	
	
	@SuppressLint("SimpleDateFormat")
	public static  Donation fromJson(String jsonString) {
		Donation donation=getGson().fromJson(jsonString, Donation.class);
		JSONObject memberSince=null;
		String memberSinceStr=null;
		try {
			memberSince = new JSONObject(jsonString).getJSONObject("donationDate");
			memberSinceStr=memberSince.getString("iso");
			Date dt=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(memberSinceStr);
			donation.donationDateTime=dt;//Assign donationTimestamp Date from JSON Object
		} catch (JSONException e) {
			Log.e("JSON_DESERIALIZATION","Exception while parsing jsonString "+jsonString,e);
		} catch (ParseException e) {
			Log.e("DATE_PARSING","Exception while parsing Date "+memberSinceStr,e);
		}
		
		return donation;
	}
	
}
