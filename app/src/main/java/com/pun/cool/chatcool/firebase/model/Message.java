package com.pun.cool.chatcool.firebase.model;

import org.parceler.Parcel;

import io.realm.RealmObject;

/**
 * Created by Cool on 13/3/2560.
 */
@Parcel
public class Message {

    String id;
    String chatRoomId;
    String userId;
    String username;
    String message;
    String createdAt;

    public Message() {
    }

    public Message(String id, String chatRoomId, String userId, String username, String message, String createdAt) {
        this.id = id;
        this.chatRoomId = chatRoomId;
        this.userId = userId;
        this.username = username;
        this.message = message;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
