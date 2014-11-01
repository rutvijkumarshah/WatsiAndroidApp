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

package codepath.watsiapp.adaptersv2;

import static codepath.watsiapp.utils.Util.applyPrimaryFont;
import static codepath.watsiapp.utils.Util.formatAmount;
import static codepath.watsiapp.utils.Util.getFormatedDate;
import static codepath.watsiapp.utils.Util.getPixels;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import codepath.watsiapp.R;
import codepath.watsiapp.activities.PatientDetailActivity;
import codepath.watsiapp.activities.WatsiMainActivity;
import codepath.watsiapp.modelsv2.Donation;
import codepath.watsiapp.modelsv2.Patient;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class DonationAdapter extends ArrayAdapter<Donation> {

	//private static final String TAG = "DONATION_ADAPTER";
	private FragmentActivity activity;
	public static final int PAGE_SIZE = 100;

	private ViewHolder viewHolder;
	private ArrayList<Donation> donations;
	
	public DonationAdapter(Context context,ArrayList<Donation> donations) {
		//Custom Query
		super(context,R.layout.item_patient_news,donations); 
		activity = (WatsiMainActivity) context;
		this.donations=donations;
		
	}



	@Override
	public View getView(int position, View convertView,
			ViewGroup parent) {
		final Donation Donation=getItem(position);
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
		Patient patient=donation.getPatient();
		imageLoader.displayImage(patient.getPhotoUrl(),
				vh.patientPhoto,options);

		// name
		vh.patientName.setText(patient.getFullName());
		vh.patientId = patient.getObjectId();


		vh.donationAmount.setText(formatAmount(donation.getDonationAmount()));
		vh.donationDate.setText(getFormatedDate(donation.getDonationDate()));

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
				PatientDetailActivity.showPatientDetailsActivity(activity, patientId);
			}
		});
		
		
		applyPrimaryFont(activity, viewHolder.patientName);
		applyPrimaryFont(activity, viewHolder.donationAmount);
		applyPrimaryFont(activity, viewHolder.donationDate);
	
		return convertView;
	}

}