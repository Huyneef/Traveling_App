package com.example.traveling_app.model.user;

import androidx.annotation.NonNull;

import com.example.traveling_app.model.savedtours.SavedTour;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;

public class UserSnapshotParser implements SnapshotParser<User> {
    public static UserSnapshotParser INSTANCE = new UserSnapshotParser();
    
    private UserSnapshotParser() {
        
    }

    @NonNull
    @Override
    public User parseSnapshot(@NonNull DataSnapshot snapshot) {
        User user = snapshot.getValue(User.class);
        user.setUsername(snapshot.getKey());
        return user;
    }
}
