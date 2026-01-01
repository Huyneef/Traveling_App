package com.example.traveling_app.model.savedtours;
import com.bumptech.glide.Glide;
import com.example.traveling_app.activity.DetailActivity;
import com.example.traveling_app.R;
import com.example.traveling_app.common.DatabaseReferences;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

class SavedTourViewHolder extends RecyclerView.ViewHolder {

    private final TextView txtTitle, txtAddress, txtBook, txtComment, txtRate;
    private final ImageView img, heartButton;
    private SavedTour savedTour;

    SavedTourViewHolder(View rootView) {
        super(rootView);
        txtTitle = rootView.findViewById(R.id.title_love_tour);
        txtAddress = rootView.findViewById(R.id.address_love_tour);
        txtBook = rootView.findViewById(R.id.book_love_tour);
        txtComment = rootView.findViewById(R.id.comment_love_tour);
        txtRate = rootView.findViewById(R.id.rate_love_tour);
        img = rootView.findViewById(R.id.img_love_tour);
        heartButton = rootView.findViewById(R.id.heartImageView);
        heartButton.setOnClickListener(this::removeSavedVoucher);
        rootView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("id", savedTour.getTourId());
            context.startActivity(intent);
        });
    }

    void bindDataToViewHolder(SavedTour savedTour) {

        this.savedTour = savedTour;
        savedTour.getTourAsync(tour -> {
            txtTitle.setText(tour.getName());
            txtAddress.setText(tour.getAddress());
            txtBook.setText(txtBook.getContext().getString(R.string.book_count_placeholder, tour.getNumBooking()));
            txtComment.setText(txtComment.getContext().getString(R.string.rate_count_placeholder, tour.getNumComment()));
            txtRate.setText(Double.toString(Math.round(tour.getNumStar() * 100D) / 100D));
            heartButton.setVisibility(View.VISIBLE);
            Glide.with(img).load(tour.getMainImageUrl()).centerCrop().into(img);
        });
    }

    private void removeSavedVoucher(View v) {
        v.setVisibility(View.GONE);
        DatabaseReferences.USER_SAVED_TOURS_DATABASE_REF.child(savedTour.getUsername()).child(savedTour.getTourId()).setValue(null);
    }

}