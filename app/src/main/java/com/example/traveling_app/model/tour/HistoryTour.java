package com.example.traveling_app.model.tour;
import androidx.annotation.Keep;

@Keep
public class HistoryTour {
    private int price;
    private String startDate;
    private String endDate;
    private String idtour;
    private String username;

    public HistoryTour() {
    }

    public HistoryTour(int price, String startDate, String endDate, String idtour, String username) {
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.idtour = idtour;
        this.username = username;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getIdtour() {
        return idtour;
    }

    public void setIdtour(String idtour) {
        this.idtour = idtour;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}