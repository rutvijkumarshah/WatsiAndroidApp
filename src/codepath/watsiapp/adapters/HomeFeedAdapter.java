package codepath.watsiapp.adapters;

import static codepath.watsiapp.utils.Util.applyPrimaryFont;
import static codepath.watsiapp.utils.Util.startShareIntent;
import static codepath.watsiapp.utils.Util.startShareIntentWithFaceBook;
import static codepath.watsiapp.utils.Util.startShareIntentWithTwitter;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import codepath.watsiapp.ParseHelper;
import codepath.watsiapp.R;
import codepath.watsiapp.activities.PatientDetailActivity;
import codepath.watsiapp.activities.WatsiMainActivity;
import codepath.watsiapp.models.Donation;
import codepath.watsiapp.models.Donor;
import codepath.watsiapp.models.FeedItem;
import codepath.watsiapp.models.FeedItem.ItemType;
import codepath.watsiapp.models.NewsItem;
import codepath.watsiapp.models.Patient;
import codepath.watsiapp.utils.Util;
import codepath.watsiapp.utils.Util.ShareableItem;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class HomeFeedAdapter extends ParseQueryAdapter<NewsItem> {

	private WatsiMainActivity activity;
	public static final int PAGE_SIZE = 20; 

	private static final String TAG = "HOME_FEED_ADAPTER";



	// View lookup cache
	private static class ViewHolder {
		//TextView name;
		TextView shortDescription;
		TextView message;
		ImageView profileImage;
		ProgressBar donationProgress;
		ImageView shareOnTwitter;
		ImageView shareOnFacebook;
		ImageView donateView;
		ImageView shareAction;
		Patient patient;	
		ItemType itemType; 
		ShareableItem shareableItem;
	}
	private ViewHolder viewHolder;

	public HomeFeedAdapter(Context context,ParseQueryAdapter.QueryFactory<NewsItem> queryFactory) {
		//Custom Query
		super(context,queryFactory); 
		activity = (WatsiMainActivity) context;
		this.setObjectsPerPage(PAGE_SIZE);
		
	}

	public HomeFeedAdapter(final Context context) {

		// load all patients
		// if required this is the place to apply where filters on patients list
		this(context, new ParseQueryAdapter.QueryFactory<NewsItem>() {
			@SuppressWarnings({ "rawtypes", "unchecked" })
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
	public View getItemView(final NewsItem newsItem, View convertView,ViewGroup parent) {

		
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = buildViewHolder(newsItem.getItemType());
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.profileImage.setImageResource(R.drawable.default_feed_icon);
			viewHolder.patient = null;
			viewHolder.itemType = newsItem.getItemType();
		}

		switch(newsItem.getItemType()) {
		case CAMPAIGN_CONTENT : 
			convertView = getCampaignContentItemView(newsItem, convertView, parent);
			viewHolder.donationProgress.setVisibility(View.INVISIBLE);
			break;
		case ON_BOARDED:
			convertView = getOnBoardedItemView(newsItem, convertView, parent);
			viewHolder.donationProgress.setVisibility(View.VISIBLE);
			break;
		case FULLY_FUNDED:
			convertView = getFullyFundedItemView(newsItem, convertView, parent);
			viewHolder.donationProgress.setVisibility(View.VISIBLE);
			break;
		case DONATION_RAISED:
			convertView = getDonationRaisedItemView(newsItem, convertView, parent);
			viewHolder.donationProgress.setVisibility(View.VISIBLE);
			break; 
		}
		
			
			
		return convertView;	
	}

	private View buildViewHolder(ItemType itemType) {
		View convertView;
		convertView = View.inflate(getContext(), R.layout.item_patient_news, null);

		viewHolder.shortDescription = (TextView) convertView
				.findViewById(R.id.tvShortDescription);

		viewHolder.message = (TextView) convertView
				.findViewById(R.id.tvMessage);

		viewHolder.profileImage = (ImageView) convertView
				.findViewById(R.id.ivProfileImage);

		viewHolder.donationProgress = (ProgressBar) convertView
				.findViewById(R.id.progressBarToday);
  
		viewHolder.shareAction = (ImageView) convertView
				.findViewById(R.id.shareIv);

		viewHolder.shareOnFacebook = (ImageView) convertView
				.findViewById(R.id.share_fb);
		viewHolder.donateView = (ImageView) convertView
				.findViewById(R.id.fund_treatment_feed);
		viewHolder.shareOnTwitter = (ImageView) convertView
				.findViewById(R.id.share_tw);

		viewHolder.itemType = itemType;

		applyPrimaryFont(getContext(), viewHolder.message);
		applyPrimaryFont(getContext(), viewHolder.shortDescription);
		convertView.setTag(viewHolder);

		return convertView;
	}

	private View getCampaignContentItemView(NewsItem newsItem, View convertView, ViewGroup parent) {
		ViewHolder viewHolder =(ViewHolder) convertView.getTag();
		viewHolder.shortDescription.setText(R.string.makeDifference);
		viewHolder.message.setText(newsItem.getCampaignContent());
		viewHolder.shareableItem = Util.getUniversalShareableItem();


		// no donation progress bar
		viewHolder.donationProgress.setVisibility(View.INVISIBLE);
		viewHolder.donateView.setVisibility(View.VISIBLE);


		
		viewHolder.donateView.setOnClickListener(null);
		convertView.setTag(viewHolder);
		setShareListeners(viewHolder);
		setPatientFundButton(viewHolder.donateView, viewHolder.shareableItem);
		
		viewHolder.message.setMaxLines(20); 
		viewHolder.message.setEllipsize(TextUtils.TruncateAt.END);
		return convertView;
	}


	private void setShareListeners(ViewHolder viewHolder) {
		viewHolder.shareAction.setTag(viewHolder.shareableItem);
		viewHolder.shareAction.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startShareIntent(activity, (ShareableItem)v.getTag());

			}
		});
		viewHolder.shareOnTwitter.setTag(viewHolder.shareableItem);
		viewHolder.shareOnTwitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startShareIntentWithTwitter(activity,(ShareableItem)v.getTag());

			}
		});

		viewHolder.shareOnFacebook.setTag(viewHolder.shareableItem);
		viewHolder.shareOnFacebook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startShareIntentWithFaceBook(activity,(ShareableItem)v.getTag());

			}
		});

	}


	private View getDonationRaisedItemView(NewsItem newsItem, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = (ViewHolder)convertView.getTag();
		Patient patient;
		Donor donor;
		Donation dn;
		try {
			dn = newsItem.getDonation();
			donor = dn.getDonor();
			patient = newsItem.getPatient();
		} catch (Exception e) {
			Log.e(TAG, "Exxception while getting donation details "+e,e);
			return convertView;
		}

		String shortDescription = patient.getFirstName() + " was helped !";
		String donorNameToUse = donor.getFirstName();
		if (dn.getIsAnonymous()) {
			// remove the donor name and call him 'A generous donor'
			donorNameToUse = "A generous donor";
		}
		String message = donorNameToUse + " helped " + patient.getFullName() + 
				" by donating " + Util.formatAmount(dn.getDonationAmount()) + ". Now its your turn!"; 

		viewHolder.patient = patient;
		viewHolder.shareableItem = patient;
		setupUI(convertView, viewHolder, patient.getPhotoUrl(), shortDescription, message);
		convertView.setTag(viewHolder);
		return convertView;

	}

	private View getFullyFundedItemView(NewsItem newsItem, View convertView, ViewGroup parent) {
		ItemType itemType=ItemType.FULLY_FUNDED;
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		Patient patient;
		try {
			patient = newsItem.getPatient().fetchIfNeeded();
		} catch (ParseException e) {
			Log.e(TAG, "Exxception while getting patient details "+e,e);;
			return convertView;
		}

		String shortDescription = patient.getFirstName() + " fully funded!!";
		String message = patient.getFullName() + " will now be able to get medical" +
				" treatment. Donors like you helped raise " +
				Util.formatAmount(patient.getDonationReceived()) +". Big Thank You to all the donors !!"; 

		viewHolder.patient = patient;
		viewHolder.shareableItem = patient;
		setupUI(convertView, viewHolder, patient.getPhotoUrl(), shortDescription, message);
		convertView.setTag(viewHolder);
		return convertView;
	}

	private View getOnBoardedItemView(NewsItem newsItem, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		Patient patient;
		try {
			patient = newsItem.getPatient().fetchIfNeeded();
		} catch (ParseException e) {
			Log.e(TAG, "Exxception while getting donation details "+e,e);
			return convertView;
		}

		String shortDescription = patient.getFirstName() + " is looking for help!";
		String message = patient.getMedicalNeed()  + ". You can help!"; 


		viewHolder.patient = patient;
		viewHolder.shareableItem = patient;
		setupUI(convertView, viewHolder, patient.getPhotoUrl(), shortDescription, message);
		convertView.setTag(viewHolder);
		return convertView;
	}

	private void setupUI(View convertView, ViewHolder viewHolder, String photoUrl, String shortDescription, String message){
//		DisplayImageOptions options = new DisplayImageOptions.Builder()
//		.displayer(new RoundedBitmapDisplayer((int) (getPixels(activity,80)/2)))
//		.cacheInMemory()
//		.cacheOnDisc()
//		.imageScaleType(ImageScaleType.EXACTLY)
//		.bitmapConfig(Bitmap.Config.RGB_565)
//		.build();

		//keep it transparent till image loads
		// this will gives smooth effect while fast scroll other wise old image will display till new image loads
		viewHolder.profileImage.setImageResource(android.R.color.transparent);
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(photoUrl, viewHolder.profileImage);

		viewHolder.shortDescription.setText(shortDescription);
		
		if (viewHolder.itemType != ItemType.CAMPAIGN_CONTENT) {
			viewHolder.message.setMaxLines(3);
			viewHolder.message.setEllipsize(TextUtils.TruncateAt.END);
		} 
		
		viewHolder.message.setText(message);

		int donationProgressPecentage = viewHolder.patient.getDonationProgressPecentage();
		// donation progress
		viewHolder.donationProgress.setProgress(donationProgressPecentage);

		Drawable progressDrawable = null;

		if (viewHolder.patient.isFullyFunded()) {
			progressDrawable = getContext().getResources().getDrawable(
					R.drawable.fully_funded_progressbar);
			viewHolder.donateView.setVisibility(View.GONE);
		} else {
			progressDrawable = getContext().getResources().getDrawable(
					R.drawable.progressbar);
			viewHolder.donationProgress.setVisibility(View.VISIBLE);
			viewHolder.donateView.setVisibility(View.VISIBLE);
		}
   
		viewHolder.donationProgress.setProgressDrawable(progressDrawable);
		setPatientNavigation(convertView, viewHolder.patient, viewHolder.itemType);
		setPatientFundButton(viewHolder.donateView, viewHolder.patient);
		setShareListeners(viewHolder);
	}

	private void setPatientNavigation(View v, Patient p, ItemType type) {
		v.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Patient pt = (Patient) ((ViewHolder) v.getTag()).patient;
				if (pt == null) {
					return;
				}
				String patientId= ((ViewHolder) v.getTag()).patient.getObjectId();
				PatientDetailActivity.showPatientDetailsActivity(activity, patientId);
			}
		});
	}

	private void setPatientFundButton(ImageView imageView, ShareableItem p) {
		imageView.setTag(p);
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				ShareableItem item = (ShareableItem) v.getTag();
				if(item instanceof Patient) {
					 Patient patient=(Patient)item;
					 activity.showDonateDialog("Donate for "+patient.getFullName(),patient.getObjectId(),patient.getFullName());
				}else {
					activity.showDonateDialog("Universal Fund",null,"Watsi Universal Fund");
				}
			}
		});
	}

}
