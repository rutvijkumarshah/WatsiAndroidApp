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

import android.content.Context;
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
import codepath.watsiapp.models.Patient;
import codepath.watsiapp.utils.Util;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class PatientAdapter extends ParseQueryAdapter<Patient> {

	private static final String TAG = "PATIENT_ADAPTER";
	private FragmentActivity activity;
	/***
	 */
	/**
	 * Trade-off here. 
	 * 
	 * Keeping lower number improves startup time specially loading images and animation.
	 * But will increase no of requests app makes.
	 * 
	 */
	public static final int PAGE_SIZE=20; 

	// View lookup cache
	private static class ViewHolder {
		TextView name;
		TextView age;
		TextView location;
		TextView percentageFunded;
		TextView donationTogo;
		TextView medicalNeed;
		ImageView patientPhoto;
		ProgressBar donationProgress;
		Button donateBtn;
		ImageView shareAction;
		String patientId;
		// DonateShareFragment donanteAndShareFragment;
		
	}
	
	private ViewHolder viewHolder;
	
	public PatientAdapter(Context context,ParseQueryAdapter.QueryFactory<Patient> queryFactory) {
		//Custom Query
		super(context,queryFactory); 
		activity = (FragmentActivity) context;
		this.setObjectsPerPage(PAGE_SIZE);
	}
	
	public PatientAdapter(final Context context) {
		
		// load all patients
		// if required this is the place to apply where filters on patients list
		this(context, new ParseQueryAdapter.QueryFactory<Patient>() {
			public ParseQuery create() {
				return new ParseHelper(context).getAllPatientsQuery();
			}
		});
	}

	@Override
	public View getItemView(final Patient patient, View convertView,
			ViewGroup parent) {
		if (convertView == null) {
			viewHolder = new ViewHolder();

			convertView = buildViewHolder();
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		setupUI(patient);
		return convertView;
	}

	private void setupUI(final Patient patient) {  
		
		 // Add and download the image
		// patient photo
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader
				.displayImage(patient.getPhotoUrl(), viewHolder.patientPhoto);

		int donationProgressPecentage = patient.getDonationProgressPecentage();
		// donation progress
		viewHolder.donationProgress.setProgress(donationProgressPecentage);

		// name
		viewHolder.name.setText(patient.getFullName());
		

		// age
		viewHolder.age.setText(patient.getAge() + " Years Old"); // TODO will be
																	// externalized
																	// String as
																	// template
																	// string

		// location
		viewHolder.location.setText(patient.getCountry());

		// percentageFunded
		viewHolder.percentageFunded.setText(donationProgressPecentage
				+ " % Funded"); // TODO will be externalized String as template
								// string

		// donationTOGO
		viewHolder.donationTogo.setText("$ " + patient.getDonationToGo()
				+ " to go");// TODO will be externalized String as template
							// string

		// medical need
		viewHolder.medicalNeed.setText(patient.getMedicalNeed());

		Drawable progressDrawable = null;

		if (patient.isFullyFunded()) {
			progressDrawable = getContext().getResources().getDrawable(
					R.drawable.fully_funded_progressbar);

		} else {
			progressDrawable = getContext().getResources().getDrawable(
					R.drawable.progressbar);
		}

		viewHolder.donationProgress.setProgressDrawable(progressDrawable);
		// viewHolder.donanteAndShareFragment=setDonateAndShareFragment(patient);

		viewHolder.donateBtn.setTag(patient);
		viewHolder.shareAction.setTag(patient);
		viewHolder.shareAction.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Util.startShareIntent(activity,(Patient)v.getTag());

			}
		});

		viewHolder.donateBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Util.startFundTreatmentIntent(activity,(Patient)v.getTag());
			}
		});
		
		viewHolder.patientId=patient.getObjectId();

	}

	private View buildViewHolder() {
		View convertView;
		convertView = View.inflate(getContext(), R.layout.item_patient,
				null);
		viewHolder.name = (TextView) convertView.findViewById(R.id.name);
		viewHolder.age = (TextView) convertView.findViewById(R.id.age);
		viewHolder.location = (TextView) convertView
				.findViewById(R.id.location);
		viewHolder.percentageFunded = (TextView) convertView
				.findViewById(R.id.percent_funded);

		viewHolder.donationTogo = (TextView) convertView
				.findViewById(R.id.donation_togo);

		viewHolder.medicalNeed = (TextView) convertView
				.findViewById(R.id.medicalNeeds);

		viewHolder.patientPhoto = (ImageView) convertView
				.findViewById(R.id.progressBarImageView);

		viewHolder.donationProgress = (ProgressBar) convertView
				.findViewById(R.id.progressBarToday);

		viewHolder.donateBtn = (Button) convertView
				.findViewById(R.id.donateBtn);
		viewHolder.shareAction = (ImageView) convertView
				.findViewById(R.id.shareIv);

		Util.applyPrimaryFont(getContext(), viewHolder.name);
		
		convertView.setTag(viewHolder);
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				ViewHolder _viewHolder = (ViewHolder) v.getTag();
				String patientId=_viewHolder.patientId;
				PatientDetailActivity.getPatientDetailsIntent(activity, patientId);
				
			}
		});
		return convertView;
	}


}
