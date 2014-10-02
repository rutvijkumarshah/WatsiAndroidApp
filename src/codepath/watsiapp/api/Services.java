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

package codepath.watsiapp.api;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.android.AndroidLog;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import codepath.watsiapp.modelsv2.Donation;
import codepath.watsiapp.modelsv2.Donor;
import codepath.watsiapp.modelsv2.NewsItem;
import codepath.watsiapp.modelsv2.Patient;
import codepath.watsiapp.utils.GsonHelper;

public class Services {
	
	private RestAdapter restAdapter;
	private PatientService patientService;
	private NewsItemService newsItemService;
	private DonorService donorService;
	private DonationService donationService;
	private PaymentConfirmatons paymentConfirmationService;
	static final String LOG_TAG="SERVICES";

	// Service Interfaces
	public interface PatientService {
		@GET("/Patient?order=isFullyFunded,-createdAt&include=medicalPartner&count=1")
		public void getPatients(@Query("limit") int limit,
				@Query("skip") int skip, Callback<Patient> callback);

	}

	public interface NewsItemService {
		@GET("/NewsItem?include=patient,donation,donation.donor&count=1&order=-updatedAt")
		public void getNewsItems(@Query("limit") int limit,
				@Query("skip") int skip, Callback<NewsItem> callback);

	}

	public interface DonorService {
		// ?where={\"email\" : \"{donorEmail}\" }"
		@GET("/Donor")
		public void findtDonorById(@Query("where") String whereClause,
				Callback<Donor> callback);
	}

	public interface DonationService {
		@GET("/Donation/{donationObjectId}?include=patient,donor")
		public void findtDonationById(
				@Path("donationObjectId") String donationObjectId,
				Callback<Donation> callback);
	}

	public interface PaymentConfirmatons {
		@POST("/PaymentConfirmatons")
		public void postConfirmation(PaymentConfirmatons confirmation,Callback<PaymentConfirmatons> callback);
	}

	// Add getters for services here
	public PatientService getPatientService() {
		if (patientService == null) {
			patientService = restAdapter.create(PatientService.class);
		}
		return patientService;
	}

	public NewsItemService getNewsItemService() {
		if (newsItemService == null) {
			newsItemService = restAdapter.create(NewsItemService.class);
		}
		return newsItemService;
	}

	public DonorService getDonorService() {
		if (donorService == null) {
			donorService = restAdapter.create(DonorService.class);
		}
		return donorService;
	}

	public DonationService getDonationService() {
		if (donationService == null) {
			donationService = restAdapter.create(DonationService.class);
		}
		return donationService;
	}

	public PaymentConfirmatons getPaymentConfirmatons() {
		if (paymentConfirmationService == null) {
			paymentConfirmationService = restAdapter
					.create(PaymentConfirmatons.class);
		}
		return paymentConfirmationService;
	}

	// --------- Building blocks of Retrofit --------------------

	private static Services _instance;
	
	public static synchronized void  init( String appId,String apiKey) {
		if(_instance==null) {
			_instance=new Services(appId, apiKey);
		}
		
	}
	public static  Services getInstance() {
		if(_instance==null) {
			throw new RuntimeException("Services is not initialized. Call Services.init() before getting instance");
		}
		return _instance;
	}
	private  Services( String appId,String apiKey) {
		// do nothing if adapter is already exists
		if (restAdapter == null) {
			//createRequestIntercepter(appId, apiKey);
			createAdapter(appId, apiKey);
		}
	}


	// expensive call must be singleton
	private synchronized void createAdapter(final String appId,final String apiKey) {
		if (restAdapter == null) {
			
			RequestInterceptor reqIntercepter=new RequestInterceptor() {
				@Override
				public void intercept(RequestFacade request) {
					// Add common headers here
					request.addHeader("X-Parse-Application-Id", appId);
					request.addHeader("X-Parse-REST-API-Key", apiKey);
				}
			};
			
			restAdapter = new RestAdapter.Builder()
					.setEndpoint("https://api.parse.com/1/classes")
					.setRequestInterceptor(reqIntercepter)
					.setConverter(new GsonConverter(GsonHelper.getGson()))
					.setLogLevel(LogLevel.FULL).setLog(new AndroidLog(LOG_TAG))
					.build();
		}
	}

}