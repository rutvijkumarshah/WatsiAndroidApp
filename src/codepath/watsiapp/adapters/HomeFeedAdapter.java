package codepath.watsiapp.adapters;

import static codepath.watsiapp.utils.Util.applyPrimaryFont;
import static codepath.watsiapp.utils.Util.getPixels;
import static codepath.watsiapp.utils.Util.startShareIntent;
import static codepath.watsiapp.utils.Util.startShareIntentWithFaceBook;
import static codepath.watsiapp.utils.Util.startShareIntentWithTwitter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import codepath.watsiapp.ParseHelper;
import codepath.watsiapp.R;
import codepath.watsiapp.activities.PatientDetailActivity;
import codepath.watsiapp.models.Donation;
import codepath.watsiapp.models.Donor;
import codepath.watsiapp.models.FeedItem;
import codepath.watsiapp.models.FeedItem.ItemType;
import codepath.watsiapp.models.NewsItem;
import codepath.watsiapp.models.Patient;
import codepath.watsiapp.utils.Util;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class HomeFeedAdapter extends ParseQueryAdapter<NewsItem> {

	private FragmentActivity activity;
	public static final int PAGE_SIZE = 4; 

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
	}
	private ViewHolder viewHolder;
	
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
			break;
		case ON_BOARDED:
			convertView = getOnBoardedItemView(newsItem, convertView, parent);
			break;
		case FULLY_FUNDED:
			convertView = getFullyFundedItemView(newsItem, convertView, parent);
			break;
		case DONATION_RAISED:
			convertView = getDonationRaisedItemView(newsItem, convertView, parent);
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
		
		// In case of campaign event we need not show progress bar.
		viewHolder.donationProgress.setVisibility(View.INVISIBLE);

		// setting click on donate button to go to generic donation.
//		setGeneralPatientFundButton(viewHolder.donateView);

		// in case of campaign share and donate not visible.
		convertView.findViewById(R.id.donateAndShare).setVisibility(View.INVISIBLE);
		convertView.setTag(viewHolder);
		
		return convertView;
	}


	private void setShareListeners(ViewHolder viewHolder) {
		viewHolder.shareAction.setTag(viewHolder.patient);
		viewHolder.shareAction.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startShareIntent(activity,(Patient)v.getTag());

			}
		});
		viewHolder.shareOnTwitter.setTag(viewHolder.patient);
		viewHolder.shareOnTwitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startShareIntentWithTwitter(activity,(Patient)v.getTag());

			}
		});

		viewHolder.shareOnFacebook.setTag(viewHolder.patient);
		viewHolder.shareOnFacebook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startShareIntentWithFaceBook(activity,(Patient)v.getTag());

			}
		});
		
	}

	private View getDonationRaisedItemView(NewsItem newsItem, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = (ViewHolder)convertView.getTag();
		Patient patient;
		Donor donor;
		Donation dn;
		try {
			dn = newsItem.getDonation().fetchIfNeeded();
			donor = dn.getDonor().fetchIfNeeded();
			patient = dn.getPatient().fetchIfNeeded();
		} catch (ParseException e) {
			e.printStackTrace();
			return convertView;
		}
		
		String shortDescription = patient.getFirstName() + " was helped !";
		String message = donor.getFirstName() + " helped " + patient.getFullName() + 
				         " by donating $" + dn.getDonationAmount() + ". Now its your turn!"; 
		
		viewHolder.patient = patient;
		setupUI(convertView, viewHolder, patient.getPhotoUrl(), shortDescription, message);
		convertView.findViewById(R.id.donateAndShare).setVisibility(View.VISIBLE);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			return convertView;
		}
		
		String shortDescription = patient.getFirstName() + " is now fully funded!!";
		String message = patient.getFullName() + " will now be able to get medical" +
		                 " treatment. Donors like you helped raise $" +
		                 patient.getDonationReceived() +". Big Thank You to all the donors !!"; 
		
		viewHolder.patient = patient;
		setupUI(convertView, viewHolder, patient.getPhotoUrl(), shortDescription, message);
		convertView.findViewById(R.id.donateAndShare).setVisibility(View.VISIBLE);
		convertView.setTag(viewHolder);
		return convertView;
	}

	private View getOnBoardedItemView(NewsItem newsItem, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		Patient patient;
		try {
			patient = newsItem.getPatient().fetchIfNeeded();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return convertView;
		}

		String shortDescription = patient.getFirstName() + " is looking for help!";
		String message = patient.getMedicalNeed()  + ". You can help!"; 

		
		viewHolder.patient = patient;
		setupUI(convertView, viewHolder, patient.getPhotoUrl(), shortDescription, message);
		convertView.findViewById(R.id.donateAndShare).setVisibility(View.VISIBLE);
		convertView.setTag(viewHolder);
		return convertView;
	}

	private void setupUI(View convertView, ViewHolder viewHolder, String photoUrl, String shortDescription, String message){
		 DisplayImageOptions options = new DisplayImageOptions.Builder()
     	.displayer(new RoundedBitmapDisplayer((int) (getPixels(activity,80)/2)))
     	.cacheInMemory()
     	.cacheOnDisc()
     	.imageScaleType(ImageScaleType.EXACTLY)
         .bitmapConfig(Bitmap.Config.RGB_565)
     	.build();

		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(photoUrl, viewHolder.profileImage,options);
		
		viewHolder.shortDescription.setText(shortDescription);
		viewHolder.message.setText(message);
		
		int donationProgressPecentage = viewHolder.patient.getDonationProgressPecentage();
		// donation progress
		viewHolder.donationProgress.setProgress(donationProgressPecentage);
		
		Drawable progressDrawable = null;

		if (viewHolder.patient.isFullyFunded()) {
			progressDrawable = getContext().getResources().getDrawable(
					R.drawable.fully_funded_progressbar);

		} else {
			progressDrawable = getContext().getResources().getDrawable(
					R.drawable.progressbar);
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
				String patientId= ((ViewHolder) v.getTag()).patient.getObjectId();
				PatientDetailActivity.getPatientDetailsIntent(activity, patientId);
			}
		});
	}
	
	private void setPatientFundButton(ImageView imageView, Patient p) {
		imageView.setTag(p);
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Patient patient = (Patient) v.getTag();
				Util.startFundTreatmentIntent(activity, patient);
			}
		});
	}
		
//	private void setGeneralPatientFundButton(ImageView imageView) {
//
//		imageView.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Util.startGeneralFundTreatmentIntent(activity);
//			}
//		});
//	}
	
}
