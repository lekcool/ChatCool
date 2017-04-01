package com.pun.cool.chatcool.common;

import android.content.Context;

/**
 * Created by Cool on 14/3/2560.
 */

public class Contextor {
    private static final Contextor ourInstance = new Contextor();

    private Context context;

    public static Contextor getInstance() {
        return ourInstance;
    }


    private Contextor() {
    }

    public void init(Context context) {
        this.context = context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
}
