package com.ats.dutyapp.model;

public class ChatDetailIdListByReadStatus {

    private Integer chatTaskDetailId;

    public Integer getChatTaskDetailId() {
        return chatTaskDetailId;
    }

    public void setChatTaskDetailId(Integer chatTaskDetailId) {
        this.chatTaskDetailId = chatTaskDetailId;
    }

    @Override
    public String toString() {
        return "ChatDetailIdListByReadStatus{" +
                "chatTaskDetailId=" + chatTaskDetailId +
                '}';
    }
}
