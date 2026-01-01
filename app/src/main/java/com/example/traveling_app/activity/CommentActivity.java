package com.example.traveling_app.activity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveling_app.R;
import com.example.traveling_app.common.Constants;
import com.example.traveling_app.common.DatabaseReferences;
import com.example.traveling_app.model.notification.Notification;
import com.example.traveling_app.model.comment.Comment;
import com.example.traveling_app.model.comment.CommentRecyclerViewAdapter;
import com.example.traveling_app.model.comment.CommentSnapshotParser;
import com.example.traveling_app.model.post.Post;
import com.example.traveling_app.model.user.User;
import com.example.traveling_app.model.user.UserSnapshotParser;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;
import java.util.Calendar;
import java.util.Optional;

public class CommentActivity extends AppCompatActivity {

    private Post post;
    private User currentUser;
    private EditText commentEditText;
    private Button sendButton;
    private RecyclerView commentRecyclerView;
    private CommentRecyclerViewAdapter adapter;
    public static final String POST_PARAM = "post";
    private final TextWatcher onTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void afterTextChanged(Editable s) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            sendButton.setEnabled(commentEditText.getText().length() > 0);
        }

    };

    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        post = getIntent().getParcelableExtra(POST_PARAM);
        if (post == null) {
            finish();
            return;
        }
        setContentView(R.layout.comment_main);
        String currentUserId = PreferenceManager.getDefaultSharedPreferences(this).getString("username", Constants.DEFAULT_USERNAME);
        if (!post.getUsername().equals(currentUserId))
            DatabaseReferences.USER_DATABASE_REF.child(currentUserId).get().addOnSuccessListener(this, dataSnapshot -> {
                currentUser = UserSnapshotParser.INSTANCE.parseSnapshot(dataSnapshot);
                initializeActivityWhenAlready();
            });
        else {
            currentUser = new User(currentUserId, null, null, null, 0, false, null);
            initializeActivityWhenAlready();
        }
    }

    private void initializeActivityWhenAlready() {
        Query query = DatabaseReferences.POST_COMMENT_DATABASE_REF.orderByChild("postId").equalTo(post.getIdPost());
        FirebaseRecyclerOptions<Comment> options = new FirebaseRecyclerOptions.Builder<Comment>().setQuery(query, CommentSnapshotParser.INSTANCE).build();
        commentEditText = findViewById(R.id.commentEditText);
        sendButton = findViewById(R.id.sendButton);
        commentRecyclerView = findViewById(R.id.commentRecycleView);

        commentEditText.addTextChangedListener(onTextChangedListener);
        commentEditText.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP && sendButton.isEnabled()) {
                sendButton.performClick();
                return true;
            }
            return false;
        });
        adapter = new CommentRecyclerViewAdapter(options, this);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentRecyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.setOnDataChangedListener(() -> commentRecyclerView.scrollToPosition(adapter.getItemCount() - 1));

        sendButton.setOnClickListener(v -> {
            pushComment();
            sendButton.setEnabled(false);
            commentEditText.setText("");
        });
    }

    private void pushComment() {
        Comment comment = new Comment(commentEditText.getText().toString(), currentUser.getUsername(), post.getIdPost(), Calendar.getInstance().getTimeInMillis());
        DatabaseReferences.POST_COMMENT_DATABASE_REF.push().setValue(comment).addOnSuccessListener(nothing -> commentRecyclerView.scrollToPosition(adapter.getItemCount() - 1));
        DatabaseReferences.POST_DATABASE_REF.child(post.getIdPost()).child("commentCount").get().addOnSuccessListener(dataSnapshot -> {
            int commentCount = Optional.ofNullable(dataSnapshot.getValue(Integer.class)).orElse(0);
            dataSnapshot.getRef().setValue(commentCount + 1);
        });
        if (!post.getUsername().equals(currentUser.getUsername())) {
            String fullName = Optional.ofNullable(currentUser.getFullName()).orElse(currentUser.getUsername());
            String notificationContent = getString(R.string.notification_content_placeholder, fullName);
            Notification notification = new Notification(currentUser.getUsername(), post.getUsername(), notificationContent, Calendar.getInstance().getTimeInMillis());
            DatabaseReferences.NOTIFICATION_DATABASE_REF.push().setValue(notification);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.stopListening();
        adapter.setOnDataChangedListener(null);
        commentEditText.removeTextChangedListener(onTextChangedListener);
    }
}
