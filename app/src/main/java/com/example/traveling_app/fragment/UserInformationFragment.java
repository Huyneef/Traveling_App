package com.example.traveling_app.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.traveling_app.R;
import com.example.traveling_app.common.Constants;
import com.example.traveling_app.common.DatabaseReferences;
import com.example.traveling_app.model.user.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.Calendar;

public class UserInformationFragment extends Fragment {
    private DatabaseReference userProfileRef;
    private ValueEventListener valueEventListener;
    private String profileId;

    @Override
    @SuppressWarnings("deprecation")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileId = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("username", Constants.DEFAULT_USERNAME);
        userProfileRef = DatabaseReferences.USER_DATABASE_REF.child(profileId);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_infomation, container, false);
        TextView usernameTextView = rootView.findViewById(R.id.usernameTextView),
                genderTextView = rootView.findViewById(R.id.genderTextView),
                ageTextView = rootView.findViewById(R.id.ageTextView);

        if (valueEventListener != null)
            userProfileRef.removeEventListener(valueEventListener);
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                user.setUsername(snapshot.getKey());
                usernameTextView.setText(user.getUsername());
                if (user.getGender()) {
                    genderTextView.setText(R.string.male);
                    genderTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.baseline_male_24, 0, 0, 0);
                } else {
                    genderTextView.setText(R.string.female);
                    genderTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.baseline_female_24, 0, 0, 0);
                }
                ageTextView.setText(getContext().getString(R.string.age_count, Calendar.getInstance().get(Calendar.YEAR) - (user.getBirthday() / 10000)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Nothing
            }
        };
        userProfileRef = DatabaseReferences.USER_DATABASE_REF.child(profileId);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        userProfileRef.addValueEventListener(valueEventListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        userProfileRef.removeEventListener(valueEventListener);
    }

}