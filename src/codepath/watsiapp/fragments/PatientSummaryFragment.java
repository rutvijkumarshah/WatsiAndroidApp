/***

The MIT License (MIT)
Copyright � 2014 Rutvijkumar Shah
 
Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the �Software�), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
 
The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.
 
THE SOFTWARE IS PROVIDED �AS IS�, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 ***/

package codepath.watsiapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import codepath.watsiapp.R;
import codepath.watsiapp.models.MedicalPartner;
import codepath.watsiapp.models.Patient;
import codepath.watsiapp.utils.Util;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

public class PatientSummaryFragment extends Fragment {

	private static final String MEDICAL_NEED_KEY = "EXTRA_MEDICAL_NEED";
	private static final String AGE_KEY = "EXTRA_AGE";
	private static final String LOCATION_KEY = "EXTRA_LOCATION";
	private static final String FUNDEDPROGRESS_KEY = "EXTRA_FUNDEDPROGRESS";
	private static final String PATIENT_ID__KEY = "EXTRA_PATIENT_ID";

	private String medicalNeed;
	private String age;
	private String location;
	private String fundedProgress;

	private TextView medicalNeedTv;
	private TextView personBio;
	private TextView percentageFundedTv;
	private TextView medicalPartnerVal;
	private String patientId;
	private Patient patientObj;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		medicalNeed = getArguments().getString(MEDICAL_NEED_KEY);
		age = getArguments().getString(AGE_KEY);
		location = getArguments().getString(LOCATION_KEY);
		fundedProgress = getArguments().getString(FUNDEDPROGRESS_KEY);
		patientId = getArguments().getString(PATIENT_ID__KEY);
		patientObj = ParseObject.createWithoutData(Patient.class, patientId);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_patient_summary, container,
				false);

		medicalNeedTv = (TextView) v.findViewById(R.id.medicalNeed);
		personBio =(TextView) v.findViewById(R.id.personBio);
		percentageFundedTv = (TextView) v.findViewById(R.id.percentageFunded);
		medicalPartnerVal = (TextView) v.findViewById(R.id.medicalPartnerVal);

		medicalNeedTv.setText(medicalNeed);
		Util.applyPrimaryFont(getActivity(), medicalNeedTv);
		personBio.setText(patientObj.getFullName() +" is " +age +" from "+location+"." );
		Util.applyPrimaryFont(getActivity(), personBio);
		percentageFundedTv.setText(fundedProgress);
		Util.applyPrimaryFont(getActivity(), percentageFundedTv);

		patientObj.getMedicalPartner(new GetCallback<MedicalPartner>() {
			@Override
			public void done(final MedicalPartner object, ParseException arg1) {
				medicalPartnerVal.setText(object.getName());
				Util.applyPrimaryFont(getActivity(), medicalPartnerVal);
				Util.makeTextViewHyperlink(medicalPartnerVal);
				medicalPartnerVal.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Util.starShowMedicalPartnerIntent(getActivity(),
								object.getWebsiteUrl());
					}
				});
			}
		});

		return v;

	}

	public static PatientSummaryFragment newInstance(String medicalNeed,
			String ageStr, String location, String fundedProgressStr,
			String patientId) {
		PatientSummaryFragment fragment = new PatientSummaryFragment();
		Bundle args = new Bundle();
		args.putString(MEDICAL_NEED_KEY, medicalNeed);
		args.putString(AGE_KEY, ageStr);
		args.putString(LOCATION_KEY, location);
		args.putString(FUNDEDPROGRESS_KEY, fundedProgressStr);
		args.putString(PATIENT_ID__KEY, patientId);
		fragment.setArguments(args);
		return fragment;
	}

}
