
package codepath.watsiapp.models;

import java.util.Date;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

/***
 * Represents Patient in Application.
 * 
 * @author Rutvijkumar Shah
 *
 */
@ParseClassName("Patient")
public class Patient extends ParseObject {

	private static final String EMPTY_STRING="".intern();
	
	public MedicalPartner getMedicalPartner() {
        return (MedicalPartner) getParseObject("medicalPartner");
	}
	public String getFirstName() {
		String fname=getString("firstName");
		if(fname == null) {
			fname=EMPTY_STRING;
		}
		return fname;
		
	}
	public String getLastName() {
		String lname=getString("lastName");
		if( lname == null) {
			lname=EMPTY_STRING;
		}
		return lname;
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
	public String getPhotoUrl() {
		return getParseFile("photo").getUrl();
	
	}
	public ParseFile getPhoto() {
		return getParseFile("photo");
	
	}
	
	
}

