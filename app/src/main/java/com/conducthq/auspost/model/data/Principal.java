package com.conducthq.auspost.model.data;

/**
 * Created by conduct19 on 24/10/2016.
 */

public class Principal {

    Integer id;
    String firstName;
    String imageUrl;
    Boolean doNotPush;
    Boolean confirmed;
    String lastName;
    String email;
    String phone;
    String state;
    Integer dietaryRequirements;
    eventStatus eventStatus;

    public Principal() { }

    public Principal(Principal copy) {
        this.id = copy.id;
        this.firstName = copy.firstName;
        this.imageUrl = copy.imageUrl;
        this.doNotPush = copy.doNotPush;
        this.confirmed = copy.confirmed;
        this.lastName = copy.lastName;
        this.email = copy.email;
        this.phone = copy.phone;
        this.state = copy.state;
        this.dietaryRequirements = copy.dietaryRequirements;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getDoNotPush() {
        return doNotPush;
    }

    public void setDoNotPush(Boolean doNotPush) {
        this.doNotPush = doNotPush;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getDietaryRequirements() {
        return dietaryRequirements;
    }

    public void setDietaryRequirements(Integer dietaryRequirements) {
        this.dietaryRequirements = dietaryRequirements;
    }

    public Integer getId() {
        return id;
    }

    public eventStatus getEventStatus() {
        return eventStatus;
    }

    public class eventStatus {
        Integer id;
        String name;
        Boolean attending;

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Boolean getAttending() {
            return attending;
        }
    }

}
