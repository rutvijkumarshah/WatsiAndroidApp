package codepath.watsiapp.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import codepath.watsiapp.R;

public class PaymentAmountFragment extends DialogFragment {
	private EditText mEditText;

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
		View view = inflater.inflate(R.layout.fragment_payment_amount, container);
//		mEditText = (EditText) view.findViewById(R.id.txt_your_name);
		String title = getArguments().getString("title", "Enter Name");
		getDialog().setTitle(title);
		// Show soft keyboard automatically
//		mEditText.requestFocus();
		getDialog().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
		getDialog().getWindow().setBackgroundDrawableResource(R.drawable.bg_card);
		return view;
	}
}
