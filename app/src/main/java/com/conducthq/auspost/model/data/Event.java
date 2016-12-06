package com.conducthq.auspost.model.data;

import java.util.ArrayList;

/**
 * Created by conduct19 on 26/10/2016.
 */

public class Event {
    Integer id;
    String name;
    String start;
    String end;
    String location;
    String locationName;
    String information;
    String informationUrl;
    String informationUrlTitle;
    String gettingThereUrl;
    String gettingThereUrlTitle;
    String gettingThere;
    String whatToBring;
    String whatToBringUrl;
    String whatToBringUrlTitle;
    String accommodation;
    String accommodationUrl;
    String accommodationUrlTitle;
    String lat;
    String lon;
    String imageUrl;
    ArrayList<Faq> faqs;
    ArrayList<Achievement> achievements;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getLocation() {
        return location;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getInformation() {
        return information;
    }

    public String getInformationUrl() {
        return informationUrl;
    }

    public String getInformationUrlTitle() {
        return informationUrlTitle;
    }

    public String getGettingThere() {
        return gettingThere;
    }

    public String getWhatToBring() {
        return whatToBring;
    }

    public String getWhatToBringUrl() {
        return whatToBringUrl;
    }

    public String getWhatToBringUrlTitle() {
        return whatToBringUrlTitle;
    }

    public String getAccommodation() {
        return accommodation;
    }

    public String getAccommodationUrl() {
        return accommodationUrl;
    }

    public String getAccommodationUrlTitle() {
        return accommodationUrlTitle;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ArrayList<Faq> getFaqs() {
        return faqs;
    }

    public ArrayList<Achievement> getAchievements() {
        return achievements;
    }

    public String getGettingThereUrl() {
        return gettingThereUrl;
    }

    public String getGettingThereUrlTitle() {
        return gettingThereUrlTitle;
    }
}
