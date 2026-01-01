package com.example.traveling_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveling_app.activity.DetailActivity;
import com.example.traveling_app.R;
import com.example.traveling_app.common.ImageLoader;
import com.example.traveling_app.model.tour.Tour;

import java.util.List;

public class NearTourAdapter extends RecyclerView.Adapter<NearTourAdapter.NearTourViewHolder>{

    private List<Tour> tours;
    private Context context;

    public NearTourAdapter( Context context,List<Tour> tours) {
        this.tours = tours;
        this.context = context;
    }

    @NonNull
    @Override
    public NearTourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.near_tour_item,parent,false);
        return new NearTourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NearTourViewHolder holder, int position) {
        Tour tour=tours.get(position);
        if (tour==null)
            return;

        holder.txtTitle.setText(tour.getName());
        holder.txtContent.setText(tour.getContent()+"");
        holder.txtDistance.setText("5 km");
        holder.txtLocation.setText(tour.getAddress());
        ImageLoader.loadImage(tours.get(position).getMainImageUrl(), holder.img);
        holder.ln_nearTour.setOnClickListener(new View.OnClickListener() {
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

    public static class NearTourViewHolder extends RecyclerView.ViewHolder{

        private TextView txtTitle, txtContent, txtDistance, txtLocation;
        private ImageView img;
        private LinearLayout ln_nearTour;
        public NearTourViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle=itemView.findViewById(R.id.title_near);
            txtContent=itemView.findViewById(R.id.content_near);
            txtDistance=itemView.findViewById(R.id.distance_near);
            txtLocation=itemView.findViewById(R.id.location_near);
            img=itemView.findViewById(R.id.img_near);
            ln_nearTour=itemView.findViewById(R.id.ln_nearTour);
        }
    }
}
