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

public class HintTourAdapter extends RecyclerView.Adapter<HintTourAdapter.TourHintViewHolder>{

    private List<Tour> tours;
    private Context context;
    public HintTourAdapter(Context context, List<Tour> tours) {
        this.tours = tours;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TourHintViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.hint_tour_item,parent,false);
        return new TourHintViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TourHintViewHolder holder, int position) {
        Tour tour=tours.get(position);
        if (tour==null)
            return;
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        DecimalFormat rateFormatter = new DecimalFormat("#.##");
        holder.txtName.setText(tour.getName());
        holder.txtRate.setText(rateFormatter.format(tour.getNumStar()).replace(',','.'));
        holder.txtPrice.setText(formatter.format(tour.getSalePrice())+" đ");
        ImageLoader.loadImage(tours.get(position).getMainImageUrl(), holder.img);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, DetailActivity.class);
                intent.putExtra("id", tour.getId());
                context.startActivity(intent);
            }
        });

        CheckLove.isLoved(CurrentUser.getCurrentUser().getUsername(),tour.getId(),holder.hint_tour_love);
        CheckLove.saveData(CurrentUser.getCurrentUser().getUsername(),tour.getId(),holder.hint_tour_love);


    }
    @Override
    public int getItemCount() {
        return tours==null?0:tours.size();
    }

    public static class TourHintViewHolder extends RecyclerView.ViewHolder{
        private TextView txtName, txtRate, txtPrice;
        private ImageView img,hint_tour_love;
        public TourHintViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName=itemView.findViewById(R.id.title_hint);
            txtRate=itemView.findViewById(R.id.rate_hint);
            txtPrice=itemView.findViewById(R.id.price_hint);
            img=itemView.findViewById(R.id.image_hint);
            hint_tour_love=itemView.findViewById(R.id.hint_tour_love);
        }
    }
}
