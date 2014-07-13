package codepath.watsiapp.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Donation")
public class Donation extends ParseObject {
	
	public Donor getDonor() {
		return (Donor) getParseObject("donar");
	}
	
	public Patient getPatient() {
		return (Patient) getParseObject("patient");
	}
	
	public boolean getIsAnonymous() {
		return getBoolean("isAnonymous");
	}
	
	public long getDonationAmount() {
		return getLong("donationAmount");
	}

}
