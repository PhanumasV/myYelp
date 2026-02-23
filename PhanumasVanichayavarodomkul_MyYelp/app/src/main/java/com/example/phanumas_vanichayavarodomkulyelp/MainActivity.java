package com.example.phanumas_vanichayavarodomkulyelp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.navigation.NavigationView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.core.content.ContextCompat;
import android.content.res.Configuration;

public class MainActivity extends AppCompatActivity {

    private RestaurantAdapter restaurantAdapter;
    private Spinner sortSpinner;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private RestaurantViewModel viewModel;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        sortSpinner = findViewById(R.id.sortSpinner);

        // Create ArrayAdapter for Spinner with options
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"None", "Rating", "Price"}
        ) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;

                int nightModeFlags =
                        parent.getContext().getResources().getConfiguration().uiMode
                                & Configuration.UI_MODE_NIGHT_MASK;

                if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                    textView.setTextColor(Color.WHITE); // Night mode
                } else {
                    textView.setTextColor(Color.BLACK); // Day mode
                }

                return view;
            }


            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;
                textView.setTextColor(Color.BLACK); // Set selected item text color to black
                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);

        // Set listener for spinner selection
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, android.view.View selectedItemView, int position, long id) {
                String sortBy = parentView.getItemAtPosition(position).toString();
                restaurantAdapter.sortBy(sortBy);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        NavigationView navView = findViewById(R.id.nav_view);

        // Initialize Room database
        appDatabase = AppDatabase.getInstance(this);

        // RecyclerView setup
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        restaurantAdapter = new RestaurantAdapter(new ArrayList<>(), this::showAddToFavoritesDialog, appDatabase, this);
        recyclerView.setAdapter(restaurantAdapter);

        // Add divider decoration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        // ViewModel setup for favorites (if needed)
        viewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);

        // SearchView setup
        searchView = findViewById(R.id.searchView);
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = searchView.findViewById(id);
        textView.setTextColor(Color.BLACK);
        textView.setHintTextColor(Color.GRAY);

        // SearchView listener for searching restaurants
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null && !query.isEmpty()) {
                    fetchRestaurants(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Drawer navigation for favorites and search
        navView.setNavigationItemSelectedListener(menuItem -> {
            // Handle Favorites navigation
            if (menuItem.getItemId() == R.id.nav_favorites) {
                Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
                startActivity(intent);
            }

            // Handle Search navigation
            if (menuItem.getItemId() == R.id.nav_search) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                // Clear previous activities in the stack if needed
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();  // Optional: If you want to close the current activity before opening the main activity
            }

            return true;
        });


        // Initial fetch of restaurants (example: "ramen")
        fetchRestaurants("ramen");
    }

    // Function to fetch restaurants based on search term
    private void fetchRestaurants(String term) {
        YelpApiInterface apiService = YelpApiClient.getClient().create(YelpApiInterface.class);

        // Use "best_match" or "rating" or "review_count" for sortBy
        Call<YelpSearchResult> call = apiService.searchRestaurants(term, "Toronto", "rating");

        call.enqueue(new Callback<YelpSearchResult>() {
            @Override
            public void onResponse(Call<YelpSearchResult> call, Response<YelpSearchResult> response) {
                Log.d("API_RESPONSE", "Code: " + response.code());
                if (response.isSuccessful()) {
                    YelpSearchResult result = response.body();
                    if (result != null && result.businesses != null) {
                        Log.d("API_SUCCESS", "Found " + result.businesses.size() + " restaurants");
                        restaurantAdapter.updateData(result.businesses);
                    } else {
                        Log.e("API_ERROR", "Response body or businesses is null");
                    }
                } else {
                    Log.e("API_ERROR", "Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<YelpSearchResult> call, Throwable t) {
                Log.e("API_ERROR", "Failed to fetch restaurants", t);
                Toast.makeText(MainActivity.this, "API Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // Show dialog to add a restaurant to favorites
    private void showAddToFavoritesDialog(YelpRestaurant restaurant) {
        new AlertDialog.Builder(this)
                .setTitle("Add to Favorites")
                .setMessage("Do you want to add " + restaurant.getName() + " to your favorites?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Add the selected restaurant to favorites in the Room database
                    new Thread(() -> {
                        FavoriteRestaurant favorite = new FavoriteRestaurant(
                                restaurant.getName(),
                                restaurant.getCategories().get(0).getTitle(),
                                restaurant.getPhone(),
                                restaurant.getLocation().getAddress1(),
                                restaurant.getImageUrl(),
                                restaurant.getPrice(),
                                (float) restaurant.getRating()
                        );
                        appDatabase.favoriteDao().insert(favorite);
                        runOnUiThread(() -> Toast.makeText(MainActivity.this, "Added to favorites", Toast.LENGTH_SHORT).show());
                    }).start();
                })
                .setNegativeButton("No", null)
                .show();
    }
}
