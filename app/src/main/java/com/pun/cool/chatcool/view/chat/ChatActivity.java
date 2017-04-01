package com.pun.cool.chatcool.view.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.pun.cool.chatcool.R;
import com.pun.cool.chatcool.bus.BusProvider;
import com.pun.cool.chatcool.config.Config;
import com.pun.cool.chatcool.firebase.FireBaseDatabaseManager;
import com.pun.cool.chatcool.firebase.model.Message;
import com.pun.cool.chatcool.view.room.MainActivity;
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

    private ArrayList<Message> messageArrayList;
    private Message messageSend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        chatRoomId = intent.getStringExtra("chat_room_id");
        String title = intent.getStringExtra("name");

        if (chatRoomId == null) {
            Toast.makeText(getApplicationContext(), "Chat room not found!", Toast.LENGTH_SHORT).show();
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
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    messageArrayList.add((Message) snapshot.getValue());
                }

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

    private void sendMessage() {
        final String message = etMessage.getText().toString().trim();

        if (TextUtils.isEmpty(message)) {
            Toast.makeText(getApplicationContext(), "Enter a message", Toast.LENGTH_SHORT).show();
            return;
        }

        etMessage.setText("");

        FireBaseDatabaseManager.getInstance().addMessage(chatRoomId, chatAdapter.getUserId(), message, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                long time = 1;
                messageSend = new Message("", chatRoomId, chatAdapter.getUserId(), message, time);
                chatAdapter.newMessage(messageSend);
            }
        });
    }

    private void pushNotification(String type, String message) {
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jData = new JSONObject();
        try {
            jNotification.put("title", "Google I/O 2016");
            jNotification.put("body", "Firebase Cloud Messaging (App)");
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            jNotification.put("click_action", "OPEN_ACTIVITY_1");

            //jData.put("picture_url", "http://opsbug.com/static/google-io.jpg");
            jData.put("id", "123456");
            jData.put("user_id", chatAdapter.getUserId());
            jData.put("chat_room_id", chatRoomId);
            jData.put("message", message);

            switch(type) {
                case "tokens":
                    JSONArray ja = new JSONArray();
                    ja.put("c5pBXXsuCN0:APA91bH8nLMt084KpzMrmSWRS2SnKZudyNjtFVxLRG7VFEFk_RgOm-Q5EQr_oOcLbVcCjFH6vIXIyWhST1jdhR8WMatujccY5uy1TE0hkppW_TSnSBiUsH_tRReutEgsmIMmq8fexTmL");
                    ja.put(FirebaseInstanceId.getInstance().getToken());
                    jPayload.put("registration_ids", ja);
                    break;
                case "topic":
                    jPayload.put("to", "/topics/firstchatroom");
                    break;
                case "condition":
                    jPayload.put("condition", "'sport' in topics || 'news' in topics");
                    break;
                default:
                    jPayload.put("to", FirebaseInstanceId.getInstance().getToken());
            }

            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);
            jPayload.put("data", jData);

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", AUTH_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send FCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jPayload.toString().getBytes());

            // Read FCM response.
            InputStream inputStream = conn.getInputStream();
            final String resp = convertStreamToString(inputStream);

            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "pushNotification: " + resp);
//                    mTextView.setText(resp);
                }
            });
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }

    @Override
    public void onClick(View v) {
        if (v == btnSend) {
            final String message = etMessage.getText().toString();
            if (message.isEmpty()) {
                return;
            }

            etMessage.setText("");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    pushNotification("topic", message);
                }
            }).start();

            //BusProvider.getInstance().post(new Message("as", chatRoomId, chatAdapter.getUserId(), etMessage.getText().toString(), (long) 0));
        }
    }

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
