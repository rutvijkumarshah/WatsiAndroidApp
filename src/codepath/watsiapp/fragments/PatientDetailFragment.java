package codepath.watsiapp.fragments;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import codepath.watsiapp.R;
import codepath.watsiapp.models.MedicalPartner;
import codepath.watsiapp.models.Patient;
import codepath.watsiapp.utils.Util;

public class PatientDetailFragment extends Fragment {
	private String patientId;
	private Patient patientObj;
	private TextView name;
	private TextView age;
	private TextView location;
	private TextView percentageFunded;
	private TextView donationToGo;
	private TextView medicalNeed;
	private ImageView patientPhoto;
	private ProgressBar donationProgress;
	private TextView medicalPartner;
	private TextView story;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		patientId = getArguments().getString("patient_id", "");
		// Get patient object based on id that we have got.
		patientObj = ParseObject.createWithoutData(Patient.class, patientId);
		try {
			patientObj = patientObj.fetchIfNeeded();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Defines the xml file for the fragment
		View view = inflater.inflate(R.layout.fragment_patient_detail,container, false);
		// Assign views to local variables
		name = (TextView) view.findViewById(R.id.tvName);
		age = (TextView) view.findViewById(R.id.tvAge);
		location = (TextView) view.findViewById(R.id.tvLocationVal);
		percentageFunded = (TextView) view.findViewById(R.id.tvDonationStatus);
		donationToGo = (TextView) view.findViewById(R.id.tvDonationTogo);
		medicalNeed = (TextView) view.findViewById(R.id.tvPatientNeed);
		patientPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
		donationProgress = (ProgressBar) view.findViewById(R.id.pbDonationProgress);
		medicalPartner = (TextView) view.findViewById(R.id.tvMedicalPartnerVal);
		story = (TextView) view.findViewById(R.id.tvDescription);
		// Set views to with values
		name.setText(patientObj.getFullName());
		age.setText(patientObj.getAge()+ " Years Old");
		location.setText(patientObj.getCountry());
		 int donationProgressPecentage = patientObj.getDonationProgressPecentage();
		percentageFunded.setText(donationProgressPecentage+ " % Funded");
		donationToGo.setText("$ " + patientObj.getDonationToGo()+ " to go");
		medicalNeed.setText(patientObj.getMedicalNeed());
		donationProgress.setProgress(donationProgressPecentage);
		
		patientObj.getMedicalPartner(new GetCallback<MedicalPartner>() {
			@Override
			public void done(final MedicalPartner object, ParseException arg1) {
				medicalPartner.setText(object.getName());
				medicalPartner.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Util.starShowMedicalPartnerIntent(getActivity(), object);
					}
				});
				Util.makeTextViewHyperlink(medicalPartner);
			}
		});
		ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
    	.cacheInMemory()
    	.cacheOnDisc()
    	.imageScaleType(ImageScaleType.EXACTLY)
        .bitmapConfig(Bitmap.Config.RGB_565)
    	.build();
		imageLoader.displayImage(patientObj.getPhotoUrl(), patientPhoto,options);
		story.setText(patientObj.getStory().replace("#$#$", "\n"));
		return view;
	}

	public static PatientDetailFragment newInstance(String patientId) {
		PatientDetailFragment fragmentObj = new PatientDetailFragment();
		Bundle args = new Bundle();
		args.putString("patient_id", patientId);
		fragmentObj.setArguments(args);
		return fragmentObj;
	}
}
