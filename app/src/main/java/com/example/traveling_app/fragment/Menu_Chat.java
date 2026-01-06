package com.example.traveling_app.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveling_app.R;
import com.example.traveling_app.adapter.ChatAdapter;
import com.example.traveling_app.common.DatabaseReferences;
import com.example.traveling_app.common.GeminiService;
import com.example.traveling_app.model.chat.ChatMessage;
import com.example.traveling_app.model.tour.Tour;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Menu_Chat extends Fragment {

    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private TextInputEditText chatInput;
    private ImageButton sendButton;
    private ProgressBar loadingProgress;
    
    private GeminiService geminiService;
    private List<Tour> tourData;
    private Handler mainHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_chat, container, false);
        
        initViews(view);
        setupRecyclerView();
        setupClickListeners();
        loadTourData();
        
        // Show welcome message
        showWelcomeMessage();
        
        return view;
    }

    private void initViews(View view) {
        chatRecyclerView = view.findViewById(R.id.chat_recycler_view);
        chatInput = view.findViewById(R.id.chat_input);
        sendButton = view.findViewById(R.id.send_button);
        loadingProgress = view.findViewById(R.id.chat_loading);
        
        geminiService = new GeminiService();
        tourData = new ArrayList<>();
        mainHandler = new Handler(Looper.getMainLooper());
    }

    private void setupRecyclerView() {
        chatAdapter = new ChatAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true); // Start from bottom
        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setAdapter(chatAdapter);
    }

    private void setupClickListeners() {
        sendButton.setOnClickListener(v -> sendMessage());
        
        chatInput.setOnEditorActionListener((v, actionId, event) -> {
            sendMessage();
            return true;
        });
    }

    private void loadTourData() {
        DatabaseReferences.TOURS_DATABASE_REF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tourData.clear();
                for (DataSnapshot tourSnapshot : snapshot.getChildren()) {
                    Tour tour = tourSnapshot.getValue(Tour.class);
                    if (tour != null) {
                        tour.setId(tourSnapshot.getKey());
                        tourData.add(tour);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Không thể tải dữ liệu tour");
            }
        });
    }

    private void showWelcomeMessage() {
        ChatMessage welcomeMessage = new ChatMessage(
                "Xin chào! Tôi là trợ lý AI du lịch. Tôi có thể giúp bạn tìm kiếm tour, tư vấn chi phí và gợi ý các hoạt động du lịch. Hãy hỏi tôi bất cứ điều gì! 🌍✈️",
                ChatMessage.TYPE_BOT
        );
        chatAdapter.addMessage(welcomeMessage);
    }

    private void sendMessage() {
        String message = chatInput.getText().toString().trim();
        
        if (message.isEmpty()) {
            showToast("Vui lòng nhập câu hỏi");
            return;
        }
        
        // Add user message to chat
        ChatMessage userMessage = new ChatMessage(message, ChatMessage.TYPE_USER);
        chatAdapter.addMessage(userMessage);
        scrollToBottom();
        
        // Clear input
        chatInput.setText("");
        
        // Show loading
        showLoading(true);
        sendButton.setEnabled(false);
        
        // Call Gemini API
        geminiService.sendMessage(message, tourData, new GeminiService.ChatCallback() {
            @Override
            public void onSuccess(String response) {
                mainHandler.post(() -> {
                    showLoading(false);
                    sendButton.setEnabled(true);
                    
                    ChatMessage botMessage = new ChatMessage(response, ChatMessage.TYPE_BOT);
                    chatAdapter.addMessage(botMessage);
                    scrollToBottom();
                });
            }

            @Override
            public void onError(String error) {
                mainHandler.post(() -> {
                    showLoading(false);
                    sendButton.setEnabled(true);
                    
                    ChatMessage errorMessage = new ChatMessage(
                            "Xin lỗi, có lỗi xảy ra: " + error + " Vui lòng thử lại.",
                            ChatMessage.TYPE_BOT
                    );
                    chatAdapter.addMessage(errorMessage);
                    scrollToBottom();
                });
            }
        });
    }

    private void showLoading(boolean show) {
        loadingProgress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void scrollToBottom() {
        chatRecyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
