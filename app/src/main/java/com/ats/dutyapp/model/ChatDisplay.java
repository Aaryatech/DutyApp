package com.ats.dutyapp.model;

public class ChatDisplay {

    private int chatTaskDetailId;
    private int headerId;
    private int typeOfText;
    private String textValue;
    private String dateTime;
    private int userId;
    private String userName;
    private int delStatus;
    private int markAsRead;
    private int syncStatus;
    private int offlineStatus;

    private int replyToId;
    private int replyToMsgType;
    private String replyToMsg;
    private String replyToName;

    public ChatDisplay() {
    }

    public ChatDisplay(int chatTaskDetailId) {
        this.chatTaskDetailId = chatTaskDetailId;
    }

    public ChatDisplay(int chatTaskDetailId, int headerId, int typeOfText, String textValue, String dateTime, int userId, String userName, int delStatus, int markAsRead) {
        this.chatTaskDetailId = chatTaskDetailId;
        this.headerId = headerId;
        this.typeOfText = typeOfText;
        this.textValue = textValue;
        this.dateTime = dateTime;
        this.userId = userId;
        this.userName = userName;
        this.delStatus = delStatus;
        this.markAsRead = markAsRead;
    }

   /* public ChatDisplay(int chatTaskDetailId, int headerId, int typeOfText, String textValue, String dateTime, int userId, String userName, int delStatus, int markAsRead, int syncStatus) {
        this.chatTaskDetailId = chatTaskDetailId;
        this.headerId = headerId;
        this.typeOfText = typeOfText;
        this.textValue = textValue;
        this.dateTime = dateTime;
        this.userId = userId;
        this.userName = userName;
        this.delStatus = delStatus;
        this.markAsRead = markAsRead;
        this.syncStatus = syncStatus;
    }*/

   public ChatDisplay(int chatTaskDetailId, int headerId, int typeOfText, String textValue, String dateTime, int userId, String userName, int delStatus, int markAsRead, int syncStatus, int offlineStatus) {
        this.chatTaskDetailId = chatTaskDetailId;
        this.headerId = headerId;
        this.typeOfText = typeOfText;
        this.textValue = textValue;
        this.dateTime = dateTime;
        this.userId = userId;
        this.userName = userName;
        this.delStatus = delStatus;
        this.markAsRead = markAsRead;
        this.syncStatus = syncStatus;
        this.offlineStatus = offlineStatus;
    }

    //21-11-2019
    public ChatDisplay(int chatTaskDetailId, int headerId, int typeOfText, String textValue, String dateTime, int userId, String userName, int delStatus, int markAsRead, int syncStatus, int offlineStatus,int replyToId,int replyToMsgType,String replyToMsg,String replyToName) {
        this.chatTaskDetailId = chatTaskDetailId;
        this.headerId = headerId;
        this.typeOfText = typeOfText;
        this.textValue = textValue;
        this.dateTime = dateTime;
        this.userId = userId;
        this.userName = userName;
        this.delStatus = delStatus;
        this.markAsRead = markAsRead;
        this.syncStatus = syncStatus;
        this.offlineStatus = offlineStatus;
        this.replyToId=replyToId;
        this.replyToMsgType=replyToMsgType;
        this.replyToMsg=replyToMsg;
        this.replyToName=replyToName;
    }

    public int getChatTaskDetailId() {
        return chatTaskDetailId;
    }

    public void setChatTaskDetailId(int chatTaskDetailId) {
        this.chatTaskDetailId = chatTaskDetailId;
    }

    public int getHeaderId() {
        return headerId;
    }

    public void setHeaderId(int headerId) {
        this.headerId = headerId;
    }

    public int getTypeOfText() {
        return typeOfText;
    }

    public void setTypeOfText(int typeOfText) {
        this.typeOfText = typeOfText;
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(int delStatus) {
        this.delStatus = delStatus;
    }

    public int getMarkAsRead() {
        return markAsRead;
    }

    public void setMarkAsRead(int markAsRead) {
        this.markAsRead = markAsRead;
    }

    public int getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(int syncStatus) {
        this.syncStatus = syncStatus;
    }

    public int getOfflineStatus() {
        return offlineStatus;
    }

    public void setOfflineStatus(int offlineStatus) {
        this.offlineStatus = offlineStatus;
    }

    public int getReplyToId() {
        return replyToId;
    }

    public void setReplyToId(int replyToId) {
        this.replyToId = replyToId;
    }

    public String getReplyToMsg() {
        return replyToMsg;
    }

    public void setReplyToMsg(String replyToMsg) {
        this.replyToMsg = replyToMsg;
    }

    public String getReplyToName() {
        return replyToName;
    }

    public void setReplyToName(String replyToName) {
        this.replyToName = replyToName;
    }

    public int getReplyToMsgType() {
        return replyToMsgType;
    }

    public void setReplyToMsgType(int replyToMsgType) {
        this.replyToMsgType = replyToMsgType;
    }

    @Override
    public String toString() {
        return "ChatDisplay{" +
                "chatTaskDetailId=" + chatTaskDetailId +
                ", headerId=" + headerId +
                ", typeOfText=" + typeOfText +
                ", textValue='" + textValue + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", delStatus=" + delStatus +
                ", markAsRead=" + markAsRead +
                ", syncStatus=" + syncStatus +
                ", offlineStatus=" + offlineStatus +
                ", replyToId=" + replyToId +
                ", replyToMsgType=" + replyToMsgType +
                ", replyToMsg='" + replyToMsg + '\'' +
                ", replyToName='" + replyToName + '\'' +
                '}';
    }
}
