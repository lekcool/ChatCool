package com.pun.cool.chatcool.view.chat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pun.cool.chatcool.R;
import com.pun.cool.chatcool.firebase.model.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cool on 14/3/2560.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_OTHER = 0;
    private static final int TYPE_ME = 1;

    private ArrayList<Message> list;
    private String userId;

    public ChatAdapter(String userId) {
        this.userId = userId;
        list = new ArrayList<>();
    }

    public void setList(ArrayList<Message> list) {
        this.list = list;
    }

    public void newMessage(Message message) {
        list.add(message);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_ME) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_your_message, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_other_message, parent, false);
        }
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatViewHolder viewHolder = (ChatViewHolder) holder;
        viewHolder.setupData(list.get(position));
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public int getItemViewType(int position) {
        return userId.equals(list.get(position).getUserId()) ? TYPE_ME : TYPE_OTHER;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Message getItem(int position) {
        return list.get(position);
    }
}
