package codepath.watsiapp.modelsv2;


public interface FeedItem {
	
	/**
	 * Describes the type of feed item
	 */
	public enum ItemType {
		@Deprecated
		CAMPAIGN_CONTENT,
		DONATION_RAISED,
		FULLY_FUNDED,
		ON_BOARDED,
		CAMPAIGN_MESSAGE
	}

	public ItemType getItemType();
}
