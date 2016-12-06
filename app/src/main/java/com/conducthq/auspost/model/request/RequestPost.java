package com.conducthq.auspost.model.request;

/**
 * Created by conduct19 on 27/10/2016.
 */

public class RequestPost {

    String messageType;
    String content;

    public RequestPost(String content) {
        this.messageType = "comment";
        this.content = content;
    }
}
