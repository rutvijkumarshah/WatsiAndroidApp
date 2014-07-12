package codepath.watsiapp.fragments;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.ParseException;
import com.parse.ParseObject;

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
//
//	// View lookup cache
//	private static class ViewHolder {
//		TextView name;
//		TextView age;
//		TextView location;
//		TextView percentageFunded;
//		TextView donationTogo;
//		TextView medicalNeed;
//		ImageView patientPhoto;
//		ProgressBar donationProgress;
//		Button donateBtn;
//		ImageView shareAction;
//		String patientId;
//	}
//
//	private ViewHolder viewHolder;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		viewHolder = new ViewHolder();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Defines the xml file for the fragment
		View view = inflater.inflate(R.layout.fragment_patient_detail,
				container, false);
		name = (TextView) view.findViewById(R.id.tvName);
		age = (TextView) view.findViewById(R.id.tvAge);
		location = (TextView) view.findViewById(R.id.tvLocationVal);
		percentageFunded = (TextView) view.findViewById(R.id.tvDonationStatus);
		donationToGo = (TextView) view.findViewById(R.id.tvDonationTogo);
		medicalNeed = (TextView) view.findViewById(R.id.tvPatientNeed);
		patientPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
		donationProgress = (ProgressBar) view.findViewById(R.id.pbDonationProgress);
		medicalPartner = (TextView) view.findViewById(R.id.tvMedicalPartner);
		story = (TextView) view.findViewById(R.id.tvDescription);
		
		name.setText(patientObj.getFullName());
		age.setText(patientObj.getAge()+ " Years Old");
		location.setText(patientObj.getCountry());
		 int donationProgressPecentage = patientObj.getDonationProgressPecentage();
		percentageFunded.setText(donationProgressPecentage+ " % Funded");
		donationToGo.setText("$ " + patientObj.getDonationToGo()+ " to go");
		medicalNeed.setText(patientObj.getMedicalNeed());
		donationProgress.setProgress(donationProgressPecentage);
		//TODO Make Medical Partner work.
		//		medicalPartner.setText(patientObj.getMedicalPartner().getName());
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(patientObj.getPhotoUrl(), patientPhoto);
		story.setText(patientObj.getStory());
		
//		viewHolder.name = (TextView) view.findViewById(R.id.tvName);
//		viewHolder.age = (TextView) view.findViewById(R.id.tvAge);
//		viewHolder.location = (TextView) view.findViewById(R.id.tvLocationVal);
//		 viewHolder.percentageFunded = (TextView) view
//		 .findViewById(R.id.tvDonationStatus);
//		
//		 viewHolder.donationTogo = (TextView) view
//		 .findViewById(R.id.tvDonationTogo);
//
//		viewHolder.medicalNeed = (TextView) view
//				.findViewById(R.id.tvPatientNeed);
//
//		viewHolder.patientPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
//
//		viewHolder.donationProgress = (ProgressBar) view
//				.findViewById(R.id.pbDonationProgress);
//		setupUI(patientObj);
		//
		// viewHolder.donateBtn = (Button) view
		// .findViewById(R.id.donateBtn);
		// viewHolder.shareAction = (ImageView) view
		// .findViewById(R.id.shareIv);
		// Setup handles to view objects here
		// etFoo = (EditText) v.findViewById(R.id.etFoo);
		return view;
	}

	public static PatientDetailFragment newInstance(String patientId) {
		PatientDetailFragment fragmentObj = new PatientDetailFragment();
		Bundle args = new Bundle();
		args.putString("patient_id", patientId);
		fragmentObj.setArguments(args);
		return fragmentObj;
	}

//	private void setupUI(final Patient patient) {
//
//		// Add and download the image
//		// patient photo
////		ImageLoader imageLoader = ImageLoader.getInstance();
////		imageLoader
////				.displayImage(patient.getPhotoUrl(), viewHolder.patientPhoto);
//
//		// int donationProgressPecentage =
//		// patient.getDonationProgressPecentage();
//		// // donation progress
//		// viewHolder.donationProgress.setProgress(donationProgressPecentage);
//		//
//		// // name
//		// viewHolder.name.setText(patient.getFullName());
//		//
//		// // age
//		// viewHolder.age.setText(patient.getAge() + " Years Old"); // TODO will
//		// be
//		// // externalized
//		// // String as
//		// // template
//		// // string
//		//
//		// // location
//		// viewHolder.location.setText(patient.getCountry());
//		//
//		// // percentageFunded
//		// viewHolder.percentageFunded.setText(donationProgressPecentage
//		// + " % Funded"); // TODO will be externalized String as template
//		// // string
//		//
//		// // donationTOGO
//		// viewHolder.donationTogo.setText("$ " + patient.getDonationToGo()
//		// + " to go");// TODO will be externalized String as template
//		// // string
//		//
//		// // medical need
//		// viewHolder.medicalNeed.setText(patient.getMedicalNeed());
//		//
//		// viewHolder.patientId = patient.getObjectId();
//
//	}

}
