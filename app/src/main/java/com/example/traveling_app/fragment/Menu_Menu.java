package com.example.traveling_app.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.traveling_app.activity.MainActivity;
import com.example.traveling_app.R;
import com.example.traveling_app.common.Constants;
import com.example.traveling_app.common.DatabaseReferences;
import com.example.traveling_app.adapter.MenuListAdapter;
import com.example.traveling_app.model.other.MenuSectionItem;
import com.example.traveling_app.model.user.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class Menu_Menu extends Fragment {

    private TextView usernameTextView, statusTextView;
    private ImageView userAvatarImageView;
    private DatabaseReference userRef;

    private final ValueEventListener infoListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            User user = snapshot.getValue(User.class);
            user.setUsername(snapshot.getKey());
            String avatarImageUrl = user.getProfileImage();
            String name = user.getFullName() == null ? user.getUsername() : user.getFullName();
            String description = user.getDescription() == null ? getContext().getString(R.string.online) : user.getDescription();
            if (avatarImageUrl == null)
                userAvatarImageView.setImageResource(R.drawable.user_profile_icon);
            else
                Glide.with(userAvatarImageView).load(avatarImageUrl).circleCrop().into(userAvatarImageView);
            usernameTextView.setText(name);
            statusTextView.setText(description);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu__menu, container, false);
        MenuListAdapter menuListAdapter = new MenuListAdapter(Constants.MENU_SECTION_ITEMS);
        ListView listView = view.findViewById(R.id.menuListItem);
        userRef = DatabaseReferences.USER_DATABASE_REF.child(PreferenceManager.getDefaultSharedPreferences(getContext()).getString("username", Constants.DEFAULT_USERNAME));
        usernameTextView = view.findViewById(R.id.usernameTextView);
        statusTextView = view.findViewById(R.id.statusTextView);
        userAvatarImageView = view.findViewById(R.id.userAvatar);
        listView.setAdapter(menuListAdapter);
        listView.setOnItemClickListener(
                (AdapterView<?> parent, View v, int position, long id) -> {
                    MenuSectionItem menuSectionItem = (MenuSectionItem) parent.getItemAtPosition(position);
                    if (menuSectionItem.getActivityClass() == null)
                        Toast.makeText(getContext(), getString(R.string.not_implemented), Toast.LENGTH_SHORT).show();
                    else
                        startActivity(new Intent(getContext(), menuSectionItem.getActivityClass()));
                }
        );

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        userRef.addValueEventListener(infoListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        userRef.removeEventListener(infoListener);
    }
}