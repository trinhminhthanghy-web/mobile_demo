package com.example.fruitshop.Domain.Entities;


import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "users")
public class User implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String email;
    private String passwordHash;
    @Nullable
    private String imageUrl;
    @Nullable
    private String address;
    @Nullable
    private String birthDay;
    @Nullable
    private String gender;

    public User() {
    }

    public User(String name, String email, String passwordHash, @Nullable String imageUrl) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.imageUrl = imageUrl;
    }

    @Nullable
    public String getAddress() {
        return address;
    }

    public void setAddress(@Nullable String address) {
        this.address = address;
    }

    @Nullable
    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(@Nullable String birthDay) {
        this.birthDay = birthDay;
    }

    @Nullable
    public String getGender() {
        return gender;
    }

    public void setGender(@Nullable String gender) {
        this.gender = gender;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Nullable
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(@Nullable String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
