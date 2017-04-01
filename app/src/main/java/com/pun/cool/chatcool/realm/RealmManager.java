package com.pun.cool.chatcool.realm;

import com.pun.cool.chatcool.realm.model.Notification;
import com.pun.cool.chatcool.realm.model.User;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Cool on 14/3/2560.
 */

public class RealmManager {

    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_NOTIFICATIONS = "notifications";

    private static RealmManager ourInstance;

    public static RealmManager getInstance() {
        if (ourInstance == null) ourInstance = new RealmManager();
        return ourInstance;
    }

    public void storeUser(User addUser) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        User user = realm.createObject(User.class);
        user.setId(user.getId());
        user.setName(addUser.getName());
        user.setEmail(addUser.getEmail());
        realm.commitTransaction();
        realm.close();
    }

    public void addNotification(String notification) {
        // get old notifications
        String oldNotifications = getNotifications();
        if (oldNotifications != null) {
            oldNotifications += "|" + notification;
        } else {
            oldNotifications = notification;
        }

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Notification notify = new Notification(oldNotifications);
        realm.insertOrUpdate(notify);
        realm.commitTransaction();
        realm.close();
    }

    public String getNotifications() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Notification> notification = realm.where(Notification.class).findAll();
        realm.close();
        if (notification.size() > 0) {
            return notification.get(notification.size()).getNotification();
        }
        return null;
    }

    public void clear() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
        realm.close();
    }
}
