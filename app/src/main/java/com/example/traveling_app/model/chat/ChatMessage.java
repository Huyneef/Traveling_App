package com.example.traveling_app.model.chat;

public class ChatMessage {
    public static final int TYPE_USER = 0;
    public static final int TYPE_BOT = 1;

    private String message;
    private int type; // 0 for user, 1 for bot
    private long timestamp;

    public ChatMessage() {
    }

    public ChatMessage(String message, int type) {
        this.message = message;
        this.type = type;
        this.timestamp = System.currentTimeMillis();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isUser() {
        return type == TYPE_USER;
    }

    public boolean isBot() {
        return type == TYPE_BOT;
    }
}
