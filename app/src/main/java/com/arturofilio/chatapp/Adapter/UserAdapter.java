package com.arturofilio.chatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arturofilio.chatapp.MessageActivity;
import com.arturofilio.chatapp.Model.User;
import com.arturofilio.chatapp.R;
import com.arturofilio.chatapp.ViewHolder;
import com.bumptech.glide.Glide;

import java.util.List;


public class UserAdapter extends RecyclerView.Adapter<ViewHolder> {

    private Context mContext;
    private List<User> mUsers;

    public UserAdapter(Context mContext, List<User> mUsers) {
        this.mUsers = mUsers;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        final User user = mUsers.get(position);
        viewHolder.username.setText(user.getUsername());
        if (user.getImageURL().equals("default")) {

            viewHolder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {

            Glide.with(mContext).load(user.getImageURL()).into(viewHolder.profile_image);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid", user.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}
