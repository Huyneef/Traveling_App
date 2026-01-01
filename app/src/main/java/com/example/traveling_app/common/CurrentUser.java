package com.example.traveling_app.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.example.traveling_app.model.user.User;
public class CurrentUser {
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    public CurrentUser(Context context, User user) {
        if (preferences == null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
            editor = preferences.edit();
        }

        editor.putString("username", user.getUsername());
//        editor.putString("imageUrl", user.getProfileImage());
        editor.putInt("role", user.getRole());
        editor.apply();
    }

    public static User getCurrentUser() {
        // Kiểm tra nếu preferences chưa được khởi tạo thì trả về giá trị mặc định
        if (preferences == null) {
            return new User("", "",0); // hoặc trả về giá trị mặc định phù hợp
        }

        String email = preferences.getString("username", "");
        String imageUrl = preferences.getString("imageUrl", "");
        int role = preferences.getInt("role", 0);
        return new User(email, imageUrl,role);

    }

    public static void remove() {
        // Kiểm tra nếu preferences chưa được khởi tạo thì không làm gì cả
        if (preferences != null) {
            editor.clear();
            editor.apply();
        }
    }
}