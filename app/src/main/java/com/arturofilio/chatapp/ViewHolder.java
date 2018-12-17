package com.arturofilio.chatapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView username;
    public ImageView profile_image;

    public ViewHolder(View itemView) {
        super(itemView);

        username = itemView.findViewById(R.id.username);
        profile_image = itemView.findViewById(R.id.profile_image);
    }

}
