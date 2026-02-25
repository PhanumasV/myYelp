package com.example.phanumas_vanichayavarodomkulyelp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Insert
    void insert(FavoriteRestaurant favoriteRestaurant);

    @Query("SELECT * FROM favorite_restaurant")
    LiveData<List<FavoriteRestaurant>> getAllFavorites();
}
