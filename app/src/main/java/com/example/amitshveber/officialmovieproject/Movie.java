package com.example.amitshveber.officialmovieproject;

/**
 * Created by amit shveber on 20/02/2017.
 */

public class Movie {
    public String Title;
    String Description;
    String URL;
    String id;
    String Year;
    String Rate;
    String RunTime;
    String Actors;

    public Movie(String title, String id, String URL) {
        this.Title = title;
        this.id = id;
        this.URL = URL;

    }


    @Override
    public String toString() {
        return Title;
    }
}
