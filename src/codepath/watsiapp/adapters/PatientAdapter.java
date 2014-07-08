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
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import codepath.watsiapp.R;
import codepath.watsiapp.models.Patient;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class PatientAdapter extends ParseQueryAdapter<Patient> {

	private String url="https://d3w52z135jkm97.cloudfront.net/uploads/profile/image/2237/profile_Ny-33537.pre_op-03_.jpg";
	public PatientAdapter(Context context) {
		// load all patients
		// if required this is the place to apply where filters on patients list
		super(context, new ParseQueryAdapter.QueryFactory<Patient>() {
			public ParseQuery create() {
				return new ParseQuery("Patient");
			}
		});
	}

	@Override
	public View getItemView(Patient patient, View v, ViewGroup parent) {
		if (v == null) {
			v = View.inflate(getContext(), R.layout.item_patient, null);
		}

		// Add and download the image
		ParseImageView patientImage = (ParseImageView) v
				.findViewById(R.id.progressBarImageView);
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(patient.getPhotoUrl(), patientImage);

//		ParseFile imageFile = patient.getPhoto();
//		if (imageFile != null) {
//			patientImage.setParseFile(imageFile);
//			patientImage.loadInBackground();
//		}

		// Add the title view
		TextView titleTextView = (TextView) v.findViewById(R.id.name);
		titleTextView.setText(patient.getFirstName()+" "+patient.getLastName());

		return v;

	}

}
