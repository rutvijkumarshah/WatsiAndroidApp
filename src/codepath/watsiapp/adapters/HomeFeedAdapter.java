package codepath.watsiapp.adapters;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import codepath.watsiapp.models.FeedItem;

public class HomeFeedAdapter extends ArrayAdapter<FeedItem> {

	public HomeFeedAdapter(Context context, List<FeedItem> feedItems) {
		super(context, 0, feedItems);
	}

	@Override
	public int getViewTypeCount() {
		return FeedItem.ItemType.values().length;
	}
	
	@Override
	public int getItemViewType(int position) {
		return getItem(position).getItemType().ordinal(); 
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			// Inflate XML layout based on the type     
			convertView = getItem(position).getInflatedLayoutForType();
		}
		
		return convertView;
	}
	
}
