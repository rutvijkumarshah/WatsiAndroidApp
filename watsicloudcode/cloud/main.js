var Donor = Parse.Object.extend("Donor");
var NewsItem = Parse.Object.extend("NewsItem");
var Donation = Parse.Object.extend("Donation");				
var Patient = Parse.Object.extend("Patient");
var saveDonation = function(donorObj,patient,amount){
	  	var donationObj=new Donation();
	    donationObj.set("patient", patient);
		donationObj.set("donor", donorObj);
		donationObj.set("donationAmount", amount);
		donationObj.set("donationDate", new Date());
		donationObj.save();
		if(patient){
			var query = new Parse.Query(Patient);
			query.get(patient.id, {
			  success: function(patientObj) {

			  		//Update receieved donation amount of Patient
			  		var targetDonation = patientObj.get("targetDonation");
			  		var receivedDonation = patientObj.get("receivedDonation") + amount;
			  		patientObj.set("receivedDonation",receivedDonation);
			  		
			  		//Add News feed item for this donation.
			  		var news = new NewsItem();
			  		news.set("donation", donationObj);
					news.set("patient", patientObj);
					if(!(targetDonation > receivedDonation)){
			  			console.log("patient is marked as fully funded");
			  			patientObj.set("isFullyFunded",true);
			  			//fully funded
			  			news.set("type", "fully_funded");
			  		}else{
			  			//donation Raised
			  			news.set("type", "donation_raised");
			  		}

					patientObj.save();
					news.save();
			  },
			  error: function(object, error) {
			    // The object was not retrieved successfully.
			    // error is a Parse.Error with an error code and description.
			    console.log("error while loading patient by id");
			  }
			});

		}
 };

/***
* After save hook for Payment Confirmation.
* PaymentConfirmation contains transactional details which required to be processed,
* To update patient,donor,donations details and update wiring of objects.
*
*
*/
Parse.Cloud.afterSave("PaymentConfirmatons", function(request) {
  var donorId;
  var payerEmail=request.object.get("donorEmail");
  var payerFullName=request.object.get("donorName");
  var payerFirstName=payerFullName.split(" ")[0];
  var payerlastName=payerFullName.split(" ")[1];
  var patient=request.object.get("patient");
  var amount=request.object.get("amount");
  var afterSavePaymentConfirmation= this;
  

  //check donors list to see if donor already exists
  query = new Parse.Query("Donor");
  query.equalTo("email",payerEmail);

  query.find({
 
  success: function(results) {
  	var me = this;
  	console.log("##  me="+me);
  	console.log("FindDonor by email successful results :"+results.length);
    if(results && results.length >0){
    		//Donor record exists
    		var existing_donor=results[0];
    		var donorObj=new Donor();
    		donorObj.id=existing_donor.id
    	    saveDonation(donorObj,patient,amount);
    }else{

    	//Donor record does not exists
		var donorObj = new Donor();
		donorObj.set("email", payerEmail);
		donorObj.set("firstName", payerFirstName);
		donorObj.set("lastName", payerlastName);
		donorObj.set("memberSince", new Date());
		donorObj.save(null, {
		  success: function(donorObj) {
		    // Execute any logic that should take place after the object is saved.
		    saveDonation(donorObj,patient,amount);
		  },
		  error: function(donorObj, error) {
		    console.log("Error while adding new donor :"+error);
		  }
		});

    }
    
  },
  error: function(error) {
    console.log("FindDonor by email failed error :"+error);
  }
  
});

});


/***
* After save hook for Patient.
* Checks if patient updated is fully funded  or not.
* In case of fully funded patient it push notification
*/
Parse.Cloud.afterSave("Patient", function(request) {
  var Patient = Parse.Object.extend("Patient");				
  var isFullyFunded=request.object.get("isFullyFunded");
  var patientId=request.object.id;
  if(isFullyFunded){
  		//Push Notification
  	  Parse.Push.send({
	  channels: [ "NewsFeed" ],
	  data: {
	       action: "codepath.watsiapp.NEWSFEED",
	       notif_type: "fully_funded",
	       patientId: patientId
	  }
	}, {
	  success: function() {
	    console.log("Fully funded patient "+patientId+" Notification pushed successfully");
	  },
	  error: function(error) {
	    console.log("Fully funded patient "+patientId+" Notification push failed");
	    console.log("Error in push Notification: "+error);
	  }
	});
  }
});