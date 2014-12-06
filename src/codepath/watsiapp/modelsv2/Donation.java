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

public class Donation extends BaseModel   {

	private Patient patient;
	private Donor donor;
	private double donationAmount;
	private boolean isAnonymous;
	private String objectId;
	private Date createdAt;    
	private Date updatedAt;
	private Date donationDateTime;
	private Date donationDate;
	
	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	public Donor getDonor() {
		return donor;
	}
	public void setDonor(Donor donor) {
		this.donor = donor;
	}
	public double getDonationAmount() {
		return donationAmount;
	}
	public void setDonationAmount(double donationAmount) {
		this.donationAmount = donationAmount;
	}
	public boolean isAnonymous() {
		return isAnonymous;
	}
	public void setAnonymous(boolean isAnonymous) {
		this.isAnonymous = isAnonymous;
	}
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public Date getDonationDateTime() {
		return donationDateTime;
	}
	public void setDonationDateTime(Date donationDateTime) {
		this.donationDateTime = donationDateTime;
	}
	public Date getDonationDate() {
		return donationDate;
	}
	public void setDonationDate(Date donationDate) {
		this.donationDate = donationDate;
	}
	@Override
	public long persist() {
		return save();
	}
	
	
	
	
}
