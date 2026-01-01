package com.example.traveling_app.model.savedtours;

import com.example.traveling_app.model.tour.Tour;
import com.google.firebase.database.Exclude;
import java.util.function.Consumer;
import androidx.annotation.Keep;

@Keep
public class SavedTour {

    @Exclude
    private Tour tour;
    @Exclude
    private String username;

    private String tourId;
    private Consumer<Tour> onTourReady;

    @SuppressWarnings("unused")
    public SavedTour() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Exclude
    public String getTourId() {
        return tourId;
    }

    void setTourId(String id) {
        this.tourId = id;
    }

    public void getTourAsync(Consumer<Tour> onTourReady) {
        if (tour == null)
            this.onTourReady = onTourReady;
        else if (onTourReady != null)
            onTourReady.accept(tour);

    }
    void setTour(Tour tour) {
        this.tour = tour;
        if (onTourReady != null) {
            onTourReady.accept(tour);
            onTourReady = null;
        }
    }
}

