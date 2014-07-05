
package codepath.watsiapp.models;

import java.util.Date;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Patient")
public class Patient extends ParseObject {

	
	public String getFirstName() {
		return getString("firstName");
		
	}
	public String getLastName() {
		return getString("lastName");
	}
	public Date getDateAdded() {
		return getDate("createdAt");
	}
	public String getMedicalNeed() {
		return getString("medicalNeed");
	}
	public double getTargetDonation() {
		return (Double) getNumber("targetDonation");
		
	}
	public double getDonationReceived() {
		return (Double)getNumber("donationReceived");
	}
	public boolean isFullyFunded() {
		return (Boolean) getBoolean("isFullyFunded");
	}
	public Date getDateFullyFunded() {
		return getDate("dateFullyFunded");
	}
	public int getAge() {
		return (Integer)getNumber("age");
	}
	public String getCountry() {
		return getString("country");
	}
	public String getStory() {
		return getString("story");
	}
	
	
	//image , medical partner
	
	
}

