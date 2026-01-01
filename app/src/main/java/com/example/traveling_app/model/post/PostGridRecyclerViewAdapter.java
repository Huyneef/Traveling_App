package com.example.traveling_app.model.post;

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

public class PostGridRecyclerViewAdapter extends FirebaseRecyclerAdapter<Post, PostGridViewHolder> {
    private final RequestManager imageLoader;
    public PostGridRecyclerViewAdapter(@NonNull FirebaseRecyclerOptions<Post> options, Context context) {
        super(options);
        imageLoader = Glide.with(context);
    }

    @Override
    protected void onBindViewHolder(@NonNull PostGridViewHolder holder, int position, @NonNull Post post) {
        holder.bindDataToView(post);
    }

    @NonNull
    @Override
    public PostGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostGridViewHolder(rootView, imageLoader);
    }
}
