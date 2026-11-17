package com.example.digitallibrary;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

// Displays detailed information about a selected book and allows borrowing/returning
public class BookDetailsActivity extends AppCompatActivity {

    private static final String TAG = "BookDetailsActivity";

    private String bookId;
    private TextView tvId;
    private TextView tvTitle;
    private TextView tvAuthor;
    private TextView tvCategory;
    private TextView tvStatus;
    private TextView tvBanner;
    private TextView tvDescription;
    private Button btnMainAction;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_book_details);

        Toolbar toolbar = findViewById(R.id.toolbar_details);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Book Details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        bookId = getIntent().getStringExtra("BOOK_ID");
        if (bookId == null || BookRepository.findById(bookId) == null) {
            Toast.makeText(this, "Book not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvId = findViewById(R.id.tv_detail_id);
        tvTitle = findViewById(R.id.tv_detail_title);
        tvAuthor = findViewById(R.id.tv_detail_author);
        tvCategory = findViewById(R.id.tv_detail_category);
        tvStatus = findViewById(R.id.tv_detail_status);
        tvBanner = findViewById(R.id.tv_detail_banner);
        tvDescription = findViewById(R.id.tv_detail_description);
        btnMainAction = findViewById(R.id.btn_detail_main_action);
        btnBack = findViewById(R.id.btn_detail_back);

        btnBack.setOnClickListener(v -> finish());

        refreshUI();
    }

    private void refreshUI() {
        Book book = BookRepository.findById(bookId);
        if (book == null) {
            Toast.makeText(this, "Book not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvId.setText("ID: " + book.getId());
        tvTitle.setText(book.getTitle());
        tvAuthor.setText("By " + book.getAuthor());
        tvCategory.setText("Category: " + book.getCategory());
        tvDescription.setText(book.getDescription());

        if (book.isBorrowed()) {
            tvStatus.setText("Borrowed");
            tvStatus.setTextColor(ContextCompat.getColor(this, R.color.status_borrowed));
            tvBanner.setText("Borrowed by you - Return it from here or from My Borrowed Books");
            btnMainAction.setText("Return Book");
            btnMainAction.setBackgroundColor(ContextCompat.getColor(this, R.color.status_borrowed));

            btnMainAction.setOnClickListener(v -> showReturnDialog(book));
        } else {
            tvStatus.setText("Available");
            tvStatus.setTextColor(ContextCompat.getColor(this, R.color.status_available));
            tvBanner.setText("In Stock & Ready - Available for immediate 14-day campus loan");
            btnMainAction.setText("Borrow Book");
            btnMainAction.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500));

            btnMainAction.setOnClickListener(v -> showBorrowDialog(book));
        }
    }

    private void showBorrowDialog(Book book) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Borrowing")
                .setMessage("Do you want to borrow \"" + book.getTitle() + "\"?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Confirm", (dialog, which) -> {
                    boolean success = BookRepository.borrow(book.getId());
                    if (success) {
                        Toast.makeText(BookDetailsActivity.this,
                                "Successfully borrowed \"" + book.getTitle() + "\"!",
                                Toast.LENGTH_SHORT).show();
                        refreshUI();
                    } else {
                        Toast.makeText(BookDetailsActivity.this,
                                "Borrow limit of 3 books reached",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private void showReturnDialog(Book book) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Return")
                .setMessage("Do you want to return \"" + book.getTitle() + "\"?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Confirm", (dialog, which) -> {
                    boolean success = BookRepository.giveBack(book.getId());
                    if (success) {
                        Toast.makeText(BookDetailsActivity.this,
                                "Successfully returned \"" + book.getTitle() + "\"!",
                                Toast.LENGTH_SHORT).show();
                        refreshUI();
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
        refreshUI();
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
