package com.example.digitallibrary;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

// Main catalogue activity displaying book list, search, filters, and stats
public class MainActivity extends AppCompatActivity implements BookAdapter.OnBookClickListener {

    private static final String TAG = "MainActivity";
    private static final String KEY_SEARCH = "search_text";
    private static final String KEY_CATEGORY = "category_pos";
    private static final String KEY_STATUS = "status_pos";

    private TextView tvStatTotal;
    private TextView tvStatRemaining;
    private TextView tvStatBorrowed;
    private EditText etSearch;
    private Spinner spinnerCategory;
    private Spinner spinnerStatus;
    private RecyclerView recyclerView;
    private TextView tvNoBooks;
    private BookAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Digital Library");
        }

        tvStatTotal = findViewById(R.id.tv_stat_total);
        tvStatRemaining = findViewById(R.id.tv_stat_remaining);
        tvStatBorrowed = findViewById(R.id.tv_stat_borrowed);
        etSearch = findViewById(R.id.et_search);
        spinnerCategory = findViewById(R.id.spinner_category);
        spinnerStatus = findViewById(R.id.spinner_status);
        recyclerView = findViewById(R.id.recycler_books);
        tvNoBooks = findViewById(R.id.tv_no_books);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookAdapter = new BookAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(bookAdapter);

        setupSpinners();
        setupSearch();

        if (savedInstanceState != null) {
            etSearch.setText(savedInstanceState.getString(KEY_SEARCH, ""));
            spinnerCategory.setSelection(savedInstanceState.getInt(KEY_CATEGORY, 0));
            spinnerStatus.setSelection(savedInstanceState.getInt(KEY_STATUS, 0));
        }
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> catAdapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(catAdapter);

        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this,
                R.array.status_array, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(statusAdapter);

        AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };

        spinnerCategory.setOnItemSelectedListener(spinnerListener);
        spinnerStatus.setOnItemSelectedListener(spinnerListener);
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilters();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // Applies search, category, and status filters together
    private void applyFilters() {
        String query = etSearch.getText().toString().trim().toLowerCase();
        String selectedCategory = spinnerCategory.getSelectedItem() != null ? spinnerCategory.getSelectedItem().toString() : "All Categories";
        String selectedStatus = spinnerStatus.getSelectedItem() != null ? spinnerStatus.getSelectedItem().toString() : "All Books";

        List<Book> filtered = new ArrayList<>();
        for (Book book : BookRepository.getAll()) {
            boolean matchesSearch = query.isEmpty() || book.getTitle().toLowerCase().contains(query);
            boolean matchesCategory = selectedCategory.equals("All Categories") || book.getCategory().equalsIgnoreCase(selectedCategory);
            boolean matchesStatus = true;
            if (selectedStatus.equals("Available")) {
                matchesStatus = !book.isBorrowed();
            } else if (selectedStatus.equals("Borrowed")) {
                matchesStatus = book.isBorrowed();
            }

            if (matchesSearch && matchesCategory && matchesStatus) {
                filtered.add(book);
            }
        }

        bookAdapter.updateList(filtered);

        if (filtered.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            tvNoBooks.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            tvNoBooks.setVisibility(View.GONE);
        }

        updateStats();
    }

    // Updates stat cards for total, remaining, and borrowed books
    private void updateStats() {
        int total = BookRepository.getAll().size();
        int borrowed = BookRepository.borrowedCount();
        int remaining = total - borrowed;

        tvStatTotal.setText(String.valueOf(total));
        tvStatRemaining.setText(String.valueOf(remaining));
        tvStatBorrowed.setText(borrowed + " / " + BookRepository.MAX_BORROW);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_my_borrowed) {
            Intent intent = new Intent(this, BorrowedBooksActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBookClick(String bookId) {
        Intent intent = new Intent(this, BookDetailsActivity.class);
        intent.putExtra("BOOK_ID", bookId);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_SEARCH, etSearch.getText().toString());
        outState.putInt(KEY_CATEGORY, spinnerCategory.getSelectedItemPosition());
        outState.putInt(KEY_STATUS, spinnerStatus.getSelectedItemPosition());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        applyFilters();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }
}
