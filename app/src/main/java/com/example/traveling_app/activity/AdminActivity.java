package com.example.traveling_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.traveling_app.R;
import com.example.traveling_app.adapter.AdminTourAdapter;
import com.example.traveling_app.common.DataCallback;
import com.example.traveling_app.model.tour.Tour;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AdminActivity extends AppCompatActivity {
    private ListView tour_admin_lv;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference ref=database.getReference("tours");
    AdminTourAdapter adapter;
    Button btnTk;
    HashMap<String, Tour> tours=new HashMap<>();
    TextView txtTk;

    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        tour_admin_lv=findViewById(R.id.tour_admin_lv);
        btnTk=findViewById(R.id.admin_searchBtn);
        txtTk=findViewById(R.id.admin_searchEdt);
        btnTk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Tour> test=new ArrayList<>();
                for (Tour tour: tours.values()){
                    if (tour.getName().startsWith(txtTk.getText().toString())){
                        test.add(tour);
                    }
                }
                adapter=new AdminTourAdapter(test, AdminActivity.this);
                tour_admin_lv.setAdapter(adapter);
            }
        });

        // dùng callback
        getData(new DataCallback() {
            @Override
            public void onDataLoaded(List<Tour> tours) {
                adapter=new AdminTourAdapter(tours, AdminActivity.this);
                tour_admin_lv.setAdapter(adapter);
            }

            @Override
            public void onTourLoaded(Tour Tour) {

            }

        });

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);
        actionBar.setTitle("Admin");
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            startActivity(new Intent(AdminActivity.this, MainActivity.class));
            return true;
        }
        else if (item.getItemId()==R.id.create_tour){
            Intent intent=new Intent(this, AdminCreateActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void getData(final DataCallback callback){
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Tour tour=snapshot.getValue(Tour.class);
                tour.setId(snapshot.getKey());
                String id=snapshot.getKey();
                tours.put(id, tour);
                callback.onDataLoaded(new ArrayList<>(tours.values()));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String idTourModified=snapshot.getKey();
                Tour tourModified=snapshot.getValue(Tour.class);
                tourModified.setId(snapshot.getKey());
                tours.put(idTourModified, tourModified);
                callback.onDataLoaded(new ArrayList<>(tours.values()));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String idTourDeleted=snapshot.getKey();
                tours.remove(idTourDeleted);
                callback.onDataLoaded(new ArrayList<>(tours.values()));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}