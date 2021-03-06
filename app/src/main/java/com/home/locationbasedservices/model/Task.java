package com.home.locationbasedservices.model;

import java.util.Date;

public class Task {

    private String userEmail;
    private String title;
    private String description;
    private Double latitude;
    private Double longitude;
    private boolean notificationReminder;
    private boolean alarmRingerReminder;
    private Date creationDate;
    private Date reminderDate;

    public Task() {

    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public boolean isNotificationReminder() {
        return notificationReminder;
    }

    public void setNotificationReminder(boolean notificationReminder) {
        this.notificationReminder = notificationReminder;
    }

    public boolean isAlarmRingerReminder() {
        return alarmRingerReminder;
    }

    public void setAlarmRingerReminder(boolean alarmRingerReminder) {
        this.alarmRingerReminder = alarmRingerReminder;
    }

    public Date getCreationDate() { return creationDate; }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getReminderDate() { return reminderDate; }

    public void setReminderDate(Date reminderDate) {
        this.reminderDate = reminderDate;
    }
}
