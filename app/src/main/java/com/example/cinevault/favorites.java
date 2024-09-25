package com.example.cinevault;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class favorites extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorites);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        ViewCompat.setOnApplyWindowInsetsListener(recyclerView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        Button GoBackButton = findViewById(R.id.Backbutton);
        GoBackButton.setOnClickListener(v -> {
            Intent intent = new Intent(favorites.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Set layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load favorites from the database
        DatabaseHandler dbHandler = new DatabaseHandler(this);
        List<Movie> favoriteMovies = dbHandler.getFavoriteMovies(); // Ensure this method is implemented
        Log.d("FavoritesActivity", "Favorite movies count: " + favoriteMovies.size());

        // Set up the adapter
        // If you want to show the favorites list, set isWatchedList to false
        MovieAdapter adapter = new MovieAdapter(favoriteMovies, this, false, false);

        recyclerView.setAdapter(adapter);
    }
}
