package com.example.cinevault;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieList;
    private DatabaseHandler dbHandler;
    private boolean showWatchedButton;
    private boolean isWatchedList; // New field

    public MovieAdapter(List<Movie> movieList, Context context, boolean showWatchedButton, boolean isWatchedList) {
        this.movieList = movieList;  //List of movies to be displayed in the RecyclerView
        this.dbHandler = new DatabaseHandler(context);
        this.showWatchedButton = showWatchedButton;
        this.isWatchedList = isWatchedList; // Initialize the new field
    }

    // Called when RecyclerView needs a new ViewHolder to represent an item
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for the individual movie items
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view); // Return a new instance of MovieViewHolder
    }

    // Called by RecyclerView to display the data at the specified position
    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        // Bind movie data to UI components
        holder.titleTextView.setText(movie.getTitle());
        holder.yearTextView.setText(String.valueOf(movie.getYear()));
        holder.genreTextView.setText(movie.getGenre());
        Picasso.get().load(movie.getPosterUrl()).into(holder.posterImageView); // Load movie poster image using Picasso

        // Set favorites button visibility based on isWatchedList
        if (isWatchedList) {
            holder.favoritesButton.setVisibility(View.GONE); // Hide favorites button
        } else {
            holder.favoritesButton.setVisibility(View.VISIBLE); // Show favorites button
            holder.favoritesButton.setText(movie.isFavorite() ? "Unfavorite" : "Add to Favorites");

            // Handle Favorites button click
            holder.favoritesButton.setOnClickListener(v -> {
                boolean isFavorite = movie.isFavorite();
                if (isFavorite) {
                    dbHandler.markMovieAsNotFavorite(movie.getId());
                    movie.setFavorite(false);
                    holder.favoritesButton.setText("Add to Favorites");
                    Toast.makeText(v.getContext(), movie.getTitle() + " removed from Favorites!", Toast.LENGTH_SHORT).show();
                } else {
                    dbHandler.markMovieAsFavorite(movie.getId());
                    movie.setFavorite(true);
                    holder.favoritesButton.setText("Unfavorite");
                    Toast.makeText(v.getContext(), movie.getTitle() + " added to Favorites!", Toast.LENGTH_SHORT).show();
                }
                notifyItemChanged(position); // Refresh the specific item
            });
        }

        // Handle Watched button visibility and click
        if (showWatchedButton && holder.watchedButton != null) {
            holder.watchedButton.setVisibility(View.VISIBLE);
            holder.watchedButton.setOnClickListener(v -> {
                dbHandler.moveMovieToWatched(movie.getId());
                movieList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, movieList.size());
                Toast.makeText(v.getContext(), movie.getTitle() + " moved to Watched!", Toast.LENGTH_SHORT).show();
            });
        } else if (holder.watchedButton != null) {
            holder.watchedButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, yearTextView, genreTextView;
        ImageView posterImageView;
        Button favoritesButton, watchedButton;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            yearTextView = itemView.findViewById(R.id.yearTextView);
            genreTextView = itemView.findViewById(R.id.genreTextView);
            posterImageView = itemView.findViewById(R.id.posterImageView);
            favoritesButton = itemView.findViewById(R.id.favoritesButton);
            watchedButton = itemView.findViewById(R.id.watchedButton);
        }
    }
}
