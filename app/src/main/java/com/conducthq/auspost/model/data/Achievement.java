package com.conducthq.auspost.model.data;

import java.util.ArrayList;

/**
 * Created by conduct19 on 26/10/2016.
 */

public class Achievement {
    Integer id;
    String name;
    String qrCode;
    String iconUrl;
    String videoUrl;
    String videoId;
    String videoTitle;
    String videoThumbnail;
    String description;
    String outcomes;
    String summary;
    String infoReadMaterial;
    String quizIntro;
    ArrayList<Faq> faqs;
    Quiz quiz;
    String status;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getQrCode() {
        return qrCode;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getDescription() {
        return description;
    }

    public String getOutcomes() {
        return outcomes;
    }

    public String getSummary() {
        return summary;
    }

    public String getInfoReadMaterial() {
        return infoReadMaterial;
    }

    public String getQuizIntro() {
        return quizIntro;
    }

    public ArrayList<Faq> getFaqs() {
        return faqs;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public String getVideoThumbnail() {
        return videoThumbnail;
    }
}
