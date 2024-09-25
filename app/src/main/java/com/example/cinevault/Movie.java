package com.example.cinevault;

public class Movie {
    private int id;
    private String title;
    private int year;
    private String genre;
    private String posterUrl;
    private boolean isFavorite;
    // Add more fields if necessary

    // Constructor, getters, and setters

    public Movie(int id, String title, int year, String genre, String posterUrl) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.genre = genre;
        this.posterUrl = posterUrl;
        this.isFavorite = false;
    }

    // Getter for id
    public int getId() {
        return id;
    }

    // Other getters and setters
    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public String getGenre() {
        return genre;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

}
