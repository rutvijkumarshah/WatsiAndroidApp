/**
 * 
 */
package codepath.watsiapp.models;

import java.util.Date;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * @author agarg10
 */
@ParseClassName("Donor")
public class Donor extends ParseObject {

	public Donor() {
		// Do not Modify any ParseObject fields in this constructor.
	}

	public String getFirstName() {
		return getString("firstName");
	}

	public void setFirstName(String value) {
		put("firstName", value);
	}

	public String getLastName() {
		return getString("lastName");
	}

	public void setLastName(String value) {
		put("lastName", value);
	}
	
	public String getEmail() {
		return getString("email");
	}

	public void setEmail(String value) {
		put("email", value);
	}
	
	public Date getMemberSince() {
		return getDate("memberSince"); 
	}
	
	
	
}
