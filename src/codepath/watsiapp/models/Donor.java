/**
 * 
 */
package codepath.watsiapp.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * @author agarg10
 * Trying to make this class Singleton.
 * As per Parse documentation i cannot have the default constructor private.
 * So i leave it Public, but i make sure i make a call to getInstance when i need object of Donor.
 */
@ParseClassName("Donor")
public class Donor extends ParseObject {
	private static Donor donorInstance;

	public Donor() {
		// Do not Modify any ParseObject fields in this constructor.
	}

	public String getDisplayName() {
		return getString("displayName");
	}

	public void setDisplayName(String value) {
		put("displayName", value);
	}
	/**
	 * Make a call to this function when you need object of class Donor. So that we can have a singleton class. 
	 * 
	 */	
	public static synchronized Donor getInstance() {
		if (donorInstance == null) {
			donorInstance = new Donor();
		}
		return donorInstance;
	}
}
