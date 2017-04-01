package com.pun.cool.chatcool.firebase.model;

import org.parceler.Parcel;

import io.realm.RealmObject;

/**
 * Created by Cool on 13/3/2560.
 */

public class User {

    String id;
    String name;
    String email;
    String token;
    String createdAt;

    public User() {
    }

    public User(String id, String name, String email, String token, String createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.token = token;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
