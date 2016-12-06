package com.conducthq.auspost.model.data;

public class AchievementLock {

    int eventId;
    String qrCode;

    public AchievementLock() {
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
