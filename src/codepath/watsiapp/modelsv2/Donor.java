/**
 * 
 */
package codepath.watsiapp.modelsv2;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.util.Log;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import static codepath.watsiapp.utils.GsonHelper.getGson;

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
	public String email;
	public String firstName;
	public String lastName;
	public Date memberSinceDate;
	
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

	@SuppressLint("SimpleDateFormat")
	public static  Donor fromJson(String jsonString) {
		Donor donor=getGson().fromJson(jsonString, Donor.class);
		JSONObject memberSince=null;
		String memberSinceStr=null;
		try {
			memberSince = new JSONObject(jsonString).getJSONObject("memberSince");
			memberSinceStr=memberSince.getString("iso");
			Date dt=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(memberSinceStr);
			donor.memberSinceDate=dt;//Assign membersince Date from JSON Object
		} catch (JSONException e) {
			Log.e("JSON_DESERIALIZATION","Exception while parsing jsonString "+jsonString,e);
		} catch (ParseException e) {
			Log.e("DATE_PARSING","Exception while parsing Date "+memberSinceStr,e);
		}
		
		return donor;
	}
}