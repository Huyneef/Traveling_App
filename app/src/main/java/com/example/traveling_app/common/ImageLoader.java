package com.example.traveling_app.common;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ImageLoader {

    // Số lượng luồng tối đa trong pool cùng 1 thời điểm
    private static final int MAX_POOL_SIZE = 5;

    // Thời gian chờ công việc trong queue, nếu hết thời gian này hủy công việc
    private static final int KEEP_ALIVE_TIME = 10;

    // Đơn vị thời gian của KEEP_ALIVE_TIME
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    // Executor để quản lý thread pool
    private static final Executor executor = new ThreadPoolExecutor(
            0,
            MAX_POOL_SIZE,
            KEEP_ALIVE_TIME,
            KEEP_ALIVE_TIME_UNIT,
            new LinkedBlockingQueue<>()
    );

    // Phương thức để load ảnh sử dụng Glide trong thread pool
    public static void loadImage(String imageUrl, ImageView imageView) {
        // Sử dụng Glide trong executor
        executor.execute(() -> {
            try {
                Bitmap bitmap = Glide.with(imageView.getContext())
                        .asBitmap()
                        .load(imageUrl)
                        .submit()
                        .get();

                // Hiển thị ảnh trong UI thread
                imageView.post(() -> imageView.setImageBitmap(bitmap));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
