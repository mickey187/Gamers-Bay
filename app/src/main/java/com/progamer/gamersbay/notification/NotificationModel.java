package com.progamer.gamersbay.notification;

import androidx.annotation.NonNull;

public class NotificationModel {
    private String title;
    private String description;
    private String notificationId;
    private String notificationTimeStamp;
    public NotificationModel() {
        //public no-arg constructor needed
    }

    public NotificationModel(String title, String description){
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationTimeStamp() {
        return notificationTimeStamp;
    }

    public void setNotificationTimeStamp(String notificationTimeStamp) {
        this.notificationTimeStamp = notificationTimeStamp;
    }
}
