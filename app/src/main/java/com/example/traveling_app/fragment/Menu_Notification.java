package com.example.traveling_app.fragment;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.traveling_app.R;
import com.example.traveling_app.common.Constants;
import com.example.traveling_app.model.notification.Notification;
import com.example.traveling_app.model.notification.NotificationAdapter;
import com.example.traveling_app.model.notification.NotificationParser;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Menu_Notification extends Fragment {

    private RecyclerView noti_rcv;
    private NotificationAdapter recentAdapter;
    private View view;
    private ImageView back_detail_blog;
    @Override
    @SuppressWarnings("deprecation")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Query query = FirebaseDatabase.getInstance().getReference("/notifications").orderByChild("sendTo").equalTo(PreferenceManager.getDefaultSharedPreferences(getContext()).getString("username", Constants.DEFAULT_USERNAME));
        FirebaseRecyclerOptions<Notification> options = new FirebaseRecyclerOptions.Builder<Notification>().setQuery(query, new NotificationParser()).build();
        view = inflater.inflate(R.layout.fragment_menu__notification, container, false);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        noti_rcv = view.findViewById(R.id.noti_rcv);
        back_detail_blog=view.findViewById(R.id.back_detail_blog);
        back_detail_blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHomeMenu();
            }
        });

        // Thay đổi LayoutManager để đảo ngược thứ tự hiển thị
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        noti_rcv.setLayoutManager(layoutManager);

        recentAdapter = new NotificationAdapter(options, getContext());
        noti_rcv.addItemDecoration(itemDecoration);
        noti_rcv.setAdapter(recentAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        recentAdapter.startListening();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recentAdapter.stopListening();
    }

    public void goHomeMenu(){
        ViewPager viewPager= getActivity().findViewById(R.id.view_pager_main);
        viewPager.setCurrentItem(0);
    }
}