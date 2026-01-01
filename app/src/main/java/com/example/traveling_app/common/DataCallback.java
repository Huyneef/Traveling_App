package com.example.traveling_app.common;

import com.example.traveling_app.model.tour.Tour;

import java.util.List;

public interface DataCallback {
    void onDataLoaded(List<Tour> tours);

    void onTourLoaded(Tour Tour);

}
