package com.conducthq.auspost.model.data;

import java.util.ArrayList;

/**
 * Created by conduct19 on 28/10/2016.
 */

public class Message {

    Integer countMessageLikes;
    Integer id;
    String content;
    String message_type;
    String videoId;
    String title;
    String thumbnail;
    String created;
    Principal principal;
    Achievement achievement;
    ArrayList<MessageLike> messageLikes;

    public Message(Integer countMessageLikes, Integer id, String content, String message_type) {
        this.countMessageLikes = countMessageLikes;
        this.id = id;
        this.content = content;
        this.message_type = message_type;
    }

    public Integer getCountMessageLikes() {
        return countMessageLikes;
    }

    public void setCountMessageLikes(Integer countMessageLikes) {
        this.countMessageLikes = countMessageLikes;
    }

    public Integer getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getMessage_type() {
        return message_type;
    }

    public String getCreated() {
        return created;
    }

    public Principal getPrincipal() {
        return principal;
    }

    public Achievement getAchievement() {
        return achievement;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public ArrayList<MessageLike> getMessageLikes() {
        return messageLikes;
    }
}
