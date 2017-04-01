package com.pun.cool.chatcool.view.chat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.pun.cool.chatcool.R;
import com.pun.cool.chatcool.bus.BusProvider;
import com.pun.cool.chatcool.firebase.FireBaseDatabaseManager;
import com.pun.cool.chatcool.firebase.model.Message;
import com.pun.cool.chatcool.utils.NotificationUtils;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cool on 14/3/2560.
 */

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = ChatActivity.class.getSimpleName();

    private static final String AUTH_KEY = "key=AIzaSyCUVJM5ALYde0T395rtmi_NCV9Q26QdHVI";

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.etMessage)
    EditText etMessage;

    @BindView(R.id.btnSend)
    Button btnSend;

    private String chatRoomId;
    private ChatAdapter chatAdapter;

    private List<Message> messageArrayList;
    private Message messageSend;
    private FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        chatRoomId = intent.getStringExtra("chat_room_id");
        String chatRoomName = intent.getStringExtra("chat_room_name");

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (chatRoomId == null) {
            Toast.makeText(getApplicationContext(), "Chat room not found!", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (user == null) {
            Toast.makeText(getApplicationContext(), "User not found!", Toast.LENGTH_SHORT).show();
            finish();
        }

        BusProvider.getInstance().register(this);
        initInstance();
        fetchChat();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //BusProvider.getInstance().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //BusProvider.getInstance().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    private void initInstance() {
        btnSend.setOnClickListener(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            chatAdapter = new ChatAdapter(user.getUid());
            recyclerView.setAdapter(chatAdapter);
            messageArrayList = new ArrayList<>();
            chatAdapter.setList(messageArrayList);
        } else {
            Toast.makeText(getApplicationContext(), "Chat room not found!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void fetchChat() {
        FireBaseDatabaseManager.getInstance().getMessageChatRoom(chatRoomId, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messageArrayList = FireBaseDatabaseManager.getInstance().getArrayMessage(dataSnapshot);

                chatAdapter.setList(messageArrayList);
                chatAdapter.notifyDataSetChanged();
                if (chatAdapter.getItemCount() > 1) {
                    recyclerView.smoothScrollToPosition(chatAdapter.getItemCount());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "fetchMessageChatRoom error: " + databaseError.getMessage());
                Toast.makeText(getApplicationContext(), "fetchMessageChatRoom error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessageToFireBaseDatabase(String message) {
        FireBaseDatabaseManager.getInstance().addMessage(chatRoomId, chatAdapter.getUserId(), user.getDisplayName(), message, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                long time = 1;
//                messageSend = new Message("", chatRoomId, chatAdapter.getUserId(), message, time);
//                chatAdapter.newMessage(messageSend);
            }
        });
    }

    private void sendMessageToFireBaseCloudMessaging(final String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NotificationUtils.pushNotification("topic", user.getUid(), user.getDisplayName(), chatRoomId, message);
            }
        }).start();
    }

    private void sendMessage() {
        final String message = etMessage.getText().toString();
        if (message.isEmpty()) {
            return;
        }

        etMessage.setText("");

        sendMessageToFireBaseDatabase(message);
        sendMessageToFireBaseCloudMessaging(message);

        //BusProvider.getInstance().post(new Message("as", chatRoomId, chatAdapter.getUserId(), etMessage.getText().toString(), (long) 0));
    }

    @Override
    public void onClick(View v) {
        if (v == btnSend) {
            sendMessage();
        }
    }

    /**
     * event bus - new message form cloud messaging
     *
     * @param message
     */
    @Subscribe
    public void subscribeMessage(final Message message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (chatRoomId != null) {
                    chatAdapter.newMessage(message);
                    chatAdapter.notifyDataSetChanged();
                    if (chatAdapter.getItemCount() > 1) {
                        recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, chatAdapter.getItemCount() - 1);
                    }
                }
            }
        });
    }

    @Produce
    public Message produce() {
        return messageSend;
    }
}
