package com.example.traveling_app.model.post;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.google.firebase.database.Exclude;
import java.util.function.Consumer;
import androidx.annotation.Keep;

@Keep
public class Post implements Parcelable {
    @Exclude
    private String idPost;
    private String postImgUrl;
    private String username;
    private String title;
    private int commentCount;
    private long time;

    // Bất đồng bộ, sử dụng listener để lấy giá trị
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

    public Post() {
    }

    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<>() {

        @Override
        public Post createFromParcel(Parcel source) {
            String idPost = source.readString();
            String postImgUrl = source.readString();
            String username = source.readString();
            String title = source.readString();
            int commentCount = source.readInt();
            long time = source.readLong();
            String fullName = source.readString();
            String profileImageUrl = source.readString();
            Post post = new Post(idPost, postImgUrl, username, title, commentCount, time);
            post.setProfileImageUrl(profileImageUrl);
            post.setFullName(fullName);
            return post;
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public Post(String idPost, String postImgUrl, String username, String title, long time) {
        this.idPost = idPost;
        this.postImgUrl = postImgUrl;
        this.username = username;
        this.title = title;
        this.commentCount = 0;
        this.time = time;
    }

    public Post(String idPost, String postImgUrl, String username, String title, int commentCount, long time) {
        this.idPost = idPost;
        this.postImgUrl = postImgUrl;
        this.username = username;
        this.title = title;
        this.commentCount = commentCount;
        this.time = time;
    }

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }

    public String getPostImgUrl() {
        return postImgUrl;
    }

    public void setPostImgUrl(String postImgUrl) {
        this.postImgUrl = postImgUrl;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCommentCount() {
        return commentCount;
    }

    void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(idPost);
        dest.writeString(postImgUrl);
        dest.writeString(username);
        dest.writeString(title);
        dest.writeInt(commentCount);
        dest.writeLong(time);
        dest.writeString(fullName);
        dest.writeString(profileImageUrl);
    }



    @Override
    public String toString() {
        return "Post{" +
                "idPost='" + idPost + '\'' +
                ", postImgUrl='" + postImgUrl + '\'' +
                ", username='" + username + '\'' +
                ", title='" + title + '\'' +
                ", time=" + time +
                ", fullName='" + fullName + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", isProfileImageUrlAlready=" + isProfileImageUrlAlready +
                ", onFullNameReady=" + onFullNameReady +
                ", onProfileImageUrlReady=" + onProfileImageUrlReady +
                '}';
    }
}
