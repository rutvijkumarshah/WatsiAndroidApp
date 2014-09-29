package codepath.watsiapp.modelsv2;

import static codepath.watsiapp.utils.GsonHelper.getGson;

import java.util.Date;

import codepath.watsiapp.models.Donation;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * News Items in News feed.
 * 
 * There are four types of News Item.
 * 
 * 1. Donation Raised for patient
 
  	Sample Json for this type of news item.
  	{
	    "donation": {
	        ....
	    },
	    "type": "donation_raised",
	    "patient": {
	        ....
	    },
	    "createdAt": "2014-09-25T23:33:11.983Z",
	    "updatedAt": "2014-09-25T23:33:11.983Z",
	    "objectId": "CHKRsZlmM7"
	}
 * 
 * 2.Patient status become Fully Funded 
 *
	Sample Json for this type of news item.
	 
	 {
	    "donation": {
	        ....
	    },
	    "type": "fully_funded",
	    "patient": {
	      ...
	    },
	    "createdAt": "2014-08-06T03:37:20.040Z",
	    "updatedAt": "2014-08-06T03:37:20.040Z",
	    "objectId": "HkDElijaWu"
	}
 
 * 3.Campaign Message
 *
	Sample Json for this type of news item.
	{
	    "campaignContent": "Instead of watching a movie this Friday in cinema, donate it and feel good !",
	    "type": "campaign_message",
	    "createdAt": "2014-07-30T05:07:27.578Z",
	    "updatedAt": "2014-08-01T05:58:07.086Z",
	    "objectId": "Dz9RGIFUPT"
	}

 *4. New Patient Onboarded
 *
	 Sample Json for this type of news item.	
	 {
		    "patient": {
		        ...
		    }
		    ,
		    "type": "on_boarded",
		    "createdAt": "2014-07-10T04:58:44.324Z",
		    "updatedAt": "2014-07-10T04:58:56.270Z",
		    "objectId": "JDFrvfamol"
	}

 * @author Rutvijkumar Shah
 */
@Table(name = "NewsItem")
public class NewsItem extends BaseModel{
	
	@Column(name = "patient")
	public Patient patient;
	
	public Donation donation;
	
	@Column(name = "type")
	public String type;
	
	@Column(name = "campaign_content")
	public String campaignContent;
	
	
	
	/**** 
	 * Common fields for every Entity 
	 * Ideally should be part of BaseModel kind of class,
	 * But as AndroidOrm does not support inheritance it has to be duplicated until I find some good workaround.
	 * 
	 * ***/
	@Column(name = "object_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	public String objectId;
    
    @Column(name = "created_at")
	public Date createdAt;
    
    @Column(name = "updated_at")
	public Date updatedAt;

	@Override
	public long persist() {
		Patient patient=this.patient;
		Patient existingPatient=loadByObjectId(Patient.class, patient.objectId);
		if(existingPatient!=null) {
			if(existingPatient.updatedAt.getTime() < patient.updatedAt.getTime()) {
				//New Object is more recent, replacing it with new object
				existingPatient.reload(patient);
				this.patient=existingPatient;
			}
		}
		patient.persist();
		this.save();
		return 0;
	}

	
	public static  NewsItem fromJson(String jsonString) {
		NewsItem newsItem=getGson().fromJson(jsonString, NewsItem.class);
		return newsItem;
	}

}
