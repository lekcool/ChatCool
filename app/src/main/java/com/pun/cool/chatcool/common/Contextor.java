package com.pun.cool.chatcool.common;

import android.annotation.SuppressLint;
import android.content.Context;

class Contextor {

    @SuppressLint("StaticFieldLeak")
    private static Contextor ourInstance;

    private Context context;

    public static Contextor getInstance() {
        if (ourInstance == null) ourInstance = new Contextor();
        return ourInstance;
    }


    private Contextor() {
    }

    void init(Context context) {
        this.context = context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
}
