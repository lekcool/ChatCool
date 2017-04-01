package com.pun.cool.chatcool.common;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Cool on 14/3/2560.
 */

public class App extends Application {

    private static App mInstance;

    public static synchronized App getInstance() {
        if (mInstance == null) mInstance = new App();
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Contextor.getInstance().init(getApplicationContext());
        initRealm();
    }

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .build();
        Realm.setDefaultConfiguration(configuration);
    }
}
