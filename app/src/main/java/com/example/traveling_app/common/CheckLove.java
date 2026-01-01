package com.example.traveling_app.common;

import android.media.Image;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.traveling_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CheckLove {
    static DatabaseReference ref= FirebaseDatabase.getInstance().getReference();
    public static void isLoved(String username, String idTour, ImageView imageView){
        ref.child("saved_tours").child(username).child(idTour).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    imageView.setImageResource(R.drawable.heart_solid_active);
                } else {
                    if (imageView.getTag().toString().equals("hot_tour_love"))
                        imageView.setImageResource(R.drawable.main_heart_regular_white);
                    else
                        imageView.setImageResource(R.drawable.main_heart_bar_regular);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static void saveData(String username, String idTour,ImageView imageView){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.child("saved_tours").child(username).child(idTour).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            if (imageView.getTag().toString().equals("hot_tour_love"))
                                imageView.setImageResource(R.drawable.main_heart_regular_white);
                            else
                                imageView.setImageResource(R.drawable.main_heart_bar_regular);
                            ref.child("saved_tours").child(username).child(idTour).removeValue();
                        }
                        else{
                            imageView.setImageResource(R.drawable.heart_solid_active);
                            ref.child("saved_tours").child(username).child(idTour).setValue(true);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }


}
