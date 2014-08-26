package codepath.watsiapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import codepath.watsiapp.R;
import codepath.watsiapp.activities.DonationInfoStorage;
import codepath.watsiapp.customview.PrefixedEditText;
import codepath.watsiapp.utils.PrefsHelper;
import codepath.watsiapp.utils.Util;
import static codepath.watsiapp.utils.Util.*;

import com.parse.ParseUser;

public class PaymentAmountFragment extends DialogFragment {
	private EditText mFullNameText;
	private EditText mEmailAddressText;
	private PrefixedEditText mDonationAmount;
	private TextView tvErrMsgEMail;
	private TextView tvErrMsgAmount;
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
		mDonationAmount.setPrefix("$");
		mDonationAmount.setPrefixTextColor(getResources().getColor(R.color.dialog_hint_color));
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
		mDonationAmount=(PrefixedEditText)v.findViewById(R.id.tvAmountToDonate);
		mDonateBtn=(Button)v.findViewById(R.id.btnDonate);
		mIsAnonymousDonation=(CheckBox)v.findViewById(R.id.isAnonymousDonation);
		tvErrMsgAmount = (TextView) v.findViewById(R.id.tvErrMsgAmount);
		tvErrMsgEMail = (TextView) v.findViewById(R.id.tvErrMsgEmail);
		boolean isAnonymousUser=ParseUser.getCurrentUser() ==null;
		
		FragmentActivity activity = getActivity();
		
		applyPrimaryFont(activity, mFullNameText);
		applyPrimaryFont(activity, mEmailAddressText);
		applyPrimaryFont(activity, mDonationAmount);
		applyPrimaryFont(activity, mDonateBtn);
		applyPrimaryFont(activity, mIsAnonymousDonation);
		applyPrimaryFont(activity, tvErrMsgAmount);
		applyPrimaryFont(activity, tvErrMsgEMail);
		
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
		
		mDonationAmount.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				Float amount = (float) 0;
				if(!hasFocus){
					if(!mDonationAmount.getText().toString().equalsIgnoreCase("")){
						amount = Float.parseFloat(mDonationAmount.getText().toString());
						if(amount < 5){
							tvErrMsgAmount.setVisibility(View.VISIBLE);
						}else if(tvErrMsgAmount.getVisibility() == View.VISIBLE){
							tvErrMsgAmount.setVisibility(View.GONE);
						}
					}
				}
			}
		});
		
		mEmailAddressText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					if(Util.isValidEmail(mEmailAddressText.getText().toString())== false){
						tvErrMsgEMail.setVisibility(View.VISIBLE);
					}else if(tvErrMsgEMail.getVisibility() == View.VISIBLE){
						tvErrMsgEMail.setVisibility(View.GONE);
					}
				}
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
	
	public class StoreDonationInfoTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			return null;
		}
		
	}
	private void donate() {
		this.dismiss();
		String strDonationAmount=mDonationAmount.getText().toString();
		DonationInfoStorage donationInfo=(DonationInfoStorage) getActivity();
//		donationInfo.setDonationAmount(Float.valueOf(strDonationAmount));
//		donationInfo.setPatientId(patientId);
//		donationInfo.setUserEmailAddress(mEmailAddressText.getText().toString());
//		donationInfo.setUserFullName(mFullNameText.getText().toString());
//		donationInfo.setAnonymousDonation(mIsAnonymousDonation.isChecked());
//		
		donationInfo.set(Float.valueOf(strDonationAmount), 
				patientId, mIsAnonymousDonation.isChecked(), 
				mFullNameText.getText().toString(), 
				mEmailAddressText.getText().toString());
		Intent intent=Util.getFundTreatmentIntent(getActivity(), donationInfo,donateTo);
		startActivityForResult(intent, 0);
		
	}
}
