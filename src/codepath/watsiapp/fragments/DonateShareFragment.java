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

package codepath.watsiapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import codepath.watsiapp.R;
import codepath.watsiapp.models.Patient;

public class DonateShareFragment extends Fragment {
	private ImageView shareIv;
	private Button fundTreatmentBtn;
	private String patientId;
	private String patientProfileUrl;

	private static String FRAGMENT_KEY_PATIENT_PROFILE="FRAGMENT_KEY_PATIENT_PROFILE";
	private static String FRAGMENT_KEY_PATIENT_ID="FRAGMENT_KEY_PATIENT_ID";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		patientProfileUrl=getArguments().getString(FRAGMENT_KEY_PATIENT_PROFILE);
		patientId=getArguments().getString(FRAGMENT_KEY_PATIENT_ID);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		
		View v = inflater.inflate(R.layout.fragment_donate_share, container,
				false);
		
			// Initialize ListView and set initial view to patientAdapter
		shareIv = (ImageView) v.findViewById(R.id.shareIv);
		fundTreatmentBtn=(Button) v.findViewById(R.id.donateBtn);
		setupUIListeners();
		return v;
	}

	
	private void setupUIListeners() {
		shareIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startShareIntent();
				
			}
		});
		
		fundTreatmentBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startFundTreatmentIntent();
			}
		});
		
	}

	private void startFundTreatmentIntent() {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(patientProfileUrl));
		startActivity(browserIntent);
	}
	private void startShareIntent() {
		Intent shareIntent = new Intent();
	    shareIntent.setAction(Intent.ACTION_SEND);
	    shareIntent.putExtra(Intent.EXTRA_TEXT, patientProfileUrl);
	    shareIntent.setType("text/plain");
	    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Fund Treatment");
	    startActivity(Intent.createChooser(shareIntent, "Share Story"));		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}



	public static DonateShareFragment newInstance(Patient patient) {
		DonateShareFragment fragment = new DonateShareFragment();
		Bundle args = new Bundle();
		args.putString(FRAGMENT_KEY_PATIENT_ID,patient.getObjectId().toString());
		args.putString(FRAGMENT_KEY_PATIENT_PROFILE,patient.getProfileUrl());
		
		//Sharing Profile URL (Mostly) required for share/donate
		//Also keeping Patient id for usecases where it requires to read patients story or other attributes.
		//In that case Patient object can be read from database.
		
		fragment.setArguments(args);
		return fragment;
	}
	

}
