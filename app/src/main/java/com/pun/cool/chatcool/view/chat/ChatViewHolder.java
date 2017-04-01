package com.pun.cool.chatcool.view.chat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pun.cool.chatcool.R;
import com.pun.cool.chatcool.firebase.model.Message;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Cool on 14/3/2560.
 */

public class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.profileImageView)
    CircleImageView profileImageView;

    @BindView(R.id.nameView)
    TextView nameView;

    @BindView(R.id.messageView)
    TextView messageView;

    @BindView(R.id.messageImageView)
    ImageView messageImageView;

    public ChatViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
    }

    public void setupData(Message message) {
        //messageView.setVisibility(View.GONE);
        messageImageView.setVisibility(View.GONE);
        nameView.setText("LEK");
        messageView.setText(message.getMessage());
    }

    @Override
    public void onClick(View v) {

    }
}
