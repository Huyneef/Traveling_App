package com.example.traveling_app.activity;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.traveling_app.R;
import com.example.traveling_app.common.Constants;
import com.example.traveling_app.common.CurrentUser;
import com.example.traveling_app.common.DatabaseReferences;
import com.example.traveling_app.common.StorageReferences;
import com.example.traveling_app.dialog.LoadingDialog;
import com.example.traveling_app.model.user.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.HashMap;

public class UpdateUserInformationActivity extends AppCompatActivity {

    private static final long ONE_YEAR_MILISECONDS = 31536000000L;
    private DatabaseReference userProfileInfoReference;
    private StorageReference avatarPictureReference;
    private ActivityResultLauncher<PickVisualMediaRequest> setAvatarImageLauncher;
    private DatePickerDialog birthdayPickerDialog;
    private ImageView avatarPictureImageView;
    private EditText fullNameEditText, birthdayEditText, descriptionEditText;
    private RadioButton maleRadioButton, femaleRadioButton;
    private Button updateButton;
    private String profileId;
    private Uri avatarPictureUri;
    private int currentBirthday;


    @Override
    @SuppressWarnings("deprecation")
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_update_user_information);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profileId = PreferenceManager.getDefaultSharedPreferences(this).getString("username", Constants.DEFAULT_USERNAME);
        setAvatarImageLauncher = registerForActivityResult(Constants.PICK_PHOTO_RESULT_CONTRACT, this::setAvatarImageUri);
        userProfileInfoReference = DatabaseReferences.USER_DATABASE_REF.child(profileId);
        avatarPictureReference = StorageReferences.USER_AVATAR_PICTURES.child(profileId + ".jpeg");
        avatarPictureImageView = findViewById(R.id.avatarPictureImageView);
        fullNameEditText = findViewById(R.id.fullNameEditText);
        birthdayEditText = findViewById(R.id.birthdayEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        maleRadioButton = findViewById(R.id.maleRadioButton);
        femaleRadioButton = findViewById(R.id.femaleRadioButton);
        updateButton = findViewById(R.id.updateButton);
        birthdayPickerDialog = new DatePickerDialog(this);

        birthdayPickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis() - ONE_YEAR_MILISECONDS);
        birthdayPickerDialog.setOnDateSetListener(this::setBirthday);
        updateButton.setOnClickListener(this::updateUserInformation);
        birthdayEditText.setOnClickListener(nothing -> { if (!birthdayPickerDialog.isShowing()) birthdayPickerDialog.show(); });
        avatarPictureImageView.setOnClickListener(nothing -> setAvatarImageLauncher.launch(Constants.PICK_PHOTO_REQUEST));
        userProfileInfoReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userProfileInfoReference.removeEventListener(this);
                User user = snapshot.getValue(User.class);
                bindToView(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void setAvatarPictureImageView(Uri uri) {
        Glide.with(this).load(uri).circleCrop().into(avatarPictureImageView);
    }

    private void bindToView(User user) {
        int year, month, day;
        if (user.getProfileImage() != null) {
            avatarPictureImageView.setImageResource(0);
            avatarPictureReference.getDownloadUrl().addOnSuccessListener(this::setAvatarPictureImageView);
        }
        fullNameEditText.setText(user.getFullName());
        descriptionEditText.setText(user.getDescription());
        year = user.getBirthday() / 10000;
        month = (user.getBirthday() % 10000) / 100;
        day = user.getBirthday() % 100;
        setBirthday(null, year, month, day);
        if (user.getGender())
            maleRadioButton.setChecked(true);
        else
            femaleRadioButton.setChecked(true);
        updateButton.setEnabled(true);
    }

    private void setAvatarImageUri(Uri uri) {
        if (uri == null)
            return;
        avatarPictureUri = uri;
        setAvatarPictureImageView(uri);
    }

    private void setBirthday(@Nullable View v, int year, int month, int day) {
        if (year == 0)
            return;
        Calendar birthday = Calendar.getInstance();
        birthday.set(year, month, day);
        birthdayPickerDialog.getDatePicker().updateDate(year, month, day);
        birthdayEditText.setText(android.text.format.DateFormat.getLongDateFormat(this).format(birthday.getTime()));
        currentBirthday = year * 10000 + month * 100 + day;
    }

    private void updateUserInformation(@Nullable View v) {
        if (avatarPictureUri != null) {
            LoadingDialog loadingDialog = new LoadingDialog(this, R.string.uploading_image);
            loadingDialog.show();
            avatarPictureReference.putFile(avatarPictureUri).addOnSuccessListener(snapshot -> {
                loadingDialog.dismiss();
                avatarPictureReference.getDownloadUrl().addOnSuccessListener(uri -> finishUserUpdateUserInformation(uri.toString()));
            });
        }
        else
            finishUserUpdateUserInformation(null);
    }

    private void finishUserUpdateUserInformation(String profileImageUrl) {
        HashMap<String, Object> updateValues = new HashMap<>(4);
        String fullName = fullNameEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        fullName = fullName.length() == 0 ? null : fullName;
        description = description.length() == 0 ? null : description;
        updateValues.put("fullName", fullName);
        updateValues.put("gender", maleRadioButton.isChecked());
        updateValues.put("birthday", currentBirthday);
        updateValues.put("description", description);
        if (profileImageUrl != null)
            updateValues.put("profileImage", profileImageUrl);
        userProfileInfoReference.updateChildren(updateValues).addOnSuccessListener(nothing -> Toast.makeText(getApplicationContext(), R.string.update_user_information_successfully, Toast.LENGTH_SHORT).show());
        finish();
    }
}
