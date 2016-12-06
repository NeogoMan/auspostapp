package com.conducthq.auspost.model.data;

/**
 * Created by conduct19 on 26/10/2016.
 */

public class QuestionOption {
    Integer id;
    Integer displayOrder;
    String content;
    Boolean correct;

    public Integer getId() {
        return id;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public String getContent() {
        return content;
    }

    public Boolean getCorrect() {
        return correct;
    }
}
