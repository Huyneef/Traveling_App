package com.example.traveling_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.traveling_app.R;
import com.example.traveling_app.common.DatabaseReferences;
import com.example.traveling_app.model.post.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Optional;

public class PostInfoActivity extends AppCompatActivity {
    private Post post;
    public static final String POST_PARAM = "post";
    private TextView commentCountTextView;
    private ValueEventListener commentCountListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            int commentCount = Optional.ofNullable(snapshot.getValue(Integer.class)).orElse(0);
            commentCountTextView.setText(String.valueOf(commentCount));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
    private DatabaseReference commentCountRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        post = getIntent().getExtras().getParcelable(POST_PARAM);
        if (post == null)
            finish();
        RequestManager imageLoader = Glide.with(this);
        commentCountRef = DatabaseReferences.POST_DATABASE_REF.child(post.getIdPost()).child("commentCount");

        setContentView(R.layout.status_main);
        ImageView commentImageView = findViewById(R.id.imageView4),
                back = findViewById(R.id.back_detail_blog),
                avatarImageView = findViewById(R.id.avatarPictureImageView),
                postImage = findViewById(R.id.postImageView);

        TextView usernameTextView = findViewById(R.id.usernameTextView),
                postContentTextView = findViewById(R.id.postContentTextView);
        commentCountTextView = findViewById(R.id.commentCountTextView);

        commentImageView.setOnClickListener(this::openComment);
        back.setOnClickListener(v -> finish());
        post.getFullNameAsync(usernameTextView::setText);
        post.getProfileImageUrlAsync(url -> {
            if (url == null)
                imageLoader.load(R.drawable.user_profile_icon).into(avatarImageView);
            else
                imageLoader.load(url).circleCrop().into(avatarImageView);
        });
        postContentTextView.setText(post.getTitle());
        if (post.getPostImgUrl() != null)
            imageLoader.load(post.getPostImgUrl()).centerCrop().into(postImage);
    }

    @Override
    protected void onStart() {
        super.onStart();
        commentCountRef.addValueEventListener(commentCountListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        commentCountRef.removeEventListener(commentCountListener);
    }

    private void openComment(View v) {
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra(CommentActivity.POST_PARAM, post);
        startActivity(intent);
    }





}
