package com.example.phanumas_vanichayavarodomkulyelp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.core.content.ContextCompat;




import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFavorites;
    private com.example.phanumas_vanichayavarodomkulyelp.FavoriteAdapter favoritesAdapter;
    private RestaurantViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        recyclerViewFavorites = findViewById(R.id.recyclerViewFavorites);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewFavorites.setLayoutManager(layoutManager);

        favoritesAdapter = new FavoriteAdapter(null);
        recyclerViewFavorites.setAdapter(favoritesAdapter);

        // Add divider decoration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerViewFavorites.getContext(), layoutManager.getOrientation());
        //dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.custom_divider));
        recyclerViewFavorites.addItemDecoration(dividerItemDecoration);

        // Get the ViewModel and observe favorite restaurants
        viewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);
        viewModel.getAllFavorites().observe(this, new Observer<List<FavoriteRestaurant>>() {
            @Override
            public void onChanged(List<FavoriteRestaurant> favoriteRestaurants) {
                favoritesAdapter.updateFavorites(favoriteRestaurants);
            }
        });
    }

}
