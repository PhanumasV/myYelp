package com.example.phanumas_vanichayavarodomkulyelp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private List<FavoriteRestaurant> favorites;

    public FavoriteAdapter(List<FavoriteRestaurant> favorites) {
        this.favorites = favorites != null ? favorites : new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (favorites != null && !favorites.isEmpty()) {
            FavoriteRestaurant restaurant = favorites.get(position);

            holder.name.setText(restaurant.getName());
            holder.category.setText(restaurant.getCategory());
            holder.phone.setText(restaurant.getPhone());
            holder.address.setText(restaurant.getAddress());
            holder.price.setText(restaurant.getPrice());
            holder.ratingBar.setRating(restaurant.getRating());

            Glide.with(holder.image.getContext())
                    .load(restaurant.getImageUrl())
                    .placeholder(R.drawable.favorite_icon) // Optional: add a placeholder image
                    .error(R.drawable.error_icon) // Optional: add an error image in case of a load failure
                    .into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return favorites != null ? favorites.size() : 0;
    }

    public void updateFavorites(List<FavoriteRestaurant> newFavorites) {
        this.favorites = newFavorites != null ? newFavorites : new ArrayList<>();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, category, phone, address, price;
        RatingBar ratingBar;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.restaurantName);
            category = itemView.findViewById(R.id.restaurantCategory);
            phone = itemView.findViewById(R.id.restaurantPhone);
            address = itemView.findViewById(R.id.restaurantAddress);
            price = itemView.findViewById(R.id.restaurantPrice);
            ratingBar = itemView.findViewById(R.id.restaurantRating);
            image = itemView.findViewById(R.id.restaurantImage);
        }
    }
}
