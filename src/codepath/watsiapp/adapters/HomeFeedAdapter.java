package codepath.watsiapp.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import codepath.watsiapp.ParseHelper;
import codepath.watsiapp.R;
import codepath.watsiapp.activities.PatientDetailActivity;
import codepath.watsiapp.models.Donation;
import codepath.watsiapp.models.Donor;
import codepath.watsiapp.models.FeedItem;
import codepath.watsiapp.models.NewsItem;
import codepath.watsiapp.models.Patient;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class HomeFeedAdapter extends ParseQueryAdapter<NewsItem> {

	private FragmentActivity activity;
	public static final int PAGE_SIZE = 4; 

	public HomeFeedAdapter(Context context,ParseQueryAdapter.QueryFactory<NewsItem> queryFactory) {
		//Custom Query
		super(context,queryFactory); 
		activity = (FragmentActivity) context;
		this.setObjectsPerPage(PAGE_SIZE);
	}

	public HomeFeedAdapter(final Context context) {

		// load all patients
		// if required this is the place to apply where filters on patients list
		this(context, new ParseQueryAdapter.QueryFactory<NewsItem>() {
			public ParseQuery create() {
				return new ParseHelper(context).getAllNewsFeedItem();
			}
		});
	}


	@Override
	public int getViewTypeCount() {
		return FeedItem.ItemType.values().length;
	}

	@Override
	public View getItemView(final NewsItem newsItem, View convertView,
			ViewGroup parent) {
		View view = null;
		switch(newsItem.getItemType()) {
		case CAMPAIGN_CONTENT : 
			view = getCampaignContentItemView(newsItem, convertView, parent);
			break;
		case ON_BOARDED:
			view = getOnBoardedItemView(newsItem, convertView, parent);
			break;
		case FULLY_FUNDED:
			view = getFullyFundedItemView(newsItem, convertView, parent);
			break;
		case DONATION_RAISED:
			view = getDonationRaisedItemView(newsItem, convertView, parent);
			break; 
		}
		return view;
	}

	private View getCampaignContentItemView(NewsItem newsItem, View convertView, ViewGroup parent) {
		if (convertView == null ) {
			convertView = View.inflate(getContext(),R.layout.item_campaign_news, null);
		}
		
		TextView tvHeading = (TextView) convertView.findViewById(R.id.tvCNHeading);
		tvHeading.setText("Make a difference !!");
		
		TextView tvShortMessage = (TextView) convertView.findViewById(R.id.tvCNShortMessage);
		tvShortMessage.setText(newsItem.getCampaignContent());
		return convertView;
	}

	private View getDonationRaisedItemView(NewsItem newsItem, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(getContext(), R.layout.item_patient_news, null);
		}
		
		Patient patient;
		Donor donor;
		Donation dn;
		try {
			dn = newsItem.getDonation().fetchIfNeeded();
			donor = dn.getDonor().fetchIfNeeded();
			patient = dn.getPatient().fetchIfNeeded();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return convertView;
		}
		
		ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader
				.displayImage(patient.getPhotoUrl(), ivProfileImage);

		TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
		tvUserName.setText(patient.getFirstName() + " was helped by " + donor.getFirstName());
		
		TextView tvShortMessage = (TextView) convertView.findViewById(R.id.tvShortMessage);
		String message = donor.getFirstName() + " helped " + patient.getFullName() + 
				         " by donating $" + dn.getDonationAmount() + ". Now its your turn!"; 
		tvShortMessage.setText(message);
		
		Button bt = (Button) convertView.findViewById(R.id.btNewsAction);
		bt.setText("Donate");
		
		setPatientNavigation(convertView, patient);
		
		return convertView;
		
	}

	private View getFullyFundedItemView(NewsItem newsItem, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(getContext(), R.layout.item_patient_news, null);
		}
		
		Patient patient;
		try {
			patient = newsItem.getPatient().fetchIfNeeded();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return convertView;
		}
		
		ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader
				.displayImage(patient.getPhotoUrl(), ivProfileImage);

		TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
		tvUserName.setText(patient.getFirstName() + " is now fully funded!!");
		
		TextView tvShortMessage = (TextView) convertView.findViewById(R.id.tvShortMessage);
		String message = patient.getFullName() + " will now be able to get his medical" +
		                 " treatment. Donors like you helped him raise $" +
		                 patient.getDonationReceived() +". Big Thank You to all the donors !!"; 
		tvShortMessage.setText(message);
		
		Button bt = (Button) convertView.findViewById(R.id.btNewsAction);
		bt.setText("Give Generic Donation!");
		setPatientNavigation(convertView, patient);
		return convertView;
	}

	private View getOnBoardedItemView(NewsItem newsItem, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(getContext(), R.layout.item_patient_news, null);
		}
		
		Patient patient;
		try {
			patient = newsItem.getPatient().fetchIfNeeded();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return convertView;
		}
		ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader
				.displayImage(patient.getPhotoUrl(), ivProfileImage);

		TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
		tvUserName.setText(patient.getFirstName() + " is looking for help!");
		
		TextView tvShortMessage = (TextView) convertView.findViewById(R.id.tvShortMessage);
		String message = patient.getMedicalNeed()  + ". You can help!"; 
		tvShortMessage.setText(message);
		
		Button bt = (Button) convertView.findViewById(R.id.btNewsAction);
		bt.setText("Help " + patient.getFirstName() + "!");
		
		setPatientNavigation(convertView, patient);
		return convertView;
	}


	private void setPatientNavigation(View v, Patient p) {
		v.setTag(p.getObjectId());
		v.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String patientId= (String) v.getTag();
				PatientDetailActivity.getPatientDetailsIntent(activity, patientId);
			}
		});
	}
}
