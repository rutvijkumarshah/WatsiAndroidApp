/***

The MIT License (MIT)
Copyright © 2014 Rutvijkumar Shah
 
Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the “Software”), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
 
The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.
 
THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 ***/

package codepath.watsiapp.adapters;

import static codepath.watsiapp.utils.Util.getPixels;

import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import codepath.watsiapp.ParseHelper;
import codepath.watsiapp.R;
import codepath.watsiapp.activities.PatientDetailActivity;
import codepath.watsiapp.models.Donation;
import codepath.watsiapp.models.Patient;
import codepath.watsiapp.utils.Util;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class DonationAdapter extends ParseQueryAdapter<Donation> {

	private static final String TAG = "DONATION_ADAPTER";
	private FragmentActivity activity;
	public static final int PAGE_SIZE = 100;

	private ViewHolder viewHolder;

	public DonationAdapter(Context context,
			ParseQueryAdapter.QueryFactory<Donation> queryFactory) {
		// Custom Query
		super(context, queryFactory);
		activity = (FragmentActivity) context;
		this.setObjectsPerPage(PAGE_SIZE);
	}

	public DonationAdapter(final Context context, final String donorId) {

		// load all Donations
		// if required this is the place to apply where filters on Donations
		// list
		this(context, new ParseQueryAdapter.QueryFactory<Donation>() {
			public ParseQuery create() {
				return new ParseHelper(context).getDonationsByDonorId(donorId);
			}
		});
	}

	@Override
	public View getItemView(final Donation Donation, View convertView,
			ViewGroup parent) {
		if (convertView == null) {
			viewHolder = new ViewHolder();

			convertView = buildViewHolder();
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		setupUI(Donation,viewHolder);
		return convertView;
	}

	private void setupUI(final Donation donation,final ViewHolder vh) {

        final DisplayImageOptions options = new DisplayImageOptions.Builder()
        	.displayer(new RoundedBitmapDisplayer((int) (getPixels(activity,60)/2)))
        	.cacheInMemory()
        	.cacheOnDisc()
        	.imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
        	.build();

		
		// Add and download the image
		// Donation photo
		
		final ImageLoader imageLoader = ImageLoader.getInstance();
		donation.getPatient(new GetCallback<Patient>() {

			@Override
			public void done(final Patient patient, ParseException exp) {
				// TODO Auto-generated method stub
				if (exp == null) {
					imageLoader.displayImage(patient.getPhotoUrl(),
							vh.patientPhoto,options);

					// name
					vh.patientName.setText(patient.getFullName());

					vh.patientId = patient.getObjectId();

				}

			}
		});

		vh.donationAmount.setText(String.valueOf("$ "+donation
				.getDonationAmount()));
		
		Date dt=donation.getUpdatedAt();
		vh.donationDate.setText(Util.getFormatedDate(dt));

	}

	private static class ViewHolder {
		TextView patientName; // android:id="@+id/name"
		ImageView patientPhoto; // android:id="@+id/progressBarImageView"
		TextView donationAmount; // android:id="@+id/donationAmount"
		String patientId;
		TextView donationDate; // android:id="@+id/donation_date"
	}
	
	
	private View buildViewHolder() {
		View convertView;
		convertView = View.inflate(getContext(), R.layout.item_donation, null);
		viewHolder.patientName = (TextView) convertView.findViewById(R.id.name);
		viewHolder.patientPhoto = (ImageView) convertView
				.findViewById(R.id.progressBarImageView);

		viewHolder.donationAmount = (TextView) convertView.findViewById(R.id.donationAmount);
		viewHolder.donationDate = (TextView) convertView.findViewById(R.id.donation_date);
		convertView.setTag(viewHolder);
		
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ViewHolder _viewHolder = (ViewHolder) v.getTag();
				String patientId = _viewHolder.patientId;
				PatientDetailActivity.getPatientDetailsIntent(activity, patientId);
			}
		});
		return convertView;
	}

}