package com.conducthq.auspost.model.response;

import com.conducthq.auspost.model.data.Achievement;
import com.conducthq.auspost.model.data.ContactDetails;
import com.conducthq.auspost.model.data.DietaryRequirements;
import com.conducthq.auspost.model.data.Event;
import com.conducthq.auspost.model.data.Principal;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by conduct19 on 21/10/2016.
 */

public class ContentResponse implements Serializable {

    @SerializedName("data")
    Response response;

    public Response getResponse() {
        return response;
    }

    public class Response implements Serializable {

        ArrayList<DietaryRequirements> dietaryRequirements;
        Principal principal;
        ContactDetails contactDetails;
        Event event;

        public ArrayList<DietaryRequirements> getDietaryRequirements() {
            return dietaryRequirements;
        }

        public Principal getPrincipal() {
            return principal;
        }

        public ContactDetails getContactDetails() {
            return contactDetails;
        }

        public Event getEvent() {
            return event;
        }
    }

}
