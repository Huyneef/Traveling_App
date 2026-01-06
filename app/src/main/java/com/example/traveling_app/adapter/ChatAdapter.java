package com.example.traveling_app.adapter;

import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveling_app.R;
import com.example.traveling_app.model.chat.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ChatMessage> messages;
    private SimpleDateFormat timeFormat;

    public ChatAdapter() {
        this.messages = new ArrayList<>();
        this.timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ChatMessage.TYPE_USER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_user, parent, false);
            return new UserMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_bot, parent, false);
            return new BotMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        String time = timeFormat.format(new Date(message.getTimestamp()));

        if (holder instanceof UserMessageViewHolder) {
            ((UserMessageViewHolder) holder).bind(message, time);
        } else if (holder instanceof BotMessageViewHolder) {
            ((BotMessageViewHolder) holder).bind(message, time);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    public void clearMessages() {
        messages.clear();
        notifyDataSetChanged();
    }

    /**
     * Format markdown text to styled text
     * Converts **bold** and *italic* markers to styled spans
     */
    private static CharSequence formatMarkdown(String text) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        
        // Handle **bold** text
        Pattern boldPattern = Pattern.compile("\\*\\*(.*?)\\*\\*");
        Matcher boldMatcher = boldPattern.matcher(text);
        int offset = 0;
        
        while (boldMatcher.find()) {
            int start = boldMatcher.start() - offset;
            int end = boldMatcher.end() - offset;
            String content = boldMatcher.group(1);
            
            // Replace **text** with text and apply bold style
            builder.replace(start, end, content);
            builder.setSpan(new StyleSpan(Typeface.BOLD), start, start + content.length(), 
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            
            offset += 4; // Account for removed ** markers (2 at start + 2 at end)
        }
        
        // Handle *italic* text (but not ** which was already processed)
        text = builder.toString();
        Pattern italicPattern = Pattern.compile("(?<!\\*)\\*(?!\\*)(.*?)(?<!\\*)\\*(?!\\*)");
        Matcher italicMatcher = italicPattern.matcher(text);
        offset = 0;
        
        while (italicMatcher.find()) {
            int start = italicMatcher.start() - offset;
            int end = italicMatcher.end() - offset;
            String content = italicMatcher.group(1);
            
            // Replace *text* with text and apply italic style
            builder.replace(start, end, content);
            builder.setSpan(new StyleSpan(Typeface.ITALIC), start, start + content.length(), 
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            
            offset += 2; // Account for removed * markers
        }
        
        return builder;
    }

    // ViewHolder for user messages
    static class UserMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        UserMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.user_message_text);
            timeText = itemView.findViewById(R.id.user_message_time);
        }

        void bind(ChatMessage message, String time) {
            messageText.setText(message.getMessage());
            timeText.setText(time);
        }
    }

    // ViewHolder for bot messages
    static class BotMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        BotMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.bot_message_text);
            timeText = itemView.findViewById(R.id.bot_message_time);
        }

        void bind(ChatMessage message, String time) {
            messageText.setText(formatMarkdown(message.getMessage()));
            timeText.setText(time);
        }
    }
}
