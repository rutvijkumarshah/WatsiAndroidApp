
package codepath.watsiapp.models;

import java.util.Date;

import codepath.watsiapp.utils.Util.ShareableItem;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;

/***
 * Represents Patient in Application.
 * 
 * @author Rutvijkumar Shah
 *
 */
@ParseClassName("Patient")
public class Patient extends ParseObject implements ShareableItem {
	private static final String EMPTY_STRING="".intern();
	
	public void getMedicalPartner(GetCallback<MedicalPartner> callBack) {
		getParseObject("medicalPartner").fetchIfNeededInBackground(callBack);
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
		return  getNumber("targetDonation").doubleValue();
		
	}
	public double getDonationReceived() {
		return getNumber("receivedDonation").doubleValue();
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
		return getString("photoUrl");
	
	}
	public String getProfileUrl() {
		return getString("profileUrl");
	
	}
	public String getFullName() {
		return getFirstName()+" "+getLastName();
	}
	public int getDonationProgressPecentage() {
		double donationReceived = getDonationReceived();
		double targetDonation = getTargetDonation();
		return (int) ((donationReceived*100)/targetDonation);
	}
	public double getDonationToGo() {
		return getTargetDonation() - getDonationReceived();
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getFullName() +" "+ getProfileUrl()+" "+getAge()+" "+getCountry()+" "+getDonationProgressPecentage();
	}
	@Override
	public String getShareableUrl() {
		return getProfileUrl();
	}
	
}

