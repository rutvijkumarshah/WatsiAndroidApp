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

package codepath.watsiapp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import codepath.watsiapp.modelsv2.Donation;
import codepath.watsiapp.modelsv2.DonationDeserializer;
import codepath.watsiapp.modelsv2.Donor;
import codepath.watsiapp.modelsv2.DonorDeserializer;
import codepath.watsiapp.modelsv2.PaymentConfirmation;
import codepath.watsiapp.modelsv2.PaymentConfirmationSerializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonHelper {
	private static final String DATE_FORMAT_PATTERN="yyyy-MM-dd'T'HH:mm:ss";
	
	@SuppressLint("SimpleDateFormat")
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
	
	public static Gson getGson() {
		Gson gson = new GsonBuilder()
		.setDateFormat(DATE_FORMAT_PATTERN)
		.registerTypeAdapter(PaymentConfirmation.class, new PaymentConfirmationSerializer())
        .registerTypeAdapter(Donation.class, new DonationDeserializer())
        .registerTypeAdapter(Donor.class, new DonorDeserializer())
        .create();
		return gson;
	}
	public static Date parseDate(String strDate) throws ParseException {
			return dateFormat.parse(strDate);
	}
}
