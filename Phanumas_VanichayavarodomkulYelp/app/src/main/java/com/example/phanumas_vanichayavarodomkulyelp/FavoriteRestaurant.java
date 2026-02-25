package com.example.phanumas_vanichayavarodomkulyelp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_restaurant")
public class FavoriteRestaurant {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String category;
    private String phone;
    private String address;
    private String imageUrl;
    private String price;
    private float rating;

    public FavoriteRestaurant(String name, String category, String phone, String address, String imageUrl, String price, float rating) {
        this.name = name;
        this.category = category;
        this.phone = phone;
        this.address = address;
        this.imageUrl = imageUrl;
        this.price = price;
        this.rating = rating;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
