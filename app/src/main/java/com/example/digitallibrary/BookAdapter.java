package com.example.digitallibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

// RecyclerView adapter for displaying books in the catalogue
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    public interface OnBookClickListener {
        void onBookClick(String bookId);
    }

    private final List<Book> books = new ArrayList<>();
    private final OnBookClickListener listener;

    public BookAdapter(List<Book> books, OnBookClickListener listener) {
        if (books != null) {
            this.books.addAll(books);
        }
        this.listener = listener;
    }

    public void updateList(List<Book> newList) {
        this.books.clear();
        if (newList != null) {
            this.books.addAll(newList);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        Context context = holder.itemView.getContext();

        holder.tvTitle.setText(book.getTitle());
        holder.tvAuthor.setText("By " + book.getAuthor());
        holder.tvCategory.setText("Category: " + book.getCategory());

        if (book.isBorrowed()) {
            holder.tvStatus.setText("Borrowed");
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_borrowed));
        } else {
            holder.tvStatus.setText("Available");
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_available));
        }

        View.OnClickListener clickListener = v -> {
            if (listener != null) {
                listener.onBookClick(book.getId());
            }
        };

        holder.btnViewDetails.setOnClickListener(clickListener);
        holder.itemView.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvAuthor;
        TextView tvCategory;
        TextView tvStatus;
        Button btnViewDetails;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvAuthor = itemView.findViewById(R.id.tv_item_author);
            tvCategory = itemView.findViewById(R.id.tv_item_category);
            tvStatus = itemView.findViewById(R.id.tv_item_status);
            btnViewDetails = itemView.findViewById(R.id.btn_view_details);
        }
    }
}
