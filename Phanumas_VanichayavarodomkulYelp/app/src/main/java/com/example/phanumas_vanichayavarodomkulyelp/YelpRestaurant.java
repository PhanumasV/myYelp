package com.example.phanumas_vanichayavarodomkulyelp;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class YelpRestaurant {

    private String name;
    private String price;
    private double rating;
    private String phone;

    @SerializedName("image_url")
    private String imageUrl;

    private List<Category> categories;
    private Location location;

    public String getName() { return name; }
    public String getPrice() { return price; }
    public double getRating() { return rating; }
    public String getPhone() { return phone; }
    public String getImageUrl() { return imageUrl; }

    public List<Category> getCategories() { return categories; }
    public Location getLocation() { return location; }

    public static class Category {
        private String title;
        public String getTitle() { return title; }
    }

    public static class Location {
        @SerializedName("address1")
        private String address1;
        private String city;
        private String state;

        public String getAddress1() { return address1; }
        public String getCity() { return city; }
        public String getState() { return state; }
    }
}
