package com.pun.cool.chatcool.view.room;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.pun.cool.chatcool.R;
import com.pun.cool.chatcool.common.OnRecyclerListener;
import com.pun.cool.chatcool.firebase.FireBaseDatabaseManager;
import com.pun.cool.chatcool.firebase.model.ChatRoom;
import com.pun.cool.chatcool.realm.RealmManager;
import com.pun.cool.chatcool.utils.NotificationUtils;
import com.pun.cool.chatcool.view.chat.ChatActivity;
import com.pun.cool.chatcool.view.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements OnRecyclerListener {

    private static final String TAG = "MainActivity";

    @BindView(R.id.userView)
    TextView userView;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private RoomAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initFireBase();
        initInstance();
        registerFcm();
        fetchChatRoom();

        //FireBaseDatabaseManager.getInstance().addChatRoom("info chat room");
    }

    private void initFireBase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();

            userView.setText(email);

            String uid = user.getUid();
        }
    }

    private void initInstance() {
        mAdapter = new RoomAdapter(new ArrayList<ChatRoom>(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    private void registerFcm() {
        FireBaseDatabaseManager.getInstance().updateUserToken(FirebaseInstanceId.getInstance().getToken());
    }

    private void fetchChatRoom() {
        FireBaseDatabaseManager.getInstance().getAllChatRoom(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<ChatRoom> chatRoomList = FireBaseDatabaseManager.getInstance().getArrayChatRoom(dataSnapshot);
                if (chatRoomList.size() > 0) {
                    mAdapter.setArrayList(chatRoomList);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "fetchChatRoom error: " + databaseError.getMessage());
                Toast.makeText(getApplicationContext(), "fetchChatRoom error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // clearing the notification tray
        NotificationUtils.clearNotifications();
    }

    private void signOut() {
        RealmManager.getInstance().clear();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view, int position) {
        ChatRoom chatRoom = mAdapter.getItem(position);
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("chat_room_id", chatRoom.getId());
        intent.putExtra("name", chatRoom.getName());
        startActivity(intent);
    }
}
