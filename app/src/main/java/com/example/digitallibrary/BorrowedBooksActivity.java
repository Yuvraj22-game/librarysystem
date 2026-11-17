package com.example.digitallibrary;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

// Activity displaying books borrowed by the user with return capability
public class BorrowedBooksActivity extends AppCompatActivity implements BorrowedAdapter.OnReturnClickListener {

    private static final String TAG = "BorrowedBooksActivity";

    private RecyclerView recyclerView;
    private TextView tvNoBorrowed;
    private BorrowedAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_borrowed_books);

        Toolbar toolbar = findViewById(R.id.toolbar_borrowed);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My Borrowed Books");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.recycler_borrowed_books);
        tvNoBorrowed = findViewById(R.id.tv_no_borrowed);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BorrowedAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        loadBorrowedBooks();
    }

    private void loadBorrowedBooks() {
        List<Book> borrowedList = BookRepository.getBorrowed();
        adapter.updateList(borrowedList);

        if (borrowedList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            tvNoBorrowed.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            tvNoBorrowed.setVisibility(View.GONE);
        }
    }

    @Override
    public void onReturnClick(Book book) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Return")
                .setMessage("Do you want to return \"" + book.getTitle() + "\"?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Confirm", (dialog, which) -> {
                    boolean success = BookRepository.giveBack(book.getId());
                    if (success) {
                        Toast.makeText(BorrowedBooksActivity.this,
                                "Successfully returned \"" + book.getTitle() + "\"!",
                                Toast.LENGTH_SHORT).show();
                        loadBorrowedBooks();
                    }
                })
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        loadBorrowedBooks();
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
