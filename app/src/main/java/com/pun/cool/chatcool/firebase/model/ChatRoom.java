package com.pun.cool.chatcool.firebase.model;

import io.realm.RealmObject;

public class ChatRoom {

    private String id;
    private String name;
    private String createdAt;

    public ChatRoom() {
    }

    public ChatRoom(String id, String name, String createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
