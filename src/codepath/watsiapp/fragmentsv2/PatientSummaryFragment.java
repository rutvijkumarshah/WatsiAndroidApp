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

package codepath.watsiapp.fragmentsv2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import codepath.watsiapp.R;
import codepath.watsiapp.modelsv2.MedicalPartner;
import codepath.watsiapp.modelsv2.Patient;
import codepath.watsiapp.utils.Util;

public class PatientSummaryFragment extends Fragment {

	private static final String PATIENT_OBJ__KEY = "EXTRA_PATIENT_OBJ";

	private TextView medicalNeedTv;
	private TextView personBio;
	private TextView percentageFundedTv;
	private TextView medicalPartnerVal;
	private Patient patientObj;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		patientObj=(Patient)getArguments().getSerializable(PATIENT_OBJ__KEY);
	
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

		medicalNeedTv.setText(patientObj.getMedicalNeed());
		Util.applyPrimaryFont(getActivity(), medicalNeedTv);
		personBio.setText(patientObj.getFullName() +" is " +patientObj.getAgeString() +" from "+patientObj.getCountry()+"." );
		Util.applyPrimaryFont(getActivity(), personBio);
		
		
		percentageFundedTv.setText(patientObj.getDonationProgressPecentage() + "% funded");
		Util.applyPrimaryFont(getActivity(), percentageFundedTv);

		final MedicalPartner medicalPartner=patientObj.getMedicalPartner();
		medicalPartnerVal.setText(medicalPartner.getName());
		Util.applyPrimaryFont(getActivity(), medicalPartnerVal);
		Util.makeTextViewHyperlink(medicalPartnerVal);
		medicalPartnerVal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Util.starShowMedicalPartnerIntent(getActivity(),
						medicalPartner.getWebsiteUrl());
			}
		});
	
		return v;

	}

	public static PatientSummaryFragment newInstance(Patient patient) {
		PatientSummaryFragment fragment = new PatientSummaryFragment();
		Bundle args = new Bundle();
		args.putSerializable(PATIENT_OBJ__KEY, patient);
		fragment.setArguments(args);
		return fragment;
	}

}
