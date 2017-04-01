package com.pun.cool.chatcool.view.room;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pun.cool.chatcool.OnRecyclerListener;
import com.pun.cool.chatcool.R;
import com.pun.cool.chatcool.firebase.model.ChatRoom;
import com.pun.cool.chatcool.model.Message;
import com.pun.cool.chatcool.view.chat.ChatViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cool on 14/3/2560.
 */

public class RoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ChatRoom> arrayList;
    private OnRecyclerListener listener;

    public RoomAdapter(ArrayList<ChatRoom> arrayList, OnRecyclerListener listener) {
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_room, parent, false);
        return new RoomViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RoomViewHolder viewHolder = (RoomViewHolder) holder;
        viewHolder.setupData(getItem(position));
    }

    public ChatRoom getItem(int position) {
        return arrayList.get(position);
    }

    public void setArrayList(List<ChatRoom> arrayList) {
        this.arrayList = arrayList;
    }

    public List<ChatRoom> getArrayList() {
        return arrayList;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
