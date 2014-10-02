
package codepath.watsiapp.modelsv2;

import java.util.Date;

import codepath.watsiapp.utils.Util.ShareableItem;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
/***
 * Represents Patient in Application.
 * 
 * Sample JSON Payload for Patient
	{
	    "age": 1,
	    "country": "Kenya",
	    "firstName": "Michael",
	    "isFullyFunded": true,
	    "medicalNeed": "Michael needs spina bifida closure surgery.",
	    "medicalPartner": {
	                "name": "African Mission Healthcare Foundation",
	                "websiteUrl": "http://www.amhf.us/",
	                "createdAt": "2014-07-10T01:18:32.807Z",
	                "updatedAt": "2014-07-10T01:19:01.898Z",
	                "objectId": "bpQh0xZkuS",
	                "__type": "Object",
	                "className": "MedicalPartner"
	    },
	    "photoUrl": "https://d3w52z135jkm97.cloudfront.net/uploads/profile/image/2224/profile_Michael_Ngari_Muchiri_pre_op_picture1_AMHF_BKKH_.jpg",
	    "profileUrl": "https://watsi.org/profile/d2b79fee498b-michael",
	    "receivedDonation": 980,
	    "story": "The days treatment...",
	    "targetDonation": 980,
	    "createdAt": "2014-08-04T05:08:49.475Z",
	    "updatedAt": "2014-08-04T20:07:12.902Z",
	    "objectId": "Gibye7bUBM"
	}

  
  @author Rutvijkumar Shah

 ****/

@Table(name = "Patient")
public class Patient extends BaseModel implements ShareableItem{


	/*** Basic Properties ***/
	@Column(name = "age")
	private int age;
	
	@Column(name = "country")
	private String country;

	@Column(name = "firstName")
	private String firstName;

	@Column(name = "lastName")
	private String lastName;

	
	@Column(name = "is_fully_funded")
	private boolean isFullyFunded;
	
	@Column(name = "medicalNeed")
	private String medicalNeed;

	@Column(name = "photo_url")
	private String photoUrl;
	
	@Column(name = "profile_url")
	private String profileUrl;
	
	@Column(name = "received_donation")
	private double receivedDonation;
	
	@Column(name = "story")
	private String story;
	
	@Column(name = "target_donation")
	private double targetDonation;
	
	/*** Associated objects *****/
	
	@Column(name = "medical_partner")
	private MedicalPartner medicalPartner;
	
	/**** 
	 * Common fields for every Entity 
	 * Ideally should be part of BaseModel kind of class,
	 * But as AndroidOrm does not support inheritance it has to be duplicated until I find some good workaround.
	 * 
	 * ***/
	@Column(name = "object_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private String objectId;
    
    @Column(name = "created_at")
    private Date createdAt;
    
    @Column(name = "updated_at")
    private Date updatedAt;


	
	@Override
	public long persist() {
		if(medicalPartner!=null) {
			medicalPartner.persist();
		}
		return this.save();

	}
	public void reload(Patient patient) {
		
		this.isFullyFunded=patient.isFullyFunded;
		this.photoUrl=patient.photoUrl;
		this.profileUrl=patient.profileUrl;
		this.receivedDonation=patient.receivedDonation;
		this.targetDonation=patient.targetDonation;
	}
	@Override
	public String getShareableUrl() {
		return getProfileUrl();
	}
	public int getDonationProgressPecentage() {
		double donationReceived = getReceivedDonation();
		double targetDonation = getTargetDonation();
		return (int) ((donationReceived*100)/targetDonation);
	}
	public double getDonationToGo() {
		return getTargetDonation() - getReceivedDonation();
	}
	
	///Getter Setters
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getFullName() {
		return String.format("%s%s", this.firstName,getLastName());
	}

	private String getLastName() {
		return this.lastName == null ? "" : " "+this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public boolean isFullyFunded() {
		return isFullyFunded;
	}
	public void setFullyFunded(boolean isFullyFunded) {
		this.isFullyFunded = isFullyFunded;
	}
	public String getMedicalNeed() {
		return medicalNeed;
	}
	public void setMedicalNeed(String medicalNeed) {
		this.medicalNeed = medicalNeed;
	}
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	public String getProfileUrl() {
		return profileUrl;
	}
	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}
	public double getReceivedDonation() {
		return receivedDonation;
	}
	public void setReceivedDonation(double receivedDonation) {
		this.receivedDonation = receivedDonation;
	}
	public String getStory() {
		return story;
	}
	public void setStory(String story) {
		this.story = story;
	}
	public double getTargetDonation() {
		return targetDonation;
	}
	public void setTargetDonation(double targetDonation) {
		this.targetDonation = targetDonation;
	}
	public MedicalPartner getMedicalPartner() {
		return medicalPartner;
	}
	public void setMedicalPartner(MedicalPartner medicalPartner) {
		this.medicalPartner = medicalPartner;
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
	


	
	
}

