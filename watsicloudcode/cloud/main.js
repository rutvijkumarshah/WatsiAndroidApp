
Parse.Cloud.afterSave("PaymentConfirmatons", function(request) {
  console.log("After save called :"+request);
  var donorId;
  var Donor = Parse.Object.extend("Donor");
  var Donation = Parse.Object.extend("Donation");       
  var payerEmail=request.object.get("donorEmail");
  var payerFullName=request.object.get("donorName");
  var payerFirstName=payerFullName.split(" ")[0];
  var payerlastName=payerFullName.split(" ")[1];
  var patient=request.object.get("patient");
  var amount=request.object.get("amount");
  //check donors list to see if donor already exists
  console.log("searching donor by email :"+payerEmail);
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
        console.log("about to save donation");
          var donationObj=new Donation();
        donationObj.set("patient", patient);
      donationObj.set("donor", donorObj);
      donationObj.set("donationAmount", amount);
      donationObj.set("donationDate", new Date());
      donationObj.save();
      var receivedDonation=patient.get("receivedDonation") + amount;
      patient.set("receivedDonation",receivedDonation);
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