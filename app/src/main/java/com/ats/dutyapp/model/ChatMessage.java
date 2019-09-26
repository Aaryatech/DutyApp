package com.ats.dutyapp.model;

public class ChatMessage {
    public boolean left;
    public String message;

    public ChatMessage(boolean left, String message) {
        this.left = left;
        this.message = message;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "left=" + left +
                ", message='" + message + '\'' +
                '}';
    }
}
