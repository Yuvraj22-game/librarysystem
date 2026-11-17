package com.example.digitallibrary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

// RecyclerView adapter for displaying borrowed books
public class BorrowedAdapter extends RecyclerView.Adapter<BorrowedAdapter.BorrowedViewHolder> {

    public interface OnReturnClickListener {
        void onReturnClick(Book book);
    }

    private final List<Book> borrowedBooks = new ArrayList<>();
    private final OnReturnClickListener listener;

    public BorrowedAdapter(List<Book> borrowedBooks, OnReturnClickListener listener) {
        if (borrowedBooks != null) {
            this.borrowedBooks.addAll(borrowedBooks);
        }
        this.listener = listener;
    }

    public void updateList(List<Book> newList) {
        this.borrowedBooks.clear();
        if (newList != null) {
            this.borrowedBooks.addAll(newList);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BorrowedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_borrowed_book, parent, false);
        return new BorrowedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BorrowedViewHolder holder, int position) {
        Book book = borrowedBooks.get(position);
        holder.tvTitle.setText(book.getTitle());
        holder.tvAuthor.setText("By " + book.getAuthor());

        holder.btnReturn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onReturnClick(book);
            }
        });
    }

    @Override
    public int getItemCount() {
        return borrowedBooks.size();
    }

    static class BorrowedViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvAuthor;
        Button btnReturn;

        public BorrowedViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_borrowed_title);
            tvAuthor = itemView.findViewById(R.id.tv_borrowed_author);
            btnReturn = itemView.findViewById(R.id.btn_return_book);
        }
    }
}
