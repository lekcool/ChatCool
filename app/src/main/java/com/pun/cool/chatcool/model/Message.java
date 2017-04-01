package com.pun.cool.chatcool.model;

import org.parceler.Parcel;

/**
 * Created by Cool on 13/3/2560.
 */
@Parcel
public class Message {

    String id;
    String message;
    String createdAt;
    User user;

    public Message() {
    }

    public Message(String id, String message, String createdAt, User user) {
        this.id = id;
        this.message = message;
        this.createdAt = createdAt;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
