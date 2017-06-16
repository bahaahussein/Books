package com.example.android.books;

import java.util.ArrayList;

/**
 * Created by Professor on 6/15/2017.
 */

public class Book {

    private String title;
    private ArrayList<String> authors;
    private String date;
    private String description;
    private double rate;
    private String image;

    public Book(String title, ArrayList<String> authors, String date, String description, double rate, String image) {
        this.title = title;
        this.authors = authors;
        this.date = date;
        this.description = description;
        this.rate = rate;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public double getRate() {
        return rate;
    }

    public String getImage() {
        return image;
    }
}
