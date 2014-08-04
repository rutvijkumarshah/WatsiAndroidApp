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

package codepath.watsiapp;

import android.content.Context;
import codepath.watsiapp.models.Donation;
import codepath.watsiapp.models.Donor;
import codepath.watsiapp.models.NewsItem;
import codepath.watsiapp.models.Patient;
import codepath.watsiapp.utils.Util;

import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ParseHelper {

	private Context context;
	
	public ParseHelper(Context context) {
		this.context=context;
	}
	public  ParseQuery<Patient> getAllPatientsQuery(){
		ParseQuery<Patient> patientQuery= new ParseQuery("Patient");
		if(!Util.isNetworkAvailable(context)) {
			/***
			 * 
			 * Parse Offline logic is erroring out for boolean key sorting.
			 * In offline mode patient objects will be sorted by createdDate 
			 * 
			 * 
			 */
			patientQuery.orderByAscending("updatedAt");
			patientQuery.fromLocalDatastore();
		}else {
			patientQuery.orderByAscending("isFullyFunded");
		}
		return patientQuery;
	};
	
	
	public  ParseQuery<NewsItem> getAllNewsFeedItem() {
		ParseQuery<NewsItem> newsItemQuery= new ParseQuery("NewsItem");
		newsItemQuery.orderByDescending("updatedAt");
		newsItemQuery.include("patient");
		newsItemQuery.include("donation");
		newsItemQuery.include("donation.donor");
		newsItemQuery.whereNotEqualTo("hidden", true);
		if(!Util.isNetworkAvailable(context)) {
			newsItemQuery.fromLocalDatastore();
		}
		return newsItemQuery;
	}
	public ParseQuery<Donation> getDonationsByDonorId(String donorId) {		
		Donor donor = ParseObject.createWithoutData(Donor.class, donorId);
		return getDonationsByDonor(donor);	
	}
	
	public ParseQuery<Donation> getDonationsByDonor(Donor donor) {
		if(Util.isNetworkAvailable(context)) {
			ParseQuery<Donation> donationsByDonorId= new ParseQuery("Donation");
			donationsByDonorId.whereEqualTo("donor", donor)
			.addDescendingOrder("donationDate");
			return donationsByDonorId;
		}
		return null;
	};
	public  ParseQuery<Donor> findDonorByEmail(String emailAddress){
	
		ParseQuery<Donor> query= new ParseQuery("Donor");
		query.whereEqualTo("email", emailAddress);
		
		return query;

	}
	public ParseQuery<Donation> findDonationById(String donationId) {
		ParseQuery<Donation> query= new ParseQuery("Donation");
		query.whereEqualTo("objectId", donationId);
		
		return query;
	}
}
