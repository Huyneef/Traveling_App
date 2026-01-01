package com.example.traveling_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.traveling_app.R;
import com.example.traveling_app.model.tour.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private Context context;
    private List<Review> reviews;

    public ReviewAdapter(Context context, List<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review=reviews.get(position);
        if (review==null)
            return ;

        holder.txtName.setText(review.getNameReviewer());
        holder.txtContent.setText(review.getContent());
        holder.txtTime.setText(review.getTime());
        // set ảnh đại diện
        if (review.getAvatarReviewer()!=null)
            Glide.with(holder.img_review).load(review.getAvatarReviewer()).into(holder.img_review);
        else
            holder.img_review.setImageResource(R.drawable.main_avatar);
        for (int i = 0; i < review.getRate(); i++) {
            ImageView star = getStarViewByIndex(holder, i);
            star.setVisibility(View.VISIBLE);
        }
        if (review.getImages()!=null)
            for(int i=0;i<review.getImages().size();i++){
                getImgByIndex(holder,i).setVisibility(View.VISIBLE);
                // ImageLoader.loadImage(review.getImages().get(i),getImgByIndex(holder,i));
                Glide.with(getImgByIndex(holder,i)).load(review.getImages().get(i)).into(getImgByIndex(holder,i));
            }
    }

    private ImageView getStarViewByIndex(ReviewViewHolder holder, int index) {
        switch (index) {
            case 0:
                return holder.star1_review;
            case 1:
                return holder.star2_review;
            case 2:
                return holder.star3_review;
            case 3:
                return holder.star4_review;
            case 4:
                return holder.star5_review;
            default:
                return null;
        }
    }

    private ImageView getImgByIndex(ReviewViewHolder holder, int index) {
        switch (index) {
            case 0:
                return holder.img_review1;
            case 1:
                return holder.img_review2;
            case 2:
                return holder.img_review3;
            case 3:
                return holder.img_review4;
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return reviews==null?0:reviews.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder{
        private TextView txtName, txtTime, txtContent;
        private ImageView img,star1_review,star2_review,star3_review,star4_review,star5_review,img_review1,img_review2,img_review3,img_review4,img_review;
        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName=itemView.findViewById(R.id.name_review);
            txtTime=itemView.findViewById(R.id.time_review);
            txtContent=itemView.findViewById(R.id.content_review);
            star1_review=itemView.findViewById(R.id.star1_review);
            star2_review=itemView.findViewById(R.id.star2_review);
            star3_review=itemView.findViewById(R.id.star3_review);
            star4_review=itemView.findViewById(R.id.star4_review);
            star5_review=itemView.findViewById(R.id.star5_review);
            img_review1=itemView.findViewById(R.id.img_review1);
            img_review2=itemView.findViewById(R.id.img_review2);
            img_review3=itemView.findViewById(R.id.img_review3);
            img_review4=itemView.findViewById(R.id.img_review4);
            img_review=itemView.findViewById(R.id.img_review);
        }
    }
}
