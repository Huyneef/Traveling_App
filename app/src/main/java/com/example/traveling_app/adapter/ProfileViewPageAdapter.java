package com.example.traveling_app.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.traveling_app.R;
import com.example.traveling_app.fragment.UserPostFragment;
import com.example.traveling_app.fragment.UserInformationFragment;

public class ProfileViewPageAdapter extends FragmentStateAdapter {

    public ProfileViewPageAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new UserPostFragment();
            case 1:
                return new UserInformationFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public int getTabName(int position) {
        switch (position) {
            case 0:
                return R.string.post;
            case 1:
                return R.string.user_infomation;
            default:
                return 0;
        }
    }
}
