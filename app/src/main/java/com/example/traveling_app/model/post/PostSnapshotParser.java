package com.example.traveling_app.model.post;


import androidx.annotation.NonNull;
import com.example.traveling_app.common.DatabaseReferences;
import com.example.traveling_app.model.user.User;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class PostSnapshotParser implements SnapshotParser<Post> {
    public static PostSnapshotParser INSTANCE = new PostSnapshotParser();

    private PostSnapshotParser() {

    }

    @NonNull
    @Override
    public Post parseSnapshot(@NonNull DataSnapshot postSnapshot) {
        Post post = postSnapshot.getValue(Post.class);
        post.setIdPost(postSnapshot.getKey());
        DatabaseReferences.USER_DATABASE_REF.child(post.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                User user = userSnapshot.getValue(User.class);
                if (user == null) {
                    postSnapshot.getRef().removeValue();
                    return;
                }
                post.setFullName(user.getFullName());
                post.setProfileImageUrl(user.getProfileImage());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return post;
    }
}


