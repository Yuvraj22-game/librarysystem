package com.example.digitallibrary;

// Represents a book item in the digital library
public class Book {
    private final String id;
    private final String title;
    private final String author;
    private final String category;
    private final String description;
    private boolean borrowed;

    public Book(String id, String title, String author, String category, String description, boolean borrowed) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.description = description;
        this.borrowed = borrowed;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public boolean isBorrowed() {
        return borrowed;
    }

    public void setBorrowed(boolean borrowed) {
        this.borrowed = borrowed;
    }
}
