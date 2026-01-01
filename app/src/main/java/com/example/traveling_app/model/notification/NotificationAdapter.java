package com.example.traveling_app.model.notification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.traveling_app.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.text.DateFormat;

public class NotificationAdapter extends FirebaseRecyclerAdapter<Notification, NotificationViewHolder> {

    private final DateFormat dateFormat;
    private final DateFormat timeFormat;
    private final RequestManager imageLoader;

    public NotificationAdapter(@NonNull FirebaseRecyclerOptions<Notification> options, Context context) {
        super(options);
        dateFormat = android.text.format.DateFormat.getLongDateFormat(context);
        timeFormat = android.text.format.DateFormat.getTimeFormat(context);
        imageLoader = Glide.with(context);
    }

    @Override
    protected void onBindViewHolder(@NonNull NotificationViewHolder holder, int position, @NonNull Notification model) {
        holder.bindToView(model, dateFormat, timeFormat);
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new NotificationViewHolder(view, imageLoader);
    }
}
