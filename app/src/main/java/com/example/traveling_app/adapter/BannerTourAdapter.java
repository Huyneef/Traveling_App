package com.example.traveling_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.traveling_app.activity.DetailActivity;
import com.example.traveling_app.R;
import com.example.traveling_app.common.ImageLoader;
import com.example.traveling_app.model.tour.Tour;

import java.text.DecimalFormat;
import java.util.List;

public class BannerTourAdapter {

    private List<Tour> tours;
    private Context context;
    private ViewFlipper viewFlipper;

    public BannerTourAdapter(Context context, List<Tour> tours, ViewFlipper viewFlipper) {
        this.tours = tours;
        this.context = context;
        this.viewFlipper = viewFlipper;
        setupFlipper();
    }

    private void setupFlipper() {
        DecimalFormat formatter = new DecimalFormat("###,###,###");

        for (Tour tour : tours) {
            View inflate = LayoutInflater.from(context).inflate(R.layout.banner_tour_item, null);
            TextView txtTitle = inflate.findViewById(R.id.title_banner);
            TextView txtContent = inflate.findViewById(R.id.content_banner);
            TextView txtPrice = inflate.findViewById(R.id.price_banner);
            ImageView img = inflate.findViewById(R.id.img_banner);

            // Cập nhật nội dung
            if (tour.getContent().length() > 32)
                txtContent.setText(tour.getContent().substring(0, 32) + "...");
            else
                txtContent.setText(tour.getContent());
            txtTitle.setText(tour.getName());
            txtPrice.setText(formatter.format(tour.getPrice()) + " đ");

            // Tải hình ảnh
            ImageLoader.loadImage(tour.getMainImageUrl(), img);

            // Thiết lập click listener
            img.setOnClickListener(v -> {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("id", tour.getId());
                context.startActivity(intent);
            });

            // Thêm view vào ViewFlipper
            viewFlipper.addView(inflate);
        }

        // Bắt đầu tự động chuyển đổi giữa các hình ảnh
        viewFlipper.setInAnimation(context, android.R.anim.fade_in);
        viewFlipper.setOutAnimation(context, android.R.anim.fade_out);
        viewFlipper.startFlipping();
    }
}
