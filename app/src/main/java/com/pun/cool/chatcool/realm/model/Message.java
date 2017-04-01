package com.pun.cool.chatcool.realm.model;

import org.parceler.Parcel;

import io.realm.RealmObject;

/**
 * Created by Cool on 13/3/2560.
 */

public class Message extends RealmObject {

    String id;
    String chatRoomId;
    String userId;
    String message;
    Long createdAt;

    public Message() {
    }

    public Message(String id, String chatRoomId, String userId, String message, Long createdAt) {
        this.id = id;
        this.chatRoomId = chatRoomId;
        this.userId = userId;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}
