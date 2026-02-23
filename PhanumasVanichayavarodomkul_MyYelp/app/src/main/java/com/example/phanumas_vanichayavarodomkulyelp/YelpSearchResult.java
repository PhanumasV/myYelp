package com.example.phanumas_vanichayavarodomkulyelp;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class YelpSearchResult {
    @SerializedName("businesses")
    public List<YelpRestaurant> businesses;
}
