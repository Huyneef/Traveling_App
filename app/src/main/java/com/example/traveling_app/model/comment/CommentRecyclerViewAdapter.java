package com.example.traveling_app.model.comment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.traveling_app.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.text.DateFormat;

public class CommentRecyclerViewAdapter extends FirebaseRecyclerAdapter<Comment, CommentViewHolder> {
    private final DateFormat dateFormat;
    private final DateFormat timeFormat;
    private final RequestManager imageLoader;
    private Runnable onDataChangedListener;
    public CommentRecyclerViewAdapter(FirebaseRecyclerOptions options, Context context) {
        super(options);
        this.dateFormat = android.text.format.DateFormat.getLongDateFormat(context);
        this.timeFormat = android.text.format.DateFormat.getTimeFormat(context);
        this.imageLoader = Glide.with(context);
    }
    @Override
    protected void onBindViewHolder(@NonNull CommentViewHolder holder, int position, @NonNull Comment comment) {
        holder.bindCommentToView(comment);
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_item, parent, false);
        return new CommentViewHolder(rootView, dateFormat, timeFormat, imageLoader);
    }

    @Override
    public void onDataChanged() {
        if (onDataChangedListener != null)
            onDataChangedListener.run();
    }

    public void setOnDataChangedListener(Runnable onDataChangedListener) {
        this.onDataChangedListener = onDataChangedListener;
    }

}

