package com.conducthq.auspost.model.response;

import com.conducthq.auspost.model.data.ContactDetails;
import com.conducthq.auspost.model.data.DietaryRequirements;
import com.conducthq.auspost.model.data.Event;
import com.conducthq.auspost.model.data.Message;
import com.conducthq.auspost.model.data.Principal;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by conduct19 on 21/10/2016.
 */

public class MessageResponse implements Serializable {

    @SerializedName("data")
    Response response;

    public Response getResponse() {
        return response;
    }

    public class Response implements Serializable {

        ArrayList<Message> items;
        Integer totalCount;
        Integer currentPageNumber;
        Integer numItemsPerPage;

        public ArrayList<Message> getItems() {
            return items;
        }

        public Integer getTotalCount() {
            return totalCount;
        }

        public Integer getCurrentPageNumber() {
            return currentPageNumber;
        }

        public Integer getNumItemsPerPage() {
            return numItemsPerPage;
        }

    }

}
