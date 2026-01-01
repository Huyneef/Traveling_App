package com.example.traveling_app.model.comment;

import com.google.firebase.database.Exclude;
import java.util.function.Consumer;
import androidx.annotation.Keep;

@Keep
@SuppressWarnings("unused")
public class Comment {

    @Exclude
    private String commentId;
    private String content;
    private String username;

    private String postId;
    private long timeStamp;

    // Bất đồng bộ, cần sử dụng listener để lấy giá trị
    @Exclude
    private String fullName;
    @Exclude
    private String profileImageUrl;
    @Exclude
    private boolean isProfileImageUrlAlready = false;
    @Exclude
    private Consumer<String> onFullNameReady;
    @Exclude
    private Consumer<String> onProfileImageUrlReady;

    @SuppressWarnings("unused")
    public Comment() {

    }

    public Comment(String content, String username, String postId, long timeStamp) {
        this.content = content;
        this.username = username;
        this.postId = postId;
        this.timeStamp = timeStamp;
    }

    @Exclude
    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void getFullNameAsync(Consumer<String> onFullNameReady) {
        if (fullName != null)
            onFullNameReady.accept(fullName);
        else
            this.onFullNameReady = onFullNameReady;

    }

    void setFullName(String fullName) {
        if (fullName == null)
            this.fullName = username;
        else
            this.fullName = fullName;

        if (onFullNameReady != null) {
            onFullNameReady.accept(this.fullName);
            onFullNameReady = null;
        }

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

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

}
