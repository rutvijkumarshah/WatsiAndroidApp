#Highlevel schema model for Watsi App
### Please Open file it text editor this not correctly formatted for Markdown.

Patient:

	  -  id											    : long (MUST Matching with one from website)
	  - first_name 									: String
	  - last_name 									: String
	  - medical_partner							: MedicalPartner
	  - medical_need								: String
	  - target_donation							: Double
	  - donations_received 					: List<Donation>				 
	  - isFullyFunded								: Boolean
	  - dateAdded									  : Date
	  - dateFullyFunded							: Date
	  - age											    : Int
	  - location_country						: String
	  - Story										    : String CLOB/HTML


Donar:

  	- id                                        : long
  	- email                                     : String email 
  	- donations_made								            : Donation
  	- first_name 									              : String
    - last_name 									              : String
    - donations_made	 						              : List<Donation>
    - pref_share_donation_on_public_feed			  : Boolean
    - join_date										              : Date
    - location                                  : longitude/lettitude (Important for showing donation heat map)


Donation:

  	- id 											: uuid_string
  	- donar                   : Donar
  	- isAnonymous							: Boolean
  	- donation_amount					: Double
  	- donated_at							: Date
	- patient            :Patient

Medical Partner:

      - id                      : long
      - name                    : name
      - website_url             : url_string
      
News:

  	- id 											  : uuid_string
  	- type											: type_hifi_fullyfunded,type_new_patient_added,type_patient_received_donation
  	- patient                   : Patient
  	- donation                  : Donation (Optioanl)
  	- campaign_content					: HTML
  	
