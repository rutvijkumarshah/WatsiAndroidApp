package codepath.watsiapp.modelsv2;

import java.util.Date;
import java.util.List;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
@Table(name = "MedicalPartner")
public class MedicalPartner extends BaseModel{
	
	
/*****
 * 
 * 
 *   Sample JSON Data Represent Medical Partner object 
 	{
	 	"name": "Possible",
	    "websiteUrl": "http://possiblehealth.org/",
	    "createdAt": "2014-07-04T14:02:50.050Z",
	    "updatedAt": "2014-07-04T14:03:58.894Z",
	    "objectId": "5z5ajvwWJl"
    }
 * 
 */

	@Column(name = "med_partner_name")
	private String name;
	
	@Column(name = "web_url")
	private String websiteUrl;

	public static List<MedicalPartner> findAll() {
		return new Select().from(MedicalPartner.class).execute();
	}


	/**** 
	 * Common fields for every Entity 
	 * Ideally should be part of BaseModel kind of class,
	 * But as AndroidOrm does not support inheritance it has to be duplicated until I find some good workaround.
	 * 
	 * ***/
	@Column(name = "object_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private  String objectId;
    
    @Column(name = "created_at")
    private  Date createdAt;
    
    @Column(name = "updated_at")
    private  Date updatedAt;

	@Override
	public long persist() {
		return this.save();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWebsiteUrl() {
		return websiteUrl;
	}

	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
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
