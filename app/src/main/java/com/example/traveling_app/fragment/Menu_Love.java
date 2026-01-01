package com.example.traveling_app.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.traveling_app.R;
import com.example.traveling_app.common.Constants;
import com.example.traveling_app.common.DatabaseReferences;
import com.example.traveling_app.model.savedtours.SavedTour;
import com.example.traveling_app.model.savedtours.SavedTourAdapter;
import com.example.traveling_app.model.savedtours.SavedTourSnapshotParser;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;

public class Menu_Love extends Fragment {

    private SavedTourAdapter savedTourAdapter;
    private ImageView back_detail_blog;
    @Override
    @SuppressWarnings("deprecation")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View rootView = inflater.inflate(R.layout.fragment_menu__love, container, false);
        TextView itemCountTextView = rootView.findViewById(R.id.totalCountTextView);
        back_detail_blog=rootView.findViewById(R.id.back_detail_blog);
        back_detail_blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHomeMenu();
            }
        });
        RecyclerView savedTourRecyclerView = rootView.findViewById(R.id.love_rcv);
        String profileId = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("username", Constants.DEFAULT_USERNAME);
        Query query = DatabaseReferences.USER_SAVED_TOURS_DATABASE_REF.child(profileId).orderByKey();
        FirebaseRecyclerOptions<SavedTour> options = new FirebaseRecyclerOptions.Builder<SavedTour>().setQuery(query, SavedTourSnapshotParser.INSTANCE).build();
        savedTourAdapter = new SavedTourAdapter(options);
        savedTourRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        savedTourRecyclerView.setAdapter(savedTourAdapter);
        savedTourAdapter.setOnDataChanged(() -> itemCountTextView.setText(getContext().getString(R.string.total_placeholder, savedTourAdapter.getItemCount())));
        savedTourAdapter.startListening();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        savedTourAdapter.stopListening();
    }



    public void goHomeMenu(){
        ViewPager viewPager= getActivity().findViewById(R.id.view_pager_main);
        viewPager.setCurrentItem(0);
    }
}