package com.example.traveling_app.model.post;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.traveling_app.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.text.DateFormat;

public class PostRecyclerViewAdapter extends FirebaseRecyclerAdapter<Post, PostViewHolder> {

    private final DateFormat dateFormat;
    private final DateFormat timeFormat;
    private final RequestManager requestManager;

    public PostRecyclerViewAdapter(@NonNull FirebaseRecyclerOptions<Post> options, Context context) {
        super(options);
        this.dateFormat = android.text.format.DateFormat.getLongDateFormat(context);
        this.timeFormat = android.text.format.DateFormat.getTimeFormat(context);
        this.requestManager = Glide.with(context);
    }

    @Override
    protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull Post post) {
        holder.bindPostToView(post);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_post_layout, parent, false);
        return new PostViewHolder(view, dateFormat, timeFormat, requestManager);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        stopListening();
    }
}
