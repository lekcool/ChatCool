package com.pun.cool.chatcool.firebase;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pun.cool.chatcool.firebase.model.ChatRoom;
import com.pun.cool.chatcool.firebase.model.Message;
import com.pun.cool.chatcool.firebase.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cool on 16/3/2560.
 */

public class FireBaseDatabaseManager {

    private static final String TAG = "FireBaseDatabaseManager";

    private static final String CHILD_USER = "users";
    private static final String CHILD_MESSAGE = "message";
    private static final String CHILD_CHAT_ROOM = "chat_room";

    private static FireBaseDatabaseManager ourInstance;

    public static FireBaseDatabaseManager getInstance() {
        if (ourInstance == null) ourInstance = new FireBaseDatabaseManager();
        return ourInstance;
    }

    private DatabaseReference mRootRef;

    private FireBaseDatabaseManager() {
        mRootRef = FirebaseDatabase.getInstance().getReference();
    }

    public DatabaseReference getRootRef() {
        return mRootRef;
    }

    public DatabaseReference getUserRef() {
        return getRootRef().child(CHILD_USER);
    }

    public DatabaseReference getMessageRef() {
        return getRootRef().child(CHILD_MESSAGE);
    }

    public DatabaseReference getChatRoomRef() {
        return getRootRef().child(CHILD_CHAT_ROOM);
    }

    public void createUser(String name, String email) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            return;
        }

        User user = new User(firebaseUser.getUid(), name, email, null, getCreatedTimestamp(ServerValue.TIMESTAMP));
        getUserRef().child(firebaseUser.getUid()).setValue(user);
    }

    public void updateUserToken(String token) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }

        getUserRef().child(user.getUid()).child("token").setValue(token);
    }

    private void getUser(String uId, final ValueEventListener listener) {
        getUserRef().child(uId).addListenerForSingleValueEvent(listener);
    }


    public void addMessage(String chatRoomId, String userId, String msg, OnSuccessListener<Void> listener) {
        String id = getMessageRef().push().getKey();
        Message message = new Message(id, chatRoomId, userId, msg, getCreatedTimestamp(ServerValue.TIMESTAMP));
        getMessageRef().child(message.getId()).setValue(message).addOnSuccessListener(listener);
    }

    public void getMessageChatRoom(String chatRoomId, final ValueEventListener listener) {
        getMessageRef().child(chatRoomId).addListenerForSingleValueEvent(listener);
    }

    public void addChatRoom(String name) {
        ChatRoom chatRoom = new ChatRoom(getChatRoomRef().push().getKey(), name, getCreatedTimestamp(ServerValue.TIMESTAMP));
        getChatRoomRef().child(chatRoom.getId()).setValue(chatRoom);
    }

    public void getAllChatRoom(final ValueEventListener listener) {
        getChatRoomRef().addListenerForSingleValueEvent(listener);
    }

    public void getChatRoom(String chatRoomId, final ValueEventListener listener) {
        getChatRoomRef().child(chatRoomId).addListenerForSingleValueEvent(listener);
    }

    public List<ChatRoom> getArrayChatRoom(DataSnapshot dataSnapshot) {
//        GenericTypeIndicator< ArrayList<ChatRoom>> t =new GenericTypeIndicator<ArrayList<ChatRoom>>() {};

        List<ChatRoom> chatRoomList = new ArrayList<>(); //dataSnapshot.getValue(t);
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            ChatRoom chatRoom = snapshot.getValue(ChatRoom.class);
            chatRoomList.add(chatRoom);

//            Log.e(TAG, "subscribeToTopic: "+ chatRoom.getId());
//            FirebaseMessaging.getInstance().subscribeToTopic(chatRoom.getId());

            Log.e(TAG, "subscribeToTopic: "+ chatRoom.getName().replace(" ", ""));
            FirebaseMessaging.getInstance().subscribeToTopic(chatRoom.getName().replace(" ", ""));
        }

        return chatRoomList;
    }

    private Long getCreatedTimestamp(Object created) {
        if (created instanceof Long) {
            return (Long) created;
        }

        return null;
    }
}
