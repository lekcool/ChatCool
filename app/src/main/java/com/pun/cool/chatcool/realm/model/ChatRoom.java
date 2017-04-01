package com.pun.cool.chatcool.realm.model;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Cool on 13/3/2560.
 */

public class ChatRoom extends RealmObject {

    String id;
    String name;
    Long createdAt;

    public ChatRoom() {
    }

    public ChatRoom(String id, String name, Long createdAt) {
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

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}
