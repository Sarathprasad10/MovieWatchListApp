package com.example.cinevault;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.util.Log;
import android.widget.Button;

public class WatchList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list);

        // Initialize DatabaseHandler
        dbHandler = new DatabaseHandler(this);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load movie data from database
        List<Movie> movieList = fetchMoviesFromDatabase();

        // Set adapter
        movieAdapter = new MovieAdapter(movieList, this, true, false); // 'false' since it's the watchlist
        recyclerView.setAdapter(movieAdapter);

        Button GoBackButton = findViewById(R.id.Backbutton);
        GoBackButton.setOnClickListener(v -> {
            Intent intent = new Intent(WatchList.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private List<Movie> fetchMoviesFromDatabase() {
        List<Movie> movies = dbHandler.getAllMovies(); // Directly fetch the list of movies

        if (movies.isEmpty()) {
            Log.d("WatchList", "No movies found in the database.");
        } else {
            for (Movie movie : movies) {
                Log.d("WatchList", "Fetched movie: " + movie.getTitle() + ", " + movie.getYear() + ", " + movie.getGenre() + ", " + movie.getPosterUrl());
            }
        }

        return movies;
    }


}
