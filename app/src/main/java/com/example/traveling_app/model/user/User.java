package com.example.traveling_app.model.user;
import com.google.firebase.database.Exclude;
import java.io.Serializable;
import androidx.annotation.Keep;

@Keep
public class User implements Serializable {
    @Exclude
    private String username;
    private String fullName;
    private String password;
    private String email;
    private int birthday = DEFAULT_BIRTHDAY;
    private String description;
    private boolean gender;
    private String profileImageUrl;
    private int point = 0;
    private int role;
    private String token;
    @Exclude
    public static int DEFAULT_BIRTHDAY = 20000001;

    public User() {

    }

    public User(String username, String profileImageUrl, int role) {
        this.username = username;
        this.profileImageUrl = profileImageUrl;
        this.role=role;
    }

    public User(String username, String fullName, String password, String description, int birthday, boolean gender, String profileImageUrl) {
        this.username = username;
        this.password = password;
        this.birthday = birthday;
        this.description = description;
        this.fullName = fullName;
        this.gender = gender;
        this.profileImageUrl = profileImageUrl;
    }

    public User(String username, String fullName, String password, String description, int birthday, boolean gender, String profileImageUrl, String email) {
        this.password = password;
        this.birthday = birthday;
        this.description = description;
        this.fullName = fullName;
        this.gender = gender;
        this.profileImageUrl = profileImageUrl;
        this.email=email;
    }
    @Exclude
    public String getUsername() {
        return username;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getBirthday() {
        return birthday;
    }

    public void setBirthday(int birthday) {
        this.birthday = birthday;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getProfileImage() {
        return profileImageUrl;
    }

    public void setProfileImage(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "username = " + username + ", fullName = " + fullName + ", birthday = " + birthday + ", description = " + description + ", gender = " + gender;
    }

}

