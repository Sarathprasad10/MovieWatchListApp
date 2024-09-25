package com.example.cinevault;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Set up window insets for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up buttons and their click listeners
        Button addMovieButton = findViewById(R.id.AddWatchList);
        Button viewWatchlistButton = findViewById(R.id.WatchList);
        Button viewWatchedMoviesButton = findViewById(R.id.Watched);
        Button addFavouritesButton=findViewById(R.id.favourites);

        addMovieButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddWatchList.class);
            startActivity(intent);


        });

        viewWatchlistButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WatchList.class);
            startActivity(intent);

        });

        viewWatchedMoviesButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WatchedMovies.class); // Navigate to WatchedMovies
            startActivity(intent);

        });
        addFavouritesButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, favorites.class); // Navigate to Favourites
            startActivity(intent);

        });
    }

}
