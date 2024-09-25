package com.example.cinevault;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cinevault.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_MOVIES = "movies";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_GENRE = "genre";
    public static final String COLUMN_POSTER_URL = "poster_url";
    public static final String COLUMN_IS_FAVORITE = "is_favorite";
    public static final String COLUMN_IS_WATCHED = "is_watched";

    public static final String TABLE_WATCHED = "watched";
    public static final String COLUMN_WATCHED_ID = "id"; // Same as COLUMN_ID

    // Create movies table
    private static final String CREATE_TABLE_MOVIES = "CREATE TABLE " + TABLE_MOVIES + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TITLE + " TEXT, " +
            COLUMN_YEAR + " INTEGER, " +
            COLUMN_GENRE + " TEXT, " +
            COLUMN_POSTER_URL + " TEXT, " +
            COLUMN_IS_FAVORITE + " INTEGER DEFAULT 0, " +
            COLUMN_IS_WATCHED + " INTEGER DEFAULT 0)";

    // Create watched table
    private static final String CREATE_TABLE_WATCHED = "CREATE TABLE " + TABLE_WATCHED + " (" +
            COLUMN_WATCHED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TITLE + " TEXT, " +
            COLUMN_YEAR + " INTEGER, " +
            COLUMN_GENRE + " TEXT, " +
            COLUMN_POSTER_URL + " TEXT)";

    // Constructor for the DatabaseHandler class
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MOVIES);
        db.execSQL(CREATE_TABLE_WATCHED);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WATCHED);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        onCreate(db);
    }

    // Adding a new movie to the movies table
    public void addMovie(String title, int year, String genre, String posterUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_YEAR, year);
        values.put(COLUMN_GENRE, genre);
        values.put(COLUMN_POSTER_URL, posterUrl);
        db.insert(TABLE_MOVIES, null, values);
        db.close();
    }

    // Get all movies from the movies table
    public List<Movie> getAllMovies() {
        List<Movie> allMovies = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MOVIES, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
                int year = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_YEAR));
                String genre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENRE));
                String posterUrl = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POSTER_URL));

                Movie movie = new Movie(id, title, year, genre, posterUrl);
                movie.setFavorite(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_FAVORITE)) == 1);  // Set favorite status
                allMovies.add(movie);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return allMovies;
    }

    // Move movie to the watched table and remove from the movies table
    public void moveMovieToWatched(int movieId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(TABLE_MOVIES, null, COLUMN_ID + " = ?", new String[]{String.valueOf(movieId)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_TITLE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                values.put(COLUMN_YEAR, cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_YEAR)));
                values.put(COLUMN_GENRE, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENRE)));
                values.put(COLUMN_POSTER_URL, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POSTER_URL)));

                db.insert(TABLE_WATCHED, null, values);
                db.delete(TABLE_MOVIES, COLUMN_ID + " = ?", new String[]{String.valueOf(movieId)});
            }
        } catch (Exception e) {
            Log.e("DatabaseHandler", "Error moving movie to watched", e);
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }

    // Get all movies in the watched table
    public List<Movie> getAllWatchedMovies() {
        List<Movie> watchedMovies = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_WATCHED, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_WATCHED_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
                int year = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_YEAR));
                String genre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENRE));
                String posterUrl = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POSTER_URL));

                watchedMovies.add(new Movie(id, title, year, genre, posterUrl));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return watchedMovies;
    }

    // Mark a movie as favorite
    public void markMovieAsFavorite(int movieId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_FAVORITE, 1);  // 1 means 'favorite'
        db.update(TABLE_MOVIES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(movieId)});
        db.close();
    }

    // Mark a movie as not favorite
    public void markMovieAsNotFavorite(int movieId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_FAVORITE, 0);  // 0 means 'not favorite'
        db.update(TABLE_MOVIES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(movieId)});
        db.close();
    }

    // Get all favorite movies from the movies table
    public List<Movie> getFavoriteMovies() {
        List<Movie> favoriteMovies = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MOVIES + " WHERE " + COLUMN_IS_FAVORITE + " = 1", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
                int year = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_YEAR));
                String genre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENRE));
                String posterUrl = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POSTER_URL));

                Movie movie = new Movie(id, title, year, genre, posterUrl);
                movie.setFavorite(true);  // Set favorite status
                favoriteMovies.add(movie);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return favoriteMovies;
    }


    // Delete a movie from the movies table
    public void deleteMovie(int movieId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MOVIES, COLUMN_ID + " = ?", new String[]{String.valueOf(movieId)});
        db.close();
    }
    // Mark a movie as watched without moving it to another table
    public void markMovieAsWatched(int movieId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_WATCHED, 1);  // 1 means 'watched'
        db.update(TABLE_MOVIES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(movieId)});
        db.close();
    }
}
