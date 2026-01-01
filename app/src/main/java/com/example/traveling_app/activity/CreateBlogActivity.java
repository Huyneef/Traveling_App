package com.example.traveling_app.activity;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.traveling_app.R;
import com.example.traveling_app.common.Constants;
import com.example.traveling_app.common.DatabaseReferences;
import com.example.traveling_app.common.StorageReferences;
import com.example.traveling_app.dialog.LoadingDialog;
import com.example.traveling_app.model.post.Post;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import java.util.Calendar;

public class CreateBlogActivity extends AppCompatActivity {

    private Uri postImageUri;
    private ImageView postImageView;
    private EditText postContentEditText;
    private Button postButton;
    private ActivityResultLauncher<PickVisualMediaRequest> selectImageLauncher;
    private final TextWatcher postContentEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void afterTextChanged(Editable s) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (postContentEditText.getText().length() > 0)
                postButton.setEnabled(true);
            else
                postButton.setEnabled(false);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createblog_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        postImageView = findViewById(R.id.postImageView);
        postContentEditText = findViewById(R.id.postContentEditText);
        postButton = findViewById(R.id.postButton);
        selectImageLauncher = registerForActivityResult(Constants.PICK_PHOTO_RESULT_CONTRACT, this::selectPostImage);

        postContentEditText.addTextChangedListener(postContentEditTextWatcher);
        postContentEditText.setOnKeyListener((view, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER && postButton.isEnabled()) {
                postButton.performClick();
                return true;
            }
            return false;
        });

        postImageView.setOnClickListener(v -> selectImageLauncher.launch(Constants.PICK_PHOTO_REQUEST));
        postButton.setOnClickListener(this::onPostButtonClick);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        postContentEditText.removeTextChangedListener(postContentEditTextWatcher);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void selectPostImage(Uri uri) {
        if (uri == null)
            return;
        postImageUri = uri;
        Glide.with(this).load(uri).into(postImageView);
    }


    private void onPostButtonClick(View v) {
        DatabaseReference postRef = DatabaseReferences.POST_DATABASE_REF.push();
        postButton.setEnabled(false);
        postContentEditText.removeTextChangedListener(postContentEditTextWatcher);
        if (postImageUri != null) {
            LoadingDialog uploadingDialog = new LoadingDialog(this, R.string.uploading_image);
            uploadingDialog.show();
            StorageReference imageStorageReference = StorageReferences.POST_IMAGE_STORAGE_REF.child(postRef.getKey() + ".jpeg");
            imageStorageReference.putFile(postImageUri).addOnSuccessListener(this, snapshot -> {
                imageStorageReference.getDownloadUrl().addOnSuccessListener(this, uri -> postToFirebase(postRef, uri.toString()));
                uploadingDialog.dismiss();
            });
        }
        else
            postToFirebase(postRef, null);
    }

    @SuppressWarnings("unused")
    private void postToFirebase(final DatabaseReference postRef, final String imageUrl) {
        String username = PreferenceManager.getDefaultSharedPreferences(this).getString("username", Constants.DEFAULT_USERNAME);
        String title = postContentEditText.getText().toString();
        long time = Calendar.getInstance().getTimeInMillis();
        Post post = new Post(null, imageUrl, username, title, time);
        postRef.setValue(post).addOnSuccessListener(this, nothing -> finish());
    }

}
