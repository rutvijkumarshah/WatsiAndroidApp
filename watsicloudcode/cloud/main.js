
Parse.Cloud.afterSave("PaymentConfirmatons", function(request) {
  var donorId;
  var Donor = Parse.Object.extend("Donor");
  var Donation = Parse.Object.extend("Donation");				
  var Patient = Parse.Object.extend("Patient");				
  var payerEmail=request.object.get("donorEmail");
  var payerFullName=request.object.get("donorName");
  var payerFirstName=payerFullName.split(" ")[0];
  var payerlastName=payerFullName.split(" ")[1];
  var patient=request.object.get("patient");
  var amount=request.object.get("amount");
  //check donors list to see if donor already exists
  query = new Parse.Query("Donor");
  query.equalTo("email",payerEmail);
  query.find({

  success: function(results) {
  	console.log("FindDonor by email successful results :"+results.length);
    if(results && results.length >0){
    		//Donor record exists
    		var existing_donor=results[0];
    		
    		var donorObj=new Donor();
    		donorObj.id=existing_donor.id
    	    var donationObj=new Donation();
		    donationObj.set("patient", patient);
			donationObj.set("donor", donorObj);
			donationObj.set("donationAmount", amount);
			donationObj.set("donationDate", new Date());
			donationObj.save();
			if(patient){
				console.log("# patient id="+patient.id);	
				var query = new Parse.Query(Patient);
				query.get(patient.id, {
				  success: function(patientObj) {
				  		var receivedDonation = patientObj.get("receivedDonation") + amount;
				  		patientObj.set("receivedDonation",receivedDonation);
						patientObj.save();
				  },
				  error: function(object, error) {
				    // The object was not retrieved successfully.
				    // error is a Parse.Error with an error code and description.
				    console.log("error while loading patient by id");
				  }
				});

			}
    }else{

    	//Donor record does not exists
		var donorObj = new Donor();
		donorObj.set("email", payerEmail);
		donorObj.set("firstName", payerFirstName);
		donorObj.set("lastName", payerlastName);
		donorObj.set("memberSince", new Date());
		donorObj.save(null, {
		  success: function(donor) {
		    // Execute any logic that should take place after the object is saved.
		    var newlyCreatedDonorId=donor.id;
		    var Donation = Parse.Object.extend("Donation");
		    var donationObj=new Donation();
		    donationObj.set("patient", patient);
			donationObj.set("donor", donor);
			donationObj.set("donationAmount", amount);
			donationObj.set("donationDate", new Date());
			donationObj.save();

			var receivedDonation=patient.get("receivedDonation") + amount;
			patient.set("receivedDonation",receivedDonation);

		  },
		  error: function(donor, error) {
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