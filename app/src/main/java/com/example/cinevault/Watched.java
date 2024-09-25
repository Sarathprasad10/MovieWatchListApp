package com.example.cinevault;

public class Watched {
    private int id; // This can be the watched ID from the database
    private String title;
    private int year;
    private String genre;
    private String posterUrl;

    // Constructor
    public Watched(int id, String title, int year, String genre, String posterUrl) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.genre = genre;
        this.posterUrl = posterUrl;
    }

    // Getters
    public int getId() {
        return id;
    }

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

    // You can also add setters if needed
}
