package codepath.watsiapp.fragments;

import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.test.PerformanceTestCase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import codepath.watsiapp.R;
import codepath.watsiapp.activities.DonationInfoStorage;
import codepath.watsiapp.utils.PrefsHelper;
import codepath.watsiapp.utils.Util;

public class PaymentAmountFragment extends DialogFragment {
	private EditText mFullNameText;
	private EditText mEmailAddressText;
	private EditText mDonationAmount;
	private Button   mDonateBtn;
	private CheckBox mIsAnonymousDonation;
	private PrefsHelper prefs;
	private String patientId;
	private String donateTo;
	
	private static final String TITLE="EXTRAS_DONATION_DIALOG_TITLE";
	private static final String PATIENT_ID="EXTRAS_DONATION_DIALOG_PATIENTID";
	private static final String DONATE_TO="EXTRAS_DONATION_DIALOG_DONATE_TO";
	public PaymentAmountFragment() {
		// Empty constructor required for DialogFragment
	}
	
	public static PaymentAmountFragment newInstance(String title,String patientId,String donateTo) {
		PaymentAmountFragment frag = new PaymentAmountFragment();
		Bundle args = new Bundle();
		args.putString(TITLE, title);
		args.putString(PATIENT_ID, patientId);
		args.putString(DONATE_TO, donateTo);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Activity hostActivity=getActivity();
		prefs=new PrefsHelper(hostActivity);
		
		View view = inflater.inflate(R.layout.fragment_payment_amount, container);
//		mEditText = (EditText) view.findViewById(R.id.txt_your_name);
		String title = getArguments().getString(TITLE, "Enter Name");
		patientId=getArguments().getString(PATIENT_ID, null);
		donateTo=getArguments().getString(DONATE_TO,null);
		
		getDialog().setTitle(title);
		initUI(view);
		// Show soft keyboard automatically
//		mEditText.requestFocus();
		getDialog().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
		getDialog().getWindow().setBackgroundDrawableResource(R.drawable.bg_card);
		
		
		return view;
	}
	private void initUI(View v) {
		//initialize UI elements
		mFullNameText=(EditText) v.findViewById(R.id.tvFullName);
		mEmailAddressText=(EditText) v.findViewById(R.id.tvEmail);
		mDonationAmount=(EditText)v.findViewById(R.id.tvAmountToDonate);
		mDonateBtn=(Button)v.findViewById(R.id.btnDonate);
		mIsAnonymousDonation=(CheckBox)v.findViewById(R.id.isAnonymousDonation);
		
		boolean isAnonymousUser=ParseUser.getCurrentUser() ==null;
		
		//populates values if available;
		mFullNameText.setText(getUserFullname());
		mEmailAddressText.setText(getUserEmailAddress());
		
		mFullNameText.setEnabled(isAnonymousUser);
		mEmailAddressText.setEnabled(isAnonymousUser);
		mDonateBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				donate();
				
			}
		});
		
		
	}
	private String getUserFullname() {
		String prefUserName = prefs.getUserFullName();
		if("".equals(prefUserName)) {
			ParseUser parseUser = ParseUser.getCurrentUser();
			if(parseUser!=null ) {
				prefUserName=parseUser.getString("name");
				prefs.setUserFullName(prefUserName);
			}
		}
		return prefUserName;
	}
	
	private String getUserEmailAddress() {
		String perfUserEmail = prefs.getUserEmail();
		if("".equals(perfUserEmail)) {
			ParseUser parseUser = ParseUser.getCurrentUser();
			if(parseUser!=null ) {
				perfUserEmail=parseUser.getEmail();
				prefs.setUserFullName(perfUserEmail);
			}
		}
		return perfUserEmail;
	}
	
	private void donate() {
		String strDonationAmount=mDonationAmount.getText().toString();
		
		DonationInfoStorage donationInfo=(DonationInfoStorage) getActivity();
		donationInfo.setDonationAmount(Float.valueOf(strDonationAmount));
		donationInfo.setPatientId(patientId);
		donationInfo.setUserEmailAddress(mEmailAddressText.getText().toString());
		donationInfo.setUserFullName(mFullNameText.getText().toString());
		donationInfo.setAnonymousDonation(mIsAnonymousDonation.isChecked());
		Intent intent=Util.getFundTreatmentIntent(getActivity(), donationInfo,donateTo);
		startActivityForResult(intent, 0);
		this.dismiss();
	}
}
