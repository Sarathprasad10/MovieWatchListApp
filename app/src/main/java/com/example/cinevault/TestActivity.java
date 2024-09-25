package com.example.cinevault;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {

    private DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // Initialize DatabaseHandler
        dbHandler = new DatabaseHandler(this);

        // Add test data
        addTestMovies();
    }

    private void addTestMovies() {
        dbHandler.addMovie("Inception", 2010, "Science Fiction", "https://example.com/inception.jpg");
        dbHandler.addMovie("The Matrix", 1999, "Action", "https://example.com/matrix.jpg");
        dbHandler.addMovie("Interstellar", 2014, "Science Fiction", "https://example.com/interstellar.jpg");
    }
}
