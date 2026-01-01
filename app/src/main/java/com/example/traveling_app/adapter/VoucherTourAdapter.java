package com.example.traveling_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.traveling_app.R;
import com.example.traveling_app.model.tour.Discount;

import java.util.List;

public class VoucherTourAdapter extends RecyclerView.Adapter<VoucherTourAdapter.VoucherTourViewHolder>{
    private List<Discount> discounts;
    private Context context;

    public VoucherTourAdapter(Context context, List<Discount> discounts) {
        this.discounts = discounts;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VoucherTourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.voucher_tour_item,parent,false);
        return new VoucherTourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherTourViewHolder holder, int position) {
        Discount discount=discounts.get(position);
        if (discount==null)
            return;
        Glide.with(context).load(discount.getpUrl()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return discounts==null?0:discounts.size();
    }

    public static class VoucherTourViewHolder extends RecyclerView.ViewHolder{
        private ImageView img;
        public VoucherTourViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.img_voucher);
        }
    }
}
