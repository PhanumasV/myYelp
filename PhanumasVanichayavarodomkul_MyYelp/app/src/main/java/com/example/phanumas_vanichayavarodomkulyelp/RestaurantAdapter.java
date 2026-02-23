package com.example.phanumas_vanichayavarodomkulyelp;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

    public interface OnRestaurantClickListener {
        void onClick(YelpRestaurant restaurant);
    }

    private List<YelpRestaurant> restaurants;
    private OnRestaurantClickListener listener;
    private AppDatabase appDatabase;
    private Context context;

    public RestaurantAdapter(List<YelpRestaurant> restaurants, OnRestaurantClickListener listener, AppDatabase appDatabase, Context context) {
        this.restaurants = restaurants;
        this.listener = listener;
        this.appDatabase = appDatabase;
        this.context = context;
    }

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurant, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RestaurantViewHolder holder, int position) {
        YelpRestaurant restaurant = restaurants.get(position);
        holder.nameTextView.setText((position + 1) + ". " + restaurant.getName());

        // Get category information - this is what's missing
        String category = "";
        if (restaurant.getCategories() != null && !restaurant.getCategories().isEmpty()) {
            category = restaurant.getCategories().get(0).getTitle();
        } else {
            category = "N/A";
        }
        holder.categoryTextView.setText(category);

        // Format address with city and state
        String address = "";
        if (restaurant.getLocation() != null) {
            address = restaurant.getLocation().getAddress1();
            // Add city information if available
            if (address != null && !address.isEmpty()) {
                address += ", " + restaurant.getLocation().getCity() + ", " + restaurant.getLocation().getState();
            }
        } else {
            address = "No address";
        }
        holder.addressTextView.setText(address);

        // Set price information
        String price = restaurant.getPrice() != null ? restaurant.getPrice() : "N/A";
        holder.priceTextView.setText(price);

        // Set phone information
        holder.phoneTextView.setText(restaurant.getPhone() != null ? restaurant.getPhone() : "N/A");

        // Set rating
        holder.ratingBar.setRating((float) restaurant.getRating());

        // Load image
        String imageUrl = restaurant.getImageUrl();

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).into(holder.imageView);
        } else {
            // Optional: set a default image if URL is missing
            holder.imageView.setImageResource(R.drawable.restaurant_sample);
        }


        // On restaurant item click
        holder.itemView.setOnClickListener(v -> {
            // Show a confirmation dialog when the user clicks on a restaurant
            new AlertDialog.Builder(context)
                    .setTitle("Add to Favorites")
                    .setMessage("Do you want to add this restaurant to your favorites?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Add the restaurant to favorites
                        addToFavorites(restaurant);
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void addToFavorites(YelpRestaurant restaurant) {
        FavoriteRestaurant favoriteRestaurant = new FavoriteRestaurant(
                restaurant.getName(),
                restaurant.getCategories().get(0).getTitle(),
                restaurant.getPhone(),
                restaurant.getLocation().getAddress1(),
                restaurant.getImageUrl(),
                restaurant.getPrice(),
                (float) restaurant.getRating()
        );
        favoriteRestaurant.setName(restaurant.getName());
        favoriteRestaurant.setAddress(restaurant.getLocation() != null ? restaurant.getLocation().getAddress1() : "No address");
        favoriteRestaurant.setImageUrl(restaurant.getImageUrl());

        // Insert the favorite restaurant into the Room database
        new Thread(() -> {
            appDatabase.favoriteDao().insert(favoriteRestaurant);
            // Optionally notify the user that the restaurant was added
        }).start();
    }

    @Override
    public int getItemCount() {
        return restaurants != null ? restaurants.size() : 0;
    }

    public void updateData(List<YelpRestaurant> newRestaurants) {
        this.restaurants = newRestaurants;
        notifyDataSetChanged();
    }

    public void sortBy(String criteria) {
        if ("Rating".equals(criteria)) {
            restaurants.sort((r1, r2) -> Double.compare(r2.getRating(), r1.getRating()));
        } else if ("Price".equals(criteria)) {
            restaurants.sort((r1, r2) -> {
                int price1 = r1.getPrice() != null ? r1.getPrice().length() : 0;
                int price2 = r2.getPrice() != null ? r2.getPrice().length() : 0;
                return Integer.compare(price1, price2);
            });
        }
        notifyDataSetChanged();
    }

    public class RestaurantViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, priceTextView, categoryTextView, phoneTextView, addressTextView;

        RatingBar ratingBar;
        ImageView imageView;

        public RestaurantViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.restaurantName);
            priceTextView = itemView.findViewById(R.id.restaurantPrice);
            categoryTextView = itemView.findViewById(R.id.restaurantCategory);
            phoneTextView = itemView.findViewById(R.id.restaurantPhone);
            addressTextView = itemView.findViewById(R.id.restaurantAddress);
            ratingBar = itemView.findViewById(R.id.restaurantRating);
            imageView = itemView.findViewById(R.id.restaurantImage);

        }
    }
}

