
package codepath.watsiapp.modelsv2;

import java.util.Date;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/***
 * Represents Patient in Application.
 * 
 * @author Rutvijkumar Shah
 *
 */
@Table(name = "Patient")
public class Patient extends BaseModel{

	/*** Basic Properties ***/
	@Column(name = "age")
	public int age;
	
	@Column(name = "country")
	public String country;

	@Column(name = "firstName")
	public String firstName;
	
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

	@Override
	public long persist() {
		if(medicalPartner!=null) {
			medicalPartner.persist();
		}
		return this.save();
	}
    
	
}

