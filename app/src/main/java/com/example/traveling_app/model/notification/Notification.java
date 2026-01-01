package com.example.traveling_app.model.notification;
import com.google.firebase.database.Exclude;
import java.util.function.Consumer;
import androidx.annotation.Keep;

@Keep
public class Notification {
    @Exclude
    private String id;
    private String sendFrom;
    private String sendTo;
    private String content;
    private long time;
    private boolean read;

    // Bất đổng bộ, sử dụng callback để lấy giá trị
    @Exclude
    private boolean isProfileImageUrlAlready = false;
    @Exclude
    private String profileImageUrl;
    @Exclude
    private Consumer<String> onProfileImageUrlReady;


    @SuppressWarnings("unused")
    public Notification() {

    }

    public Notification(String sendFrom, String sendTo, String content, long time) {
        this.sendFrom = sendFrom;
        this.sendTo = sendTo;
        this.content = content;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSendFrom() {
        return sendFrom;
    }

    public String getSendTo() {
        return sendTo;
    }

    public String getContent() {
        return content;
    }

    public long getTime() {
        return time;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public void getProfileImageUrlAsync(Consumer<String> onProfileImageUrlReady) {
        if (!isProfileImageUrlAlready)
            this.onProfileImageUrlReady = onProfileImageUrlReady;
        else
            onProfileImageUrlReady.accept(profileImageUrl);
    }

    void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
        isProfileImageUrlAlready = true;
        if (onProfileImageUrlReady != null) {
            onProfileImageUrlReady.accept(profileImageUrl);
            onProfileImageUrlReady = null;
        }
    }

}
