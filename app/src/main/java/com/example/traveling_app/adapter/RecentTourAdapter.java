package com.example.traveling_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveling_app.activity.DetailActivity;
import com.example.traveling_app.R;
import com.example.traveling_app.common.ImageLoader;
import com.example.traveling_app.model.tour.Tour;

import java.text.DecimalFormat;
import java.util.List;

public class RecentTourAdapter extends RecyclerView.Adapter<RecentTourAdapter.RecentTourViewHolder>{
    private List<Tour> tours;
    private Context context;

    public RecentTourAdapter(Context context, List<Tour> tours) {
        this.tours = tours;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecentTourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_tour_item,parent,false);
        return new RecentTourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentTourViewHolder holder, int position) {
        Tour tour=tours.get(position);
        if (tour==null)
            return;

        DecimalFormat formatter = new DecimalFormat("###,###,###");
        DecimalFormat rateFormatter = new DecimalFormat("#.##");
        holder.txtTitle.setText(tour.getName());
        holder.txtAddress.setText(tour.getAddress());
        holder.txtBook.setText(tour.getNumBooking()+" đã đặt");
        holder.txtRate.setText(rateFormatter.format(tour.getNumStar()).replace(',','.'));
        holder.txtComment.setText("(" +tour.getNumComment()+" đánh giá)");
        holder.txtPrice.setText(formatter.format(tour.getPrice())+" đ");
        holder.txtSale.setText(formatter.format(tour.getSalePrice())+" đ");
        ImageLoader.loadImage(tours.get(position).getMainImageUrl(), holder.img);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, DetailActivity.class);
                intent.putExtra("id", tour.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tours==null?0:tours.size();
    }


    public static class RecentTourViewHolder extends RecyclerView.ViewHolder{
        private TextView txtTitle, txtAddress, txtBook, txtRate, txtComment,txtPrice,txtSale;
        private ImageView img;
        private CardView cardView;
        public RecentTourViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.main_card);
            txtTitle=itemView.findViewById(R.id.recent_title);
            txtAddress=itemView.findViewById(R.id.recent_address);
            txtBook=itemView.findViewById(R.id.recent_book);
            txtRate=itemView.findViewById(R.id.recent_rate);
            txtComment=itemView.findViewById(R.id.recent_comment);
            txtPrice=itemView.findViewById(R.id.recent_price);
            txtSale=itemView.findViewById(R.id.recent_sale_price);
            img=itemView.findViewById(R.id.recent_image);
        }
    }
}
