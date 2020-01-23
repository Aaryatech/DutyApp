package com.ats.dutyapp.model;

public class MemoGenerated {

    private Integer memoId;
    private Integer userId;
    private String userName;
    private Integer generatedUserId;
    private String generatedUserName;
    private String memoDesc;
    private String memoDate;
    private Integer taskHeaderId;
    private String headerName;

    public Integer getMemoId() {
        return memoId;
    }

    public void setMemoId(Integer memoId) {
        this.memoId = memoId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getGeneratedUserId() {
        return generatedUserId;
    }

    public void setGeneratedUserId(Integer generatedUserId) {
        this.generatedUserId = generatedUserId;
    }

    public String getGeneratedUserName() {
        return generatedUserName;
    }

    public void setGeneratedUserName(String generatedUserName) {
        this.generatedUserName = generatedUserName;
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

    public Integer getTaskHeaderId() {
        return taskHeaderId;
    }

    public void setTaskHeaderId(Integer taskHeaderId) {
        this.taskHeaderId = taskHeaderId;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    @Override
    public String toString() {
        return "MemoGenerated{" +
                "memoId=" + memoId +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", generatedUserId=" + generatedUserId +
                ", generatedUserName='" + generatedUserName + '\'' +
                ", memoDesc='" + memoDesc + '\'' +
                ", memoDate='" + memoDate + '\'' +
                ", taskHeaderId=" + taskHeaderId +
                ", headerName='" + headerName + '\'' +
                '}';
    }
}
