package com.conducthq.auspost.model.data;

/**
 * Created by conduct19 on 24/10/2016.
 */

public class DietaryRequirements {

    Integer id;
    String value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    //to display object as a string in spinner
    public String toString() {
        return value;
    }
}
