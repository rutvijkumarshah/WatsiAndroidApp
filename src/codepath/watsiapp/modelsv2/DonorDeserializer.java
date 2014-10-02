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
import java.text.ParseException;
import java.util.Date;

import codepath.watsiapp.utils.GsonHelper;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class DonorDeserializer implements JsonDeserializer<Donor>{

	@Override
	public Donor deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext ctx) throws JsonParseException {
		
		JsonObject obj = json.getAsJsonObject();
		String memberSinceStr=obj.getAsJsonObject("memberSince").get("iso").getAsString();
		Date dt=null;
		Donor donor = new Donor();
		
		try {
			dt=GsonHelper.parseDate(memberSinceStr);
			donor.createdAt=GsonHelper.parseDate(obj.get("createdAt").getAsString());
			donor.updatedAt=GsonHelper.parseDate(obj.get("updatedAt").getAsString());
		} catch (ParseException e) {
			throw new JsonParseException(e.getLocalizedMessage());
		}
		donor.setMemberSince(dt);
		donor.setEmail(obj.get("email").getAsString());
		donor.setObjectId(obj.get("objectId").getAsString());
		donor.setFirstName(obj.get("firstName").getAsString());
		donor.setLastName(obj.get("lastName").getAsString());		
		return donor;
	}

}
