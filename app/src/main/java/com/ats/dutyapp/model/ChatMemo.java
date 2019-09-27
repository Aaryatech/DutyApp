package com.ats.dutyapp.model;

public class ChatMemo {

    private int memoId;
    private int userId;
    private int taskHeaderId;
    private int generatedUserId;
    private String memoDesc;
    private String memoDate;
    private int status;
    private int delStatus;
    private Integer exInt1;
    private Integer exInt2;
    private Integer exInt3;
    private String exVar1;
    private String exVar2;
    private String exVar3;

    public ChatMemo(int memoId, int userId, int taskHeaderId, int generatedUserId, String memoDesc, String memoDate, int status, int delStatus, Integer exInt1, Integer exInt2, Integer exInt3, String exVar1, String exVar2, String exVar3) {
        this.memoId = memoId;
        this.userId = userId;
        this.taskHeaderId = taskHeaderId;
        this.generatedUserId = generatedUserId;
        this.memoDesc = memoDesc;
        this.memoDate = memoDate;
        this.status = status;
        this.delStatus = delStatus;
        this.exInt1 = exInt1;
        this.exInt2 = exInt2;
        this.exInt3 = exInt3;
        this.exVar1 = exVar1;
        this.exVar2 = exVar2;
        this.exVar3 = exVar3;
    }

    public int getMemoId() {
        return memoId;
    }

    public void setMemoId(int memoId) {
        this.memoId = memoId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTaskHeaderId() {
        return taskHeaderId;
    }

    public void setTaskHeaderId(int taskHeaderId) {
        this.taskHeaderId = taskHeaderId;
    }

    public int getGeneratedUserId() {
        return generatedUserId;
    }

    public void setGeneratedUserId(int generatedUserId) {
        this.generatedUserId = generatedUserId;
    }

    public String getMemoDesc() {
        return memoDesc;
    }

    public void setMemoDesc(String memoDesc) {
        this.memoDesc = memoDesc;
    }

    public String getMemoDate() {
        return memoDate;
    }

    public void setMemoDate(String memoDate) {
        this.memoDate = memoDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(int delStatus) {
        this.delStatus = delStatus;
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

    @Override
    public String toString() {
        return "ChatMemo{" +
                "memoId=" + memoId +
                ", userId=" + userId +
                ", taskHeaderId=" + taskHeaderId +
                ", generatedUserId=" + generatedUserId +
                ", memoDesc='" + memoDesc + '\'' +
                ", memoDate='" + memoDate + '\'' +
                ", status=" + status +
                ", delStatus=" + delStatus +
                ", exInt1=" + exInt1 +
                ", exInt2=" + exInt2 +
                ", exInt3=" + exInt3 +
                ", exVar1='" + exVar1 + '\'' +
                ", exVar2='" + exVar2 + '\'' +
                ", exVar3='" + exVar3 + '\'' +
                '}';
    }
}
