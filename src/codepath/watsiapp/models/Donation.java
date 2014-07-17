package codepath.watsiapp.models;

import java.util.Date;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Donation")
public class Donation extends ParseObject {
	
	public Donor getDonor() {
		return (Donor) getParseObject("donor");
	}
	
	public Patient getPatient() {
		return (Patient)getParseObject("patient");
	}
	public void getPatient(GetCallback<Patient> callBack) {
		getParseObject("patient").fetchIfNeededInBackground(callBack);
	}
	
	public boolean getIsAnonymous() {
		return getBoolean("isAnonymous");
	}
	
	public Double getDonationAmount() {
		return getDouble("donationAmount");
	}

	public Date getDonationDate() {
		Date donationDt=getDate("donationDate");
		if(donationDt == null) {
			donationDt=getDate("updatedAt");
		}
		return donationDt;
	}
}
