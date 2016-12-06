package com.conducthq.auspost.model.data;

/**
 * Created by conduct19 on 21/11/2016.
 */

public class DeviceToken {

    String deviceToken;
    String deviceType;
    String version;

    public DeviceToken(String deviceToken, String version) {
        this.deviceToken = deviceToken;
        this.deviceType = "android";
        this.version = version;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getVersion() {
        return version;
    }
}
