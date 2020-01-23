package com.ats.dutyapp.model;

public class ChatDetail {

    private int chatTaskDetailId;
    private int headerId;
    private int typeOfText;
    private String textValue;
    private String localDate;
    private String serverDate;
    private int userId;
    private String userName;
    private int delStatus;
    private int markAsRead;
    private Integer exInt1;
    private Integer exInt2;
    private Integer exInt3;
    private String exVar1;
    private String exVar2;
    private String exVar3;

    private int replyToMsgType;
    private String replyToMsg;
    private String replyToName;


    public ChatDetail() {
    }

    public ChatDetail(int chatTaskDetailId) {
        this.chatTaskDetailId = chatTaskDetailId;
    }

    public ChatDetail(int chatTaskDetailId, int headerId, int typeOfText, String textValue, String localDate, String serverDate, int userId, String userName, int delStatus, int markAsRead,int exInt1) {
        this.chatTaskDetailId = chatTaskDetailId;
        this.headerId = headerId;
        this.typeOfText = typeOfText;
        this.textValue = textValue;
        this.localDate = localDate;
        this.serverDate = serverDate;
        this.userId = userId;
        this.userName = userName;
        this.delStatus = delStatus;
        this.markAsRead = markAsRead;
        this.exInt1=exInt1;
    }

    public ChatDetail(int chatTaskDetailId, int headerId, int typeOfText, String textValue, String localDate, String serverDate, int userId, String userName, int delStatus, int markAsRead,String exVar1,int exInt1) {
        this.chatTaskDetailId = chatTaskDetailId;
        this.headerId = headerId;
        this.typeOfText = typeOfText;
        this.textValue = textValue;
        this.localDate = localDate;
        this.serverDate = serverDate;
        this.userId = userId;
        this.userName = userName;
        this.delStatus = delStatus;
        this.markAsRead = markAsRead;
        this.exVar1=exVar1;
        this.exInt1=exInt1;
    }

    //21-11-2019
    public ChatDetail(int chatTaskDetailId, int headerId, int typeOfText, String textValue, String localDate, String serverDate, int userId, String userName, int delStatus, int markAsRead,String exVar1,int exInt1,int replyToMsgType,String replyToMsg,String replyToName) {
        this.chatTaskDetailId = chatTaskDetailId;
        this.headerId = headerId;
        this.typeOfText = typeOfText;
        this.textValue = textValue;
        this.localDate = localDate;
        this.serverDate = serverDate;
        this.userId = userId;
        this.userName = userName;
        this.delStatus = delStatus;
        this.markAsRead = markAsRead;
        this.exVar1=exVar1;
        this.exInt1=exInt1;
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

    public String getLocalDate() {
        return localDate;
    }

    public void setLocalDate(String localDate) {
        this.localDate = localDate;
    }

    public String getServerDate() {
        return serverDate;
    }

    public void setServerDate(String serverDate) {
        this.serverDate = serverDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public Integer getExInt1() {
        return exInt1;
    }

    public void setExInt1(Integer exInt1) {
        this.exInt1 = exInt1;
    }

    public Integer getExInt2() {
        return exInt2;
    }

    public void setExInt2(Integer exInt2) {
        this.exInt2 = exInt2;
    }

    public Integer getExInt3() {
        return exInt3;
    }

    public void setExInt3(Integer exInt3) {
        this.exInt3 = exInt3;
    }

    public String getExVar1() {
        return exVar1;
    }

    public void setExVar1(String exVar1) {
        this.exVar1 = exVar1;
    }

    public String getExVar2() {
        return exVar2;
    }

    public void setExVar2(String exVar2) {
        this.exVar2 = exVar2;
    }

    public String getExVar3() {
        return exVar3;
    }

    public void setExVar3(String exVar3) {
        this.exVar3 = exVar3;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public int getReplyToMsgType() {
        return replyToMsgType;
    }

    public void setReplyToMsgType(int replyToMsgType) {
        this.replyToMsgType = replyToMsgType;
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

    @Override
    public String toString() {
        return "ChatDetail{" +
                "chatTaskDetailId=" + chatTaskDetailId +
                ", headerId=" + headerId +
                ", typeOfText=" + typeOfText +
                ", textValue='" + textValue + '\'' +
                ", localDate='" + localDate + '\'' +
                ", serverDate='" + serverDate + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", delStatus=" + delStatus +
                ", markAsRead=" + markAsRead +
                ", exInt1=" + exInt1 +
                ", exInt2=" + exInt2 +
                ", exInt3=" + exInt3 +
                ", exVar1='" + exVar1 + '\'' +
                ", exVar2='" + exVar2 + '\'' +
                ", exVar3='" + exVar3 + '\'' +
                ", replyToMsgType=" + replyToMsgType +
                ", replyToMsg='" + replyToMsg + '\'' +
                ", replyToName='" + replyToName + '\'' +
                '}';
    }
}
