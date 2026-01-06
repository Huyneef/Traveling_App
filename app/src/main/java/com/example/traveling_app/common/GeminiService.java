package com.example.traveling_app.common;

import android.util.Log;

import com.example.traveling_app.model.tour.Tour;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GeminiService {
    private static final String TAG = "GeminiService";
    // TODO: Replace with your Gemini API key from https://makersuite.google.com/app/apikey
    private static final String API_KEY = "AIzaSyD5chQKUxWbcj2BMP-SDAPLCKDOQ_iOYdI";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + API_KEY;
    
    private final OkHttpClient client;
    private final Gson gson;

    public interface ChatCallback {
        void onSuccess(String response);
        void onError(String error);
    }

    public GeminiService() {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        this.gson = new Gson();
    }

    public void sendMessage(String userMessage, List<Tour> tourData, ChatCallback callback) {
        // Build context with tour data
        StringBuilder context = new StringBuilder();
        context.append("Bạn là trợ lý AI chuyên về du lịch Việt Nam. ");
        context.append("Dưới đây là danh sách các tour du lịch hiện có trong hệ thống:\n\n");
        
        if (tourData != null && !tourData.isEmpty()) {
            int count = 1;
            for (Tour tour : tourData) {
                context.append(count++).append(". ");
                context.append("Tên: ").append(tour.getName()).append("\n");
                context.append("   Địa chỉ: ").append(tour.getAddress()).append("\n");
                context.append("   Giá: ").append(formatPrice(tour.getPrice())).append(" VNĐ\n");
                if (tour.getSalePrice() > 0 && tour.getSalePrice() < tour.getPrice()) {
                    context.append("   Giá khuyến mãi: ").append(formatPrice(tour.getSalePrice())).append(" VNĐ\n");
                }
                context.append("   Đánh giá: ").append(tour.getNumStar()).append(" sao\n");
                context.append("   Mô tả: ").append(tour.getContent()).append("\n\n");
            }
        } else {
            context.append("Hiện chưa có dữ liệu tour cụ thể trong hệ thống.\n\n");
        }
        
        context.append("Hãy trả lời câu hỏi sau của người dùng dựa trên thông tin tour trên. ");
        context.append("Nếu không có tour phù hợp, hãy gợi ý các địa điểm du lịch nổi tiếng ở Việt Nam ");
        context.append("và ước tính chi phí, thời gian phù hợp. Trả lời bằng tiếng Việt, thân thiện và ngắn gọn.\n\n");
        context.append("Câu hỏi: ").append(userMessage);

        // Build request JSON
        JsonObject requestJson = new JsonObject();
        JsonObject contents = new JsonObject();
        JsonObject parts = new JsonObject();
        parts.addProperty("text", context.toString());
        
        com.google.gson.JsonArray partsArray = new com.google.gson.JsonArray();
        partsArray.add(parts);
        contents.add("parts", partsArray);
        
        com.google.gson.JsonArray contentsArray = new com.google.gson.JsonArray();
        contentsArray.add(contents);
        requestJson.add("contents", contentsArray);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"),
                requestJson.toString()
        );

        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "API call failed", e);
                callback.onError("Không thể kết nối đến AI. Vui lòng thử lại.");
            }

            @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        Log.e(TAG, "API returned error: " + response.code());
                        callback.onError("Có lỗi xảy ra. Vui lòng thử lại sau.");
                        return;
                    }

                try {
                    String responseBody = response.body().string();
                    Log.d(TAG, "API Response: " + responseBody);
                    
                    JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
                    
                    if (jsonResponse.has("candidates")) {
                        com.google.gson.JsonArray candidates = jsonResponse.getAsJsonArray("candidates");
                        if (candidates.size() > 0) {
                            JsonObject candidate = candidates.get(0).getAsJsonObject();
                            JsonObject content = candidate.getAsJsonObject("content");
                            com.google.gson.JsonArray parts = content.getAsJsonArray("parts");
                            if (parts.size() > 0) {
                                String text = parts.get(0).getAsJsonObject().get("text").getAsString();
                                callback.onSuccess(text);
                                return;
                            }
                        }
                    }
                    
                    callback.onError("Không nhận được phản hồi từ AI.");
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing response", e);
                    callback.onError("Lỗi xử lý dữ liệu. Vui lòng thử lại.");
                }
            }
        });
    }

    private String formatPrice(double price) {
        return String.format("%,.0f", price);
    }
}
