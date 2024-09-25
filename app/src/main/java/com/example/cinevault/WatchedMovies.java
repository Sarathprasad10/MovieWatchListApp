package com.example.cinevault;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WatchedMovies extends AppCompatActivity {

    private RecyclerView watchedRecyclerView;
    private MovieAdapter movieAdapter;
    private DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watched_movies); // Ensure this points to the correct layout

        // Initialize RecyclerView
        watchedRecyclerView = findViewById(R.id.watchedListView); // Use the updated ID
        watchedRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the database handler
        dbHandler = new DatabaseHandler(this);

        // Fetch watched movies from the database
        List<Movie> watchedMovies = dbHandler.getAllWatchedMovies(); // Fetch from the new method

        // Find the TextView for empty state
        TextView noMoviesTextView = findViewById(R.id.noMoviesTextView);

        if (watchedMovies != null && !watchedMovies.isEmpty()) {
            // Set up the adapter and attach it to the RecyclerView
            movieAdapter = new MovieAdapter(watchedMovies, this, false, true); // true for isWatchedList
            watchedRecyclerView.setAdapter(movieAdapter);
            noMoviesTextView.setVisibility(View.GONE); // Hide the message if movies are found
        } else {
            // Show the message when no movies are found
            noMoviesTextView.setVisibility(View.VISIBLE);
            watchedRecyclerView.setVisibility(View.GONE); // Optionally hide the RecyclerView
        }
        Button GoBackButton = findViewById(R.id.Backbutton);
        GoBackButton.setOnClickListener(v -> {
            Intent intent = new Intent(WatchedMovies.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
