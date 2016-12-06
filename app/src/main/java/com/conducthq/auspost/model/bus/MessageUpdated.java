package com.conducthq.auspost.model.bus;

import com.conducthq.auspost.model.data.Message;

public class MessageUpdated {

    Message message;

    public MessageUpdated(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
