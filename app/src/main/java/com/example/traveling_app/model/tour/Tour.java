package com.example.traveling_app.model.tour;

import com.google.firebase.database.Exclude;
import java.util.Date;
import androidx.annotation.Keep;

@Keep
public class Tour {
    @Exclude
    private String id;
    private String name;
    private String address;
    private String phone;
    private String mainImageUrl;
    private String content;
    private double numStar;
    private double price;

    private double salePrice;

    private int numComment;

    private int numBooking;

    private String createdTime;

    private String dateStart;
    private String dateEnd;

    private String type;

    public Tour() {
        // khởi tạo để lấy dữ liệu từ firebase về
    }

    public Tour(String name, String address, String phone, String mainImageUrl, String content, int numStar, double price, double salePrice, int numComment, int numBooking, String id,String type, String dateStart, String dateEnd, String createdTime) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.mainImageUrl = mainImageUrl;
        this.content = content;
        this.numStar = numStar;
        this.price = price;
        this.salePrice = salePrice;
        this.numComment = numComment;
        this.numBooking = numBooking;
        this.id=id;
        this.type=type;
        this.dateEnd=dateEnd;
        this.dateStart=dateStart;
        this.createdTime=createdTime;
    }

    public Tour(String name, String address, String phone, String content, String dateStart, String dateEnd, double price, double salePrice, String createdTime,String mainImageUrl, String id,String type) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.content = content;
        this.price = price;
        this.salePrice = salePrice;
        this.createdTime = createdTime;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.mainImageUrl=mainImageUrl;
        this.id=id;
        this.type=type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public String getName() {
        return name;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl;
    }

    public double getNumStar() {
        return numStar;
    }

    public void setNumStar(double numStar) {
        this.numStar = numStar;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNumComment() {
        return numComment;
    }

    public void setNumComment(int numComment) {
        this.numComment = numComment;
    }

    public int getNumBooking() {
        return numBooking;
    }

    public void setNumBooking(int numBooking) {
        this.numBooking = numBooking;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    @Override
    public String toString() {
        return "Tour{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", mainImageUrl='" + mainImageUrl + '\'' +
                ", content='" + content + '\'' +
                ", numStar=" + numStar +
                ", price=" + price +
                ", salePrice=" + salePrice +
                ", numComment=" + numComment +
                ", numBooking=" + numBooking +
                ", createdTime=" + createdTime +
                ", dateStart=" + dateStart +
                ", dateEnd=" + dateEnd +
                '}';
    }
}
