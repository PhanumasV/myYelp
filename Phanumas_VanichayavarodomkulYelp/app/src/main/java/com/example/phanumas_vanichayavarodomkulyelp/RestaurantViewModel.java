package com.example.phanumas_vanichayavarodomkulyelp;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class RestaurantViewModel extends AndroidViewModel {

    private AppDatabase db;
    private LiveData<List<FavoriteRestaurant>> allFavorites;

    public RestaurantViewModel(Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
        allFavorites = db.favoriteDao().getAllFavorites();
    }

    public LiveData<List<FavoriteRestaurant>> getAllFavorites() {
        return allFavorites;
    }
}
