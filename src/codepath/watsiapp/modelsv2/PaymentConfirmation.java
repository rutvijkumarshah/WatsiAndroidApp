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

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "PaymentConfirmation")
public class PaymentConfirmation extends BaseModel {
	
	@Column(name="donorName",notNull=true)
	private String donorName;
	
	@Column(name="donorEmail",notNull=true)
	private String donorEmail;
	
	@Column(name="patientObjectId",notNull=true)
	private String patientObjectId;
	
	@Column(name="amount",notNull=true)
	private double amount;
	
	@Column(name="payPalConfirmation",notNull=true)
	private String payPalConfirmation;
	
	@Column(name="isAnonymous",notNull=true)
	private boolean isAnonymous;
	
	@Column(name="isSynced",notNull=true)
	private boolean isSynced;
	

	@Override
	public long persist() {
		return this.save();
	}
	public List<PaymentConfirmation> findNonSyncedConfirmations() {
		return new Select().from(PaymentConfirmation.class).where("isSynced=?",false).execute();
	}
	public String getDonorName() {
		return donorName;
	}
	public void setDonorName(String donorName) {
		this.donorName = donorName;
	}
	public String getDonorEmail() {
		return donorEmail;
	}
	public void setDonorEmail(String donorEmail) {
		this.donorEmail = donorEmail;
	}
	public String getPatientObjectId() {
		return patientObjectId;
	}
	public void setPatientObjectId(String patientObjectId) {
		this.patientObjectId = patientObjectId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getPayPalConfirmation() {
		return payPalConfirmation;
	}
	public void setPayPalConfirmation(String payPalConfirmation) {
		this.payPalConfirmation = payPalConfirmation;
	}
	public boolean isAnonymous() {
		return isAnonymous;
	}
	public void setAnonymous(boolean isAnonymous) {
		this.isAnonymous = isAnonymous;
	}
	public boolean isSynced() {
		return isSynced;
	}
	public void setSynced(boolean isSynced) {
		this.isSynced = isSynced;
	}
	
	
}