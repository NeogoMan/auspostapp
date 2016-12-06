package com.conducthq.auspost.model.response;

import com.conducthq.auspost.model.data.Feedback;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FeedbackResponse {

    @SerializedName("data")
    Response response;

    public Response getResponse() {
        return response;
    }

    public class Response implements Serializable {

        Feedback feedback;
        boolean success;
        boolean error;

        public Feedback getFeedback() {
            return feedback;
        }

        public boolean isSuccess() {
            return success;
        }

        public boolean isError() {
            return error;
        }
    }
}
