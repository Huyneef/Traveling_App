package com.example.traveling_app.model.tour;

import com.google.firebase.database.Exclude;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;import androidx.annotation.Keep;

@Keep

public class Review implements Comparable<Review>{

    @Exclude
    private String nameReviewer;
    @Exclude
    private String avatarReviewer;
    private int rate;
    private String time;
    private String content;
    private List<String> images;

    public Review() {
    }

    public Review(int rate, String content, String time, List<String> images) {
        this.rate = rate;
        this.time = time;
        this.content = content;
        this.images = images;
    }

    public Review(String nameReviewer, String avatarReviewer, int rate, String time, String content, List<String> images) {
        this.nameReviewer = nameReviewer;
        this.avatarReviewer = avatarReviewer;
        this.rate = rate;
        this.time = time;
        this.content = content;
        this.images = images;
    }

    public String getNameReviewer() {
        return nameReviewer;
    }

    public void setNameReviewer(String nameReviewer) {
        this.nameReviewer = nameReviewer;
    }

    public String getAvatarReviewer() {
        return avatarReviewer;
    }

    public void setAvatarReviewer(String avatarReviewer) {
        this.avatarReviewer = avatarReviewer;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "Review{" +
                "nameReviewer='" + nameReviewer + '\'' +
                ", avatarReviewer='" + avatarReviewer + '\'' +
                ", rate=" + rate +
                ", time='" + time + '\'' +
                ", content='" + content + '\'' +
                ", images=" + images +
                '}';
    }

    @Override
    public int compareTo(Review other) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        try {
            return sdf.parse(this.time).compareTo(sdf.parse(other.getTime()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
