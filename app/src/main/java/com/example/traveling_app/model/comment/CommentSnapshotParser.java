package com.example.traveling_app.model.comment;
import androidx.annotation.NonNull;

import com.example.traveling_app.common.DatabaseReferences;
import com.example.traveling_app.model.user.User;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class CommentSnapshotParser implements SnapshotParser<Comment> {
    public static CommentSnapshotParser INSTANCE = new CommentSnapshotParser();

    private CommentSnapshotParser() {

    }

    @NonNull
    @Override
    public Comment parseSnapshot(@NonNull DataSnapshot commentSnapshot) {
        Comment comment = commentSnapshot.getValue(Comment.class);
        comment.setCommentId(commentSnapshot.getKey());
        DatabaseReferences.USER_DATABASE_REF.child(comment.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                User user = userSnapshot.getValue(User.class);
                if (user == null) {
                    commentSnapshot.getRef().removeValue();
                    return;
                }
                comment.setFullName(user.getFullName());
                comment.setProfileImageUrl(user.getProfileImage());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return comment;
    }
}
