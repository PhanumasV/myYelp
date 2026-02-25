package com.example.phanumas_vanichayavarodomkulyelp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YelpApiInterface {
    @GET("businesses/search")
    Call<YelpSearchResult> searchRestaurants(
            @Query("term") String term,
            @Query("location") String location,
            @Query("sort_by") String sortBy
    );
}

