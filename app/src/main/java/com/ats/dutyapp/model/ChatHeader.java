package com.ats.dutyapp.model;

public class ChatHeader {

    private int headerId;
    private String createdDate;
    private String headerName;
    private int createdUserId;
    private String adminUserIds;
    private String assignUserIds;
    private String taskDesc;
    private String image;
    private int status;
    private int requestUserId;
    private int taskCloseUserId;
    private String taskCompleteRemark;
    private int isReminderRequired;
    private String reminderFrequency;
    private String lastDate;
    private int isActive;
    private int delStatus;
    private Integer exInt1;
    private Integer exInt2;
    private Integer exInt3;
    private String exVar1;
    private String exVar2;
    private String exVar3;

    public ChatHeader(int headerId, String createdDate, String headerName, int createdUserId, String adminUserIds, String assignUserIds, String taskDesc, String image, int status, int requestUserId, int taskCloseUserId, String taskCompleteRemark, int isReminderRequired, String reminderFrequency, String lastDate, int isActive, int delStatus, Integer exInt1, Integer exInt2, Integer exInt3, String exVar1, String exVar2, String exVar3) {
        this.headerId = headerId;
        this.createdDate = createdDate;
        this.headerName = headerName;
        this.createdUserId = createdUserId;
        this.adminUserIds = adminUserIds;
        this.assignUserIds = assignUserIds;
        this.taskDesc = taskDesc;
        this.image = image;
        this.status = status;
        this.requestUserId = requestUserId;
        this.taskCloseUserId = taskCloseUserId;
        this.taskCompleteRemark = taskCompleteRemark;
        this.isReminderRequired = isReminderRequired;
        this.reminderFrequency = reminderFrequency;
        this.lastDate = lastDate;
        this.isActive = isActive;
        this.delStatus = delStatus;
        this.exInt1 = exInt1;
        this.exInt2 = exInt2;
        this.exInt3 = exInt3;
        this.exVar1 = exVar1;
        this.exVar2 = exVar2;
        this.exVar3 = exVar3;
    }

    public int getHeaderId() {
        return headerId;
    }

    public void setHeaderId(int headerId) {
        this.headerId = headerId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public int getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(int createdUserId) {
        this.createdUserId = createdUserId;
    }

    public String getAdminUserIds() {
        return adminUserIds;
    }

    public void setAdminUserIds(String adminUserIds) {
        this.adminUserIds = adminUserIds;
    }

    public String getAssignUserIds() {
        return assignUserIds;
    }

    public void setAssignUserIds(String assignUserIds) {
        this.assignUserIds = assignUserIds;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRequestUserId() {
        return requestUserId;
    }

    public void setRequestUserId(int requestUserId) {
        this.requestUserId = requestUserId;
    }

    public int getTaskCloseUserId() {
        return taskCloseUserId;
    }

    public void setTaskCloseUserId(int taskCloseUserId) {
        this.taskCloseUserId = taskCloseUserId;
    }

    public String getTaskCompleteRemark() {
        return taskCompleteRemark;
    }

    public void setTaskCompleteRemark(String taskCompleteRemark) {
        this.taskCompleteRemark = taskCompleteRemark;
    }

    public int getIsReminderRequired() {
        return isReminderRequired;
    }

    public void setIsReminderRequired(int isReminderRequired) {
        this.isReminderRequired = isReminderRequired;
    }

    public String getReminderFrequency() {
        return reminderFrequency;
    }

    public void setReminderFrequency(String reminderFrequency) {
        this.reminderFrequency = reminderFrequency;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
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
        return "ChatHeader{" +
                "headerId=" + headerId +
                ", createdDate='" + createdDate + '\'' +
                ", headerName='" + headerName + '\'' +
                ", createdUserId=" + createdUserId +
                ", adminUserIds='" + adminUserIds + '\'' +
                ", assignUserIds='" + assignUserIds + '\'' +
                ", taskDesc='" + taskDesc + '\'' +
                ", image='" + image + '\'' +
                ", status=" + status +
                ", requestUserId=" + requestUserId +
                ", taskCloseUserId=" + taskCloseUserId +
                ", taskCompleteRemark='" + taskCompleteRemark + '\'' +
                ", isReminderRequired=" + isReminderRequired +
                ", reminderFrequency=" + reminderFrequency +
                ", lastDate=" + lastDate +
                ", isActive=" + isActive +
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
