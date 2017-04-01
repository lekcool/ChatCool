package com.pun.cool.chatcool.realm.model;

import io.realm.RealmObject;

/**
 * Created by Cool on 18/3/2560.
 */

public class Notification extends RealmObject {

    String notification;

    public Notification() {
    }

    public Notification(String notification) {
        this.notification = notification;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }
}
