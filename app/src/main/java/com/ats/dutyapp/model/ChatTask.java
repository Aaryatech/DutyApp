package com.ats.dutyapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatTask {
    private Integer headerId;
    private String createdDate;
    private String headerName;
    private Integer createdUserId;
    private String adminUserIds;
    private String assignUserIds;
    private String taskDesc;
    private String image;
    private Integer status;
    private Integer requestUserId;
    private Integer taskCloseUserId;
    private String taskCompleteRemark;
    private Integer isReminderRequired;
    private String reminderFrequency;
    private String lastDate;
    private Integer isActive;
    private Integer delStatus;
    private Integer exInt1;
    private Integer exInt2;
    private Integer exInt3;
    private String exVar1;
    private String exVar2;
    private String exVar3;
    private String createdByName;
    private String adminUserNames;
    private String assignUserNames;
    private String requestUserName;
    private String taskCloseUserName;
    private Integer privilege;
    private int unreadCount;

    public ChatTask() {
    }

    public ChatTask(Integer headerId, String createdDate, String headerName, Integer createdUserId, String adminUserIds, String assignUserIds, String taskDesc, String image, Integer status, Integer privilege) {
        this.headerId = headerId;
        this.createdDate = createdDate;
        this.headerName = headerName;
        this.createdUserId = createdUserId;
        this.adminUserIds = adminUserIds;
        this.assignUserIds = assignUserIds;
        this.taskDesc = taskDesc;
        this.image = image;
        this.status = status;
        this.privilege = privilege;
    }

    public ChatTask(Integer headerId, String createdDate, String headerName, Integer createdUserId, String adminUserIds, String assignUserIds, String taskDesc, String image, Integer status, Integer requestUserId, String requestUserName, Integer privilege) {
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
        this.requestUserName = requestUserName;
        this.privilege = privilege;
    }

    public ChatTask(Integer headerId, String createdDate, String headerName, Integer createdUserId, String adminUserIds, String assignUserIds, String taskDesc, String image, Integer status, Integer requestUserId, String requestUserName, Integer privilege,String lastDate) {
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
        this.requestUserName = requestUserName;
        this.privilege = privilege;
        this.lastDate = lastDate;
    }


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

    public String getRequestUserName() {
        return requestUserName;
    }

    public void setRequestUserName(String requestUserName) {
        this.requestUserName = requestUserName;
    }

    public String getTaskCloseUserName() {
        return taskCloseUserName;
    }

    public void setTaskCloseUserName(String taskCloseUserName) {
        this.taskCloseUserName = taskCloseUserName;
    }

    public Integer getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Integer privilege) {
        this.privilege = privilege;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
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
                ", reminderFrequency='" + reminderFrequency + '\'' +
                ", lastDate='" + lastDate + '\'' +
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
                ", requestUserName='" + requestUserName + '\'' +
                ", taskCloseUserName='" + taskCloseUserName + '\'' +
                ", privilege=" + privilege +
                ", unreadCount=" + unreadCount +
                '}';
    }
}
