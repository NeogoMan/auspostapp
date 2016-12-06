package com.conducthq.auspost.model.request;

/**
 * Created by conduct19 on 27/10/2016.
 */

public class RequestPrincipalEvent {

    Boolean accepted;
    String notes;

    public RequestPrincipalEvent(Boolean accepted, String notes) {
        this.accepted = accepted;
        this.notes = notes;
    }
}
