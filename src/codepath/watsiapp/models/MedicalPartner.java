package codepath.watsiapp.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("MedicalPartner")
public class MedicalPartner extends ParseObject {
	
	public MedicalPartner(){
		
	}
	
	public String getName() {
		return getString("name");
	}

	public void setName(String value) {
		put("name", value);
	}
	
	public String getDescription() {
		return getString("description");
	}

	public void setDescription(String value) {
		put("description", value);
	}
	
	public String getWebsiteUrl() {
		return getString("websiteUrl");
	}

	public void setWebsiteUrl(String value) {
		put("websiteUrl", value);
	}
}
