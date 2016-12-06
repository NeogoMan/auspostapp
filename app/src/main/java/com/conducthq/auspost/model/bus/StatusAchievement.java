package com.conducthq.auspost.model.bus;

public class StatusAchievement {

    int id;
    int status;

    public StatusAchievement(int id, int status) {
        this.status = status;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }
}
