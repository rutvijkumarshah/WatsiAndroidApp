package codepath.watsiapp.fragments;

import com.parse.ParseUser;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.test.PerformanceTestCase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import codepath.watsiapp.R;
import codepath.watsiapp.utils.PrefsHelper;

public class PaymentAmountFragment extends DialogFragment {
	private EditText mFullNameText;
	private EditText mEmailAddressText;
	private EditText mDonationAmount;
	private Button   mDonateBtn;
	private PrefsHelper prefs;
	
	public PaymentAmountFragment() {
		// Empty constructor required for DialogFragment
	}
	
	public static PaymentAmountFragment newInstance(String title) {
		PaymentAmountFragment frag = new PaymentAmountFragment();
		Bundle args = new Bundle();
		args.putString("title", title);
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
		String title = getArguments().getString("title", "Enter Name");
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
		
		boolean isAnonymousUser=ParseUser.getCurrentUser() ==null;
		
		//populates values if available;
		mFullNameText.setText(getUserFullname());
		mEmailAddressText.setText(getUserEmailAddress());
		
		mFullNameText.setEnabled(isAnonymousUser);
		mEmailAddressText.setEnabled(isAnonymousUser);
		
		
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
}
