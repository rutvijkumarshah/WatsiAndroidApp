package codepath.watsiapp.models;


public interface FeedItem {
	
	/**
	 * Describes the type of feed item
	 */
	public enum ItemType {
		CAMPAIGN_CONTENT,
		DONATION_RAISED,
		FULLY_FUNDED,
		ON_BOARDED,
	}

	public ItemType getItemType();
}
