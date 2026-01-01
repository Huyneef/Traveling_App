package com.example.traveling_app.model.comment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.RequestManager;
import com.example.traveling_app.R;
import com.example.traveling_app.common.DatabaseReferences;
import com.example.traveling_app.model.user.User;
import com.example.traveling_app.model.user.UserSnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.text.DateFormat;
import java.util.Date;

class CommentViewHolder extends RecyclerView.ViewHolder {

    private final TextView usernameTextView;
    private final TextView contentTextView;
    private final TextView timeStampTextView;
    private final ImageView userAvatarImageView;
    private final RequestManager imageLoader;
    private final DateFormat dateFormat;
    private final DateFormat timeFormat;

    public CommentViewHolder(@NonNull View itemView, DateFormat dateFormat, DateFormat timeFormat, RequestManager imageLoader) {
        super(itemView);
        this.dateFormat = dateFormat;
        this.timeFormat = timeFormat;
        this.imageLoader = imageLoader;
        usernameTextView = itemView.findViewById(R.id.usernameTextView);
        contentTextView = itemView.findViewById(R.id.commentTextView);
        timeStampTextView = itemView.findViewById(R.id.timeStampTextView);
        userAvatarImageView = itemView.findViewById(R.id.userAvatarImageView);
    }

    public void bindCommentToView(Comment comment) {
        comment.getFullNameAsync(usernameTextView::setText);
        comment.getProfileImageUrlAsync(profileImg -> {
            if (profileImg == null)
                imageLoader.load(R.drawable.user_profile_icon).into(userAvatarImageView);
            else
                imageLoader.load(profileImg).circleCrop().into(userAvatarImageView);
        });
        contentTextView.setText(comment.getContent());
        Date date = new Date(comment.getTimeStamp());
        timeStampTextView.setText(dateFormat.format(date) + " " + timeFormat.format(date));
    }
}
