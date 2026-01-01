package com.example.traveling_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveling_app.activity.DetailActivity;
import com.example.traveling_app.R;
import com.example.traveling_app.common.CheckLove;
import com.example.traveling_app.common.CurrentUser;
import com.example.traveling_app.common.ImageLoader;
import com.example.traveling_app.model.tour.Tour;

import java.text.DecimalFormat;
import java.util.List;

public class HotTourAdapter extends RecyclerView.Adapter<HotTourAdapter.HotTourViewHolder>{
    private List<Tour> tours;
    private Context context;

    public HotTourAdapter( Context context,List<Tour> tours) {
        this.tours = tours;
        this.context = context;
    }

    @NonNull
    @Override
    public HotTourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.hot_tour_item,parent,false);
        return new HotTourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotTourViewHolder holder, int position) {
        Tour tour=tours.get(position);
        if (tour==null)
            return;
        DecimalFormat df = new DecimalFormat("#.##");
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        holder.txtTitle.setText(tour.getName());
        holder.txtRate.setText(df.format(tour.getNumStar()).replace(',','.'));
        holder.txtReview.setText("(" +tour.getNumComment()+" đánh giá)");
        holder.txtPrice.setText(formatter.format(tour.getPrice())+" đ");
        holder.txtSale.setText(formatter.format(tour.getSalePrice())+" đ");
        String percent="(" + df.format((tour.getSalePrice()/tour.getPrice())*100).replace(',','.') +"%)";
        holder.txtPercent.setText(percent);
        ImageLoader.loadImage(tours.get(position).getMainImageUrl(), holder.img);

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, DetailActivity.class);
                intent.putExtra("id", tour.getId());
                context.startActivity(intent);
            }
        });

        CheckLove.isLoved(CurrentUser.getCurrentUser().getUsername(),tour.getId(),holder.hot_tour_love);
        CheckLove.saveData(CurrentUser.getCurrentUser().getUsername(),tour.getId(),holder.hot_tour_love);

    }

    @Override
    public int getItemCount() {
        return tours==null?0:tours.size();
    }

    public static class HotTourViewHolder extends RecyclerView.ViewHolder{

        private TextView txtTitle, txtPercent, txtRate, txtReview,txtPrice,txtSale;
        private ImageView img,hot_tour_love;
        public HotTourViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle=itemView.findViewById(R.id.title_hot);
            txtRate=itemView.findViewById(R.id.rate_hot);
            txtReview=itemView.findViewById(R.id.review_hot);
            txtPercent=itemView.findViewById(R.id.percent_hot);
            txtPrice=itemView.findViewById(R.id.price_hot);
            txtSale=itemView.findViewById(R.id.sale_hot);
            img=itemView.findViewById(R.id.image_hot);
            hot_tour_love=itemView.findViewById(R.id.hot_tour_love);
        }
    }
}
