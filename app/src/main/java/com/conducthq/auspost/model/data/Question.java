package com.conducthq.auspost.model.data;

import java.util.ArrayList;

/**
 * Created by conduct19 on 26/10/2016.
 */

public class Question {
    Integer id;
    Integer displayOrder;
    String content;
    String correctFeedback;
    String incorrectFeedback;
    ArrayList<QuestionOption> options;

    public Integer getId() {
        return id;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public String getContent() {
        return content;
    }

    public ArrayList<QuestionOption> getOptions() {
        return options;
    }

    public String getCorrectFeedback() {
        return correctFeedback;
    }

    public String getIncorrectFeedback() {
        return incorrectFeedback;
    }

}
