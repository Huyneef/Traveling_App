package com.example.traveling_app.model.other;

import androidx.lifecycle.ViewModel;

import com.example.traveling_app.model.tour.Review;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {
    private int numRate=0;
    private List<Review> reviews=new ArrayList<>();

    public int getNumRate() {
        return numRate;
    }

    public void setNumRate(int numRate) {
        this.numRate = numRate;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}