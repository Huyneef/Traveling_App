package com.example.traveling_app.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.traveling_app.activity.AdminCreateActivity;
import com.example.traveling_app.R;
import com.example.traveling_app.common.ImageLoader;
import com.example.traveling_app.model.tour.Tour;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdminTourAdapter extends BaseAdapter {

    DatabaseReference ref= FirebaseDatabase.getInstance().getReference("tours");
    private List<Tour> tours;
    private Activity activity;

    public AdminTourAdapter(List<Tour> tours, Activity activity) {
        this.tours = tours;
        this.activity=activity;
    }

    @Override
    public int getCount() {
        return tours==null?0:tours.size();
    }

    @Override
    public Object getItem(int position) {
        return tours.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=activity.getLayoutInflater();
        convertView =inflater.inflate(R.layout.admin_tour_item,null);

        //Bind sữ liệu phần tử vào View
        Tour product = (Tour) getItem(position);
        ((TextView) convertView.findViewById(R.id.title_tour_admin)).setText(tours.get(position).getName());
        ((TextView) convertView.findViewById(R.id.address_tour_admin)).setText(tours.get(position).getAddress());
        ((TextView) convertView.findViewById(R.id.time_tour_admin)).setText("Thời gian: " + tours.get(position).getDateStart() + " - "+ tours.get(position).getDateEnd());
        ImageView menu_tour=convertView.findViewById(R.id.menu_tour_admin);

        PopupMenu popupMenu=new PopupMenu(activity, menu_tour);
        popupMenu.inflate(R.menu.menu_tour_admin);
        menu_tour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
            }
        });


        ImageLoader.loadImage(tours.get(position).getMainImageUrl(), ((ImageView) convertView.findViewById(R.id.avatar_tour_admin)));


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.update_tour_admin){
                    Intent intent=new Intent(activity, AdminCreateActivity.class);
                    intent.putExtra("id",tours.get(position).getId());
                    activity.startActivity(intent);
                }
                if(item.getItemId()==R.id.delete_tour_admin){
                    // xóa dữ liệu trên firebase
                    ref.child(tours.get(position).getId()).removeValue();
                    tours.remove(position);
                    notifyDataSetChanged();
                }

                return true;
            }
        });

        return convertView;
    }
}
