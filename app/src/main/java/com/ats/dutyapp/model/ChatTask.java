package com.ats.dutyapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatTask {
    @SerializedName("headerId")
    @Expose
    private Integer headerId;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("headerName")
    @Expose
    private String headerName;
    @SerializedName("createdUserId")
    @Expose
    private Integer createdUserId;
    @SerializedName("adminUserIds")
    @Expose
    private String adminUserIds;
    @SerializedName("assignUserIds")
    @Expose
    private String assignUserIds;
    @SerializedName("taskDesc")
    @Expose
    private String taskDesc;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("requestUserId")
    @Expose
    private Integer requestUserId;
    @SerializedName("taskCloseUserId")
    @Expose
    private Integer taskCloseUserId;
    @SerializedName("taskCompleteRemark")
    @Expose
    private String taskCompleteRemark;
    @SerializedName("isReminderRequired")
    @Expose
    private Integer isReminderRequired;
    @SerializedName("reminderFrequency")
    @Expose
    private Integer reminderFrequency;
    @SerializedName("lastDate")
    @Expose
    private Integer lastDate;
    @SerializedName("isActive")
    @Expose
    private Integer isActive;
    @SerializedName("delStatus")
    @Expose
    private Integer delStatus;
    @SerializedName("exInt1")
    @Expose
    private Integer exInt1;
    @SerializedName("exInt2")
    @Expose
    private Integer exInt2;
    @SerializedName("exInt3")
    @Expose
    private Integer exInt3;
    @SerializedName("exVar1")
    @Expose
    private String exVar1;
    @SerializedName("exVar2")
    @Expose
    private String exVar2;
    @SerializedName("exVar3")
    @Expose
    private String exVar3;
    @SerializedName("createdByName")
    @Expose
    private String createdByName;
    @SerializedName("adminUserNames")
    @Expose
    private String adminUserNames;
    @SerializedName("assignUserNames")
    @Expose
    private String assignUserNames;
    @SerializedName("requestUserName")
    @Expose
    private Object requestUserName;
    @SerializedName("taskCloseUserName")
    @Expose
    private Object taskCloseUserName;
    @SerializedName("privilege")
    @Expose
    private Integer privilege;

    public Integer getHeaderId() {
        return headerId;
    }

    public void setHeaderId(Integer headerId) {
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

    public Integer getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(Integer createdUserId) {
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getRequestUserId() {
        return requestUserId;
    }

    public void setRequestUserId(Integer requestUserId) {
        this.requestUserId = requestUserId;
    }

    public Integer getTaskCloseUserId() {
        return taskCloseUserId;
    }

    public void setTaskCloseUserId(Integer taskCloseUserId) {
        this.taskCloseUserId = taskCloseUserId;
    }

    public String getTaskCompleteRemark() {
        return taskCompleteRemark;
    }

    public void setTaskCompleteRemark(String taskCompleteRemark) {
        this.taskCompleteRemark = taskCompleteRemark;
    }

    public Integer getIsReminderRequired() {
        return isReminderRequired;
    }

    public void setIsReminderRequired(Integer isReminderRequired) {
        this.isReminderRequired = isReminderRequired;
    }

    public Integer getReminderFrequency() {
        return reminderFrequency;
    }

    public void setReminderFrequency(Integer reminderFrequency) {
        this.reminderFrequency = reminderFrequency;
    }

    public Integer getLastDate() {
        return lastDate;
    }

    public void setLastDate(Integer lastDate) {
        this.lastDate = lastDate;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
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

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getAdminUserNames() {
        return adminUserNames;
    }

    public void setAdminUserNames(String adminUserNames) {
        this.adminUserNames = adminUserNames;
    }

    public String getAssignUserNames() {
        return assignUserNames;
    }

    public void setAssignUserNames(String assignUserNames) {
        this.assignUserNames = assignUserNames;
    }

    public Object getRequestUserName() {
        return requestUserName;
    }

    public void setRequestUserName(Object requestUserName) {
        this.requestUserName = requestUserName;
    }

    public Object getTaskCloseUserName() {
        return taskCloseUserName;
    }

    public void setTaskCloseUserName(Object taskCloseUserName) {
        this.taskCloseUserName = taskCloseUserName;
    }

    public Integer getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Integer privilege) {
        this.privilege = privilege;
    }

    @Override
    public String toString() {
        return "ChatTask{" +
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
                ", createdByName='" + createdByName + '\'' +
                ", adminUserNames='" + adminUserNames + '\'' +
                ", assignUserNames='" + assignUserNames + '\'' +
                ", requestUserName=" + requestUserName +
                ", taskCloseUserName=" + taskCloseUserName +
                ", privilege=" + privilege +
                '}';
    }
}
