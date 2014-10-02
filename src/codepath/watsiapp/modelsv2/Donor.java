/**
 * 
 */
package codepath.watsiapp.modelsv2;

import java.util.Date;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Represents user who has funded for one or more treatments for Patient(s).
 * 
 * {
            "email": "rutvij.shah@yahoo.com",
            "firstName": "Rutvij",
            "lastName": "Shah",
            "memberSince": {
                "__type": "Date",
                "iso": "2014-07-28T05:56:28.259Z"
            },
            "createdAt": "2014-07-28T05:56:28.342Z",
            "updatedAt": "2014-07-28T05:56:28.342Z",
            "objectId": "SFoGgpWECe"
        }
 * 
 * @author Rutvijkumar Shah
 */
@Table(name = "Donation")
public class Donor extends BaseModel {

	/*** Basic Properties ***/
	private String email;
	private String firstName;
	private String lastName;
	private Date memberSince;
	
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
		return this.save();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Date getMemberSince() {
		return memberSince;
	}

	public void setMemberSince(Date memberSince) {
		this.memberSince = memberSince;
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

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	
}