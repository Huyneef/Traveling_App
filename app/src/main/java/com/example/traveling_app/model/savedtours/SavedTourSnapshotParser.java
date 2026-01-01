package com.example.traveling_app.model.savedtours;

import androidx.annotation.NonNull;

import com.example.traveling_app.common.DatabaseReferences;
import com.example.traveling_app.model.tour.Tour;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class SavedTourSnapshotParser implements SnapshotParser<SavedTour> {
    public static SavedTourSnapshotParser INSTANCE = new SavedTourSnapshotParser();

    private SavedTourSnapshotParser() {

    }

    @NonNull
    @Override
    public SavedTour parseSnapshot(@NonNull DataSnapshot savedTourSnapshot) {
        SavedTour savedTour = new SavedTour();
        savedTour.setTourId(savedTourSnapshot.getKey());
        savedTour.setUsername(savedTourSnapshot.getRef().getParent().getKey());
        DatabaseReferences.TOURS_DATABASE_REF.child(savedTourSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot tourSnapshot) {
                Tour tour = tourSnapshot.getValue(Tour.class);
                if (tour == null) {
                    savedTourSnapshot.getRef().removeValue();
                    return;
                }
                savedTour.setTour(tour);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return savedTour;
    }
}
