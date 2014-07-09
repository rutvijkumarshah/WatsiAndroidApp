package codepath.watsiapp.models;

import android.view.View;

public interface FeedItem {
	
	/**
	 * Describes the type of feed item
	 */
	public enum ItemType {
		PATIENT,
		DONOR,
	}
	
	/**
	 * Method to inflate this item
	 */
	public View getInflatedLayoutForType();

	public ItemType getItemType();
}
