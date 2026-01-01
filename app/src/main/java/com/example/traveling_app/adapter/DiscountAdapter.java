package com.example.traveling_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.traveling_app.R;
import com.example.traveling_app.model.tour.Discount;
import com.example.traveling_app.activity.BookTourActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.card.MaterialCardView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DiscountAdapter extends FirebaseRecyclerAdapter<Discount, DiscountAdapter.myviewholder> {

    private Context context;
        public DiscountAdapter(@NonNull FirebaseRecyclerOptions<Discount> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull Discount model) {
        holder.discount.setText(model.getDiscount()+"");
        holder.numStar.setText(model.getNumStar()+"");
        holder.efDatetime.setText(model.getEfDatetime());
        Glide.with(holder.discount_img.getContext()).load(model.getpUrl()).into(holder.discount_img);

        holder.item_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String saleprice = model.getDiscount()+"";
                Intent intent=new Intent(context, BookTourActivity.class);
                intent.putExtra("key_saleprice",saleprice);
                intent.putExtra("key_id",model.getId());
                context.startActivity(intent);
            }
        });
    }
    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_magiamgia,parent,false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder {

        private ImageView discount_img;
        private TextView discount,numStar,efDatetime;
        private MaterialCardView item_voucher;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            discount_img = itemView.findViewById(R.id.discount_img);
            discount = itemView.findViewById(R.id.discount);
            numStar = itemView.findViewById(R.id.numStar);
            efDatetime = itemView.findViewById(R.id.efDatetime);
            item_voucher = itemView.findViewById(R.id.item_voucher);
        }
    }

}
