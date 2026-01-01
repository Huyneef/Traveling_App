package com.example.traveling_app.model.notification;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.RequestManager;
import com.example.traveling_app.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.DateFormat;
import java.util.Date;

class NotificationViewHolder extends RecyclerView.ViewHolder {

    private final TextView txtContent, txtTime;
    private final ImageView senderAvatarPicture;
    private Notification notification;
    private final RequestManager imageLoader;
    private static final DatabaseReference NOTIFICATION_REF = FirebaseDatabase.getInstance().getReference("/notifications");
    public NotificationViewHolder(@NonNull View itemView, RequestManager imageLoader) {
        super(itemView);
        this.imageLoader = imageLoader;
        LinearLayout layoutDelete = itemView.findViewById(R.id.layout_delete);
        txtContent = itemView.findViewById(R.id.content_noti);
        txtTime = itemView.findViewById(R.id.time_noti);
        senderAvatarPicture = itemView.findViewById(R.id.image_noti);
        layoutDelete.setOnClickListener(v -> NOTIFICATION_REF.child(notification.getId()).removeValue());
    }

    public void bindToView(Notification notification, DateFormat dateFormat, DateFormat timeFormat) {
        this.notification = notification;
        Date date = new Date(notification.getTime());
        txtContent.setText(notification.getContent());
        txtTime.setText(dateFormat.format(date) + " " + timeFormat.format(date));
        senderAvatarPicture.setImageResource(0);
        notification.getProfileImageUrlAsync(url -> {
            if (url == null)
                imageLoader.load(R.drawable.user_profile_icon).into(senderAvatarPicture);
            else
                imageLoader.load(url).circleCrop().into(senderAvatarPicture);
        });
    }
}
