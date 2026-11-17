package com.example.digitallibrary;

import java.util.ArrayList;
import java.util.List;

// Repository managing all sample books and borrow state
public class BookRepository {
    public static final int MAX_BORROW = 3;
    private static final List<Book> books = new ArrayList<>();

    static {
        // Initializing 10 sample books (B101 to B110)
        books.add(new Book("B101", "Clean Code", "Robert C. Martin", "Programming",
                "A handbook of agile software craftsmanship focused on writing readable code.", false));
        books.add(new Book("B102", "The Pragmatic Programmer", "Andrew Hunt & David Thomas", "Programming",
                "Your journey to software mastery through pragmatic principles and techniques.", false));
        books.add(new Book("B103", "To Kill a Mockingbird", "Harper Lee", "Fiction",
                "A powerful story of innocence, compassion, and justice in a small town.", false));
        books.add(new Book("B104", "1984", "George Orwell", "Fiction",
                "A classic dystopian novel exploring surveillance, truth, and freedom.", false));
        books.add(new Book("B105", "A Brief History of Time", "Stephen Hawking", "Science",
                "An accessible exploration of black holes, space, time, and cosmology.", false));
        books.add(new Book("B106", "Sapiens", "Yuval Noah Harari", "Science",
                "A narrative of human history exploring how Homo sapiens came to rule Earth.", false));
        books.add(new Book("B107", "The Lean Startup", "Eric Ries", "Business",
                "How modern entrepreneurs use continuous innovation to build successful products.", false));
        books.add(new Book("B108", "Zero to One", "Peter Thiel", "Business",
                "Notes on startups and secrets on how to build a better future.", false));
        books.add(new Book("B109", "Design Patterns", "Erich Gamma et al.", "Programming",
                "Cataloging object-oriented software design patterns for scalable architecture.", false));
        books.add(new Book("B110", "Dune", "Frank Herbert", "Fiction",
                "An epic science fiction adventure set on the desert planet Arrakis.", false));
    }

    // Returns all books in repository
    public static List<Book> getAll() {
        return books;
    }

    // Finds a book by ID, returning null if not found
    public static Book findById(String id) {
        if (id == null) return null;
        for (Book book : books) {
            if (book.getId().equalsIgnoreCase(id)) {
                return book;
            }
        }
        return null;
    }

    // Returns count of currently borrowed books
    public static int borrowedCount() {
        int count = 0;
        for (Book book : books) {
            if (book.isBorrowed()) {
                count++;
            }
        }
        return count;
    }

    // Returns list of currently borrowed books
    public static List<Book> getBorrowed() {
        List<Book> borrowedList = new ArrayList<>();
        for (Book book : books) {
            if (book.isBorrowed()) {
                borrowedList.add(book);
            }
        }
        return borrowedList;
    }

    // Borrows a book if allowed; returns false on failure
    public static boolean borrow(String id) {
        Book book = findById(id);
        if (book == null || book.isBorrowed() || borrowedCount() >= MAX_BORROW) {
            return false;
        }
        book.setBorrowed(true);
        return true;
    }

    // Returns a borrowed book; returns false on failure
    public static boolean giveBack(String id) {
        Book book = findById(id);
        if (book == null || !book.isBorrowed()) {
            return false;
        }
        book.setBorrowed(false);
        return true;
    }
}
