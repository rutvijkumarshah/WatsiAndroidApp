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

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "PaymentConfirmation")
public class PaymentConfirmation extends BaseModel {
	
	@Column(name="donorName",notNull=true)
	public String donorName;
	
	@Column(name="donorEmail",notNull=true)
	public String donorEmail;
	
	@Column(name="patientObjectId",notNull=true)
	public String patientObjectId;
	
	@Column(name="amount",notNull=true)
	public double amount;
	
	@Column(name="payPalConfirmation",notNull=true)
	public String payPalConfirmation;
	
	@Column(name="isAnonymous",notNull=true)
	public boolean isAnonymous;
	
	@Column(name="isSynced",notNull=true)
	public boolean isSynced;
	

	@Override
	public long persist() {
		return this.save();
	}
	public List<PaymentConfirmation> findNonSyncedConfirmations() {
		return new Select().from(PaymentConfirmation.class).where("isSynced=?",false).execute();
	}
	public String toJSON() throws JSONException {
		JSONObject payConfirmJson=new JSONObject();
		JSONObject patientJson=new JSONObject();
		patientJson.put("__type", "Pointer");
		patientJson.put("className", "Patient");
		patientJson.put("objectId", this.patientObjectId);
		
		payConfirmJson.put("patient", patientJson);
		payConfirmJson.put("donorName", this.donorName);
		payConfirmJson.put("donorEmail", this.donorEmail);
		payConfirmJson.put("amount", this.amount);
		payConfirmJson.put("payPalConfirmation", this.payPalConfirmation);
		payConfirmJson.put("isAnonymous", this.isAnonymous);
		
		return payConfirmJson.toString();
	}
}