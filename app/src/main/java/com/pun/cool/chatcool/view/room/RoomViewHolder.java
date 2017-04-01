package com.pun.cool.chatcool.view.room;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pun.cool.chatcool.OnRecyclerListener;
import com.pun.cool.chatcool.R;
import com.pun.cool.chatcool.firebase.model.ChatRoom;
import com.pun.cool.chatcool.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cool on 16/3/2560.
 */

public class RoomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.tvMessage)
    TextView tvMessage;

    @BindView(R.id.tvTimestamp)
    TextView tvTimestamp;

    @BindView(R.id.tvCount)
    TextView tvCount;

    private OnRecyclerListener listener;

    public RoomViewHolder(View itemView, OnRecyclerListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
        this.listener = listener;
    }

    public void setupData(ChatRoom chatRoom) {
        tvName.setText(chatRoom.getName());
//        tvMessage.setText(chatRoom.getLastMessage());
//        if (chatRoom.getUnreadCount() > 0) {
//            tvCount.setText(String.valueOf(chatRoom.getUnreadCount()));
//            tvCount.setVisibility(View.VISIBLE);
//        } else {
//            tvCount.setVisibility(View.GONE);
//        }

        tvTimestamp.setText(Utils.getTimeStamp(chatRoom.getCreatedAt()));
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition());
    }
}
