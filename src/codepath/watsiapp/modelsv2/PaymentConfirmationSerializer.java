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

package codepath.watsiapp.modelsv2;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class PaymentConfirmationSerializer implements
		JsonSerializer<PaymentConfirmation> {

	@Override
	public JsonElement serialize(PaymentConfirmation payConfirmation,
			Type arg1, JsonSerializationContext ctx) {

		JsonObject payConfirmJson = new JsonObject();
		JsonObject patientJson = new JsonObject();

		patientJson.addProperty("__type", "Pointer");
		patientJson.addProperty("className", "Patient");
		patientJson.addProperty("objectId", payConfirmation.getPatientObjectId());

		payConfirmJson.add("patient", patientJson);
		payConfirmJson.addProperty("donorName", payConfirmation.getDonorName());
		payConfirmJson.addProperty("donorEmail", payConfirmation.getDonorEmail());
		payConfirmJson.addProperty("amount", payConfirmation.getAmount());
		payConfirmJson.addProperty("payPalConfirmation",
				payConfirmation.getPayPalConfirmation());
		payConfirmJson.addProperty("isAnonymous", payConfirmation.isAnonymous());

		return payConfirmJson;
	}

}
