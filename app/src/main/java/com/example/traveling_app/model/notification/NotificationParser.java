package com.example.traveling_app.model.notification;

import androidx.annotation.NonNull;
import com.example.traveling_app.common.DatabaseReferences;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;

public class NotificationParser implements SnapshotParser<Notification> {
    @NonNull
    @Override
    public Notification parseSnapshot(@NonNull DataSnapshot snapshot) {
        Notification notification = snapshot.getValue(Notification.class);
        notification.setId(snapshot.getKey());
        DatabaseReferences.USER_DATABASE_REF.child(notification.getSendFrom()).child("profileImage").get().addOnSuccessListener(dataSnapshot -> {
            String imagePath = dataSnapshot.getValue(String.class);
            notification.setProfileImageUrl(imagePath);
        });
        return notification;
    }
}
