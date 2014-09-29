
package codepath.watsiapp.modelsv2;

import java.util.Date;


import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import static codepath.watsiapp.utils.GsonHelper.getGson;
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
public class Patient extends BaseModel{


	/*** Basic Properties ***/
	@Column(name = "age")
	public int age;
	
	@Column(name = "country")
	public String country;

	@Column(name = "firstName")
	public String firstName;

	@Column(name = "lastName")
	public String lastName;

	
	@Column(name = "is_fully_funded")
	public boolean isFullyFunded;
	
	@Column(name = "medicalNeed")
	public String medicalNeed;

	@Column(name = "photo_url")
	public String photoUrl;
	
	@Column(name = "profile_url")
	public String profileUrl;
	
	@Column(name = "received_donation")
	public double receivedDonation;
	
	@Column(name = "story")
	public String story;
	
	@Column(name = "target_donation")
	public double targetDonation;
	
	/*** Associated objects *****/

	
	@Column(name = "medical_partner")
	public MedicalPartner medicalPartner;
	
	/**** 
	 * Common fields for every Entity 
	 * Ideally should be part of BaseModel kind of class,
	 * But as AndroidOrm does not support inheritance it has to be duplicated until I find some good workaround.
	 * 
	 * ***/
	@Column(name = "object_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	public String objectId;
    
    @Column(name = "created_at")
	public Date createdAt;
    
    @Column(name = "updated_at")
	public Date updatedAt;

	public String getFullName() {
		return String.format("%s%s", this.lastName,getLastName());
	}

	private String getLastName() {
		return this.lastName == null ? "" : " "+this.lastName;
	}

	
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
	public static  Patient fromJson(String jsonString) {
		Patient patient=getGson().fromJson(jsonString, Patient.class);
		return patient;
	}

	
}

