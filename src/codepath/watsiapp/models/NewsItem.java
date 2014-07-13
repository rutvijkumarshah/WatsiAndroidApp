package codepath.watsiapp.models;

import java.util.Date;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("NewsItem")
public class NewsItem extends ParseObject implements FeedItem {
	
	private static final String EMPTY_STRING="".intern();
	
	
	public Patient getPatient() {
		 return (Patient) getParseObject("patient");
	}
	
	public String getCampaignContent() {
		String content = getString("campaignContent");
		if(content == null) {
			content = EMPTY_STRING;
		}
		return content;
	}
	
	public ItemType getItemType() {
		/* TODO : see if you can make enum constants match the column data for easier comparison */
		String type = getString("type");
		if (type.compareTo("campaign_message") == 0 ) {
			return ItemType.CAMPAIGN_CONTENT;
		} else if (type.compareTo("donation_raised") == 0) {
			return ItemType.DONATION_RAISED;
		} else if (type.compareTo("fully_funded") == 0) {
			return ItemType.FULLY_FUNDED;
		} else {
			return ItemType.ON_BOARDED;
		}
	}
	
	public Date getCreatedAt() {
		return getDate("createdAt");
	}
	
	public Donation getDonation() {
		return (Donation) getParseObject("donation");
	}

}
