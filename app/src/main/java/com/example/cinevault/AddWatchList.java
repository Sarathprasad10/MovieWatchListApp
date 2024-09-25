package com.example.cinevault;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddWatchList extends AppCompatActivity {

    private EditText movieTitle;
    private EditText releaseYear;
    private EditText posterUrl;
    private Spinner genreSpinner;
    private Button submitButton;
    private DatabaseHandler dbHandler;  // Moved here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_watch_list);

        // Initialize UI components
        movieTitle = findViewById(R.id.editTextTitle);
        releaseYear = findViewById(R.id.editTextYear);
        posterUrl = findViewById(R.id.editTextPoster);
        genreSpinner = findViewById(R.id.genreSpinner);
        submitButton = findViewById(R.id.submitButton);

        // Initialize the database handler
        dbHandler = new DatabaseHandler(this);

        // List of genres
        String[] genres = {"Action", "Comedy", "Drama", "Horror", "Romance"};

        // Set up the ArrayAdapter for the Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genres);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreSpinner.setAdapter(adapter);

        // Handle submit button click
        submitButton.setOnClickListener(v -> {
            String title = movieTitle.getText().toString();
            String year = releaseYear.getText().toString();
            String poster = posterUrl.getText().toString();
            String genre = genreSpinner.getSelectedItem().toString();

            // Validate input and display toast
            if (title.isEmpty() || year.isEmpty() || poster.isEmpty()) {
                Toast.makeText(AddWatchList.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Add the movie to the database
                dbHandler.addMovie(title, Integer.parseInt(year), genre, poster);

                Toast.makeText(AddWatchList.this, "Movie added to Watchlist!", Toast.LENGTH_SHORT).show();

                // Optionally, clear the input fields
                movieTitle.setText("");
                releaseYear.setText("");
                posterUrl.setText("");
                genreSpinner.setSelection(0);  // Reset the spinner selection
            }
        });
        Button GoBackButton = findViewById(R.id.Backbutton);
        GoBackButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddWatchList.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

}
