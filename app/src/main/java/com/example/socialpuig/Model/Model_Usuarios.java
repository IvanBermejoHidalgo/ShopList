package com.example.socialpuig.Model;


import java.io.Serializable;

public class Model_Usuarios implements Serializable {

    public String email, name, profileImage, telefono, uid, userType;
    long timestamp;

    public Model_Usuarios() {
    }

    public Model_Usuarios(String email, String name, String profileImage, String telefono, String uid, String userType, long timestamp) {
        this.email = email;
        this.name = name;
        this.profileImage = profileImage;
        this.telefono = telefono;
        this.uid = uid;
        this.userType = userType;
        this.timestamp = timestamp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
