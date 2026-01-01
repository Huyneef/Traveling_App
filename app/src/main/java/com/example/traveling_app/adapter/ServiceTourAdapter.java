package com.example.traveling_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveling_app.R;
import com.example.traveling_app.model.other.Service;

import java.util.List;

public class ServiceTourAdapter extends RecyclerView.Adapter<ServiceTourAdapter.ServiceTourViewHolder>{
    private List<Service> services;
    private Context context;

    public ServiceTourAdapter(List<Service> services) {
        this.services = services;
        notifyDataSetChanged();
    }

    public ServiceTourAdapter( Context context,List<Service> services) {
        this.services = services;
        this.context = context;
    }

    @NonNull
    @Override
    public ServiceTourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.service_tour_item,parent,false);
        return new ServiceTourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceTourViewHolder holder, int position) {
        Service service=services.get(position);
        if (service==null)
            return ;

        holder.txtContent.setText(service.getContent());
        holder.img.setImageResource(service.getIcon());
    }

    @Override
    public int getItemCount() {
        return services==null?0:services.size();
    }

    public static class ServiceTourViewHolder extends RecyclerView.ViewHolder{
        private TextView txtContent;
        private ImageView img;
        public ServiceTourViewHolder(@NonNull View itemView) {
            super(itemView);
            txtContent=itemView.findViewById(R.id.content_service);
            img=itemView.findViewById(R.id.img_service);
        }
    }
}

