package com.conducthq.auspost.model.response;

import com.conducthq.auspost.model.data.DietaryRequirements;
import com.conducthq.auspost.model.data.Principal;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by conduct19 on 21/10/2016.
 */

public class PrincipalResponse implements Serializable {

    @SerializedName("data")
    Response response;

    public Response getResponse() {
        return response;
    }

    public class Response implements Serializable {

        String token;
        ArrayList<DietaryRequirements> dietaryRequirements;
        Principal principal;

        public String getToken() {
            return token;
        }

        public ArrayList<DietaryRequirements> getDietaryRequirements() {
            return dietaryRequirements;
        }

        public Principal getPrincipal() {
            return principal;
        }

    }


}
