package com.example.traveling_app.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.bumptech.glide.Glide;
import com.example.traveling_app.R;
import com.example.traveling_app.common.CurrentUser;
import com.example.traveling_app.model.tour.Tour;
import com.example.traveling_app.adapter.ViewPagerAdapter;
import com.example.traveling_app.fragment.ReviewFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.DecimalFormat;

public class DetailActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    String idTour,phone;
    Tour tour;
    DecimalFormat df = new DecimalFormat("#.##");
    private ImageView tour_detail_ava, tour_detail_ava1, tour_detail_ava2, tour_detail_ava3, tour_detail_ava4;
    private TextView tour_detail_tit, tour_detail_add, tour_detail_price, tour_detail_star, tour_detail_com;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("tours");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tour_detail_ava = findViewById(R.id.tour_detail_ava);
        tour_detail_ava1 = findViewById(R.id.tour_detail_ava1);
        tour_detail_ava2 = findViewById(R.id.tour_detail_ava2);
        tour_detail_ava3 = findViewById(R.id.tour_detail_ava3);
        tour_detail_ava4 = findViewById(R.id.tour_detail_ava4);
        tour_detail_tit = findViewById(R.id.tour_detail_tit);
        tour_detail_add = findViewById(R.id.tour_detail_add);
        tour_detail_price = findViewById(R.id.tour_detail_price);
        tour_detail_star = findViewById(R.id.tour_detail_star);
        tour_detail_com = findViewById(R.id.tour_detail_com);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        idTour= getIntent().getStringExtra("id");
        bindingData();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        // nút Back
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);
        actionBar.setTitle("");
    }

    // tạo menu cho action bar
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);

        MenuItem item = menu.findItem(R.id.menu_item_love);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("saved_tours").child(CurrentUser.getCurrentUser().getUsername());
        DatabaseReference tourRef = ref.child(idTour);
        tourRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    runOnUiThread(() -> item.setIcon(R.drawable.heart_solid_active));
                 else
                    runOnUiThread(() -> item.setIcon(R.drawable.main_heart_bar_regular));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu có
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_item_call) {
            Uri uri = Uri.parse("tel:" + phone);
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_CALL, uri);
                startActivity(intent);
            } else {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
            }
        } else if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        else if (item.getItemId() == R.id.menu_item_love) {
            DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("saved_tours").child(CurrentUser.getCurrentUser().getUsername());
            ref.child(idTour).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        ref.child(idTour).removeValue();
                        item.setIcon(R.drawable.main_heart_bar_regular);
                    }
                    else {
                        ref.child(idTour).setValue(true);
                        item.setIcon(R.drawable.heart_solid_active);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void bindingData() {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        ref.child(idTour).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    tour = snapshot.getValue(Tour.class);
                    tour_detail_tit.setText(tour.getName());
                    tour_detail_add.setText(tour.getAddress());
                    tour_detail_price.setText(formatter.format(tour.getSalePrice()) + " VNĐ");

                    Glide.with(getApplicationContext()).load(tour.getMainImageUrl()).into(tour_detail_ava);
                    Glide.with(getApplicationContext()).load(tour.getMainImageUrl()).into(tour_detail_ava1);
                    Glide.with(getApplicationContext()).load(tour.getMainImageUrl()).into(tour_detail_ava2);
                    Glide.with(getApplicationContext()).load(tour.getMainImageUrl()).into(tour_detail_ava3);
                    Glide.with(getApplicationContext()).load(tour.getMainImageUrl()).into(tour_detail_ava4);

                    tour_detail_star.setText(df.format(tour.getNumStar()).replace(',', '.'));
                    tour_detail_com.setText(String.valueOf(tour.getNumComment()));

                    phone=tour.getPhone();
            }
        }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void onStarClick(View view) {
        int clickedStar = Integer.parseInt(view.getTag().toString());
        // Đặt trạng thái cho các ngôi sao
        for (int i = 1; i <= 5; i++) {
            ImageView starImageView = findViewById(getResources().getIdentifier("star" + i, "id", getPackageName()));
            starImageView.setImageResource(i <= clickedStar ? R.drawable.main_star_solid : R.drawable.main_star_disable);
        }
        // truyền giá trị đến fragment
        Bundle bundle = new Bundle();
        bundle.putInt("numStar", clickedStar);
        ReviewFragment fragment = new ReviewFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

}