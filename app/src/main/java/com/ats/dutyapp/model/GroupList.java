package com.ats.dutyapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupList {
    @SerializedName("groupId")
    @Expose
    private Integer groupId;
    @SerializedName("groupName")
    @Expose
    private String groupName;
    @SerializedName("groupDesc")
    @Expose
    private String groupDesc;
    @SerializedName("userIds")
    @Expose
    private String userIds;
    @SerializedName("groupCreatedUserId")
    @Expose
    private Integer groupCreatedUserId;
    @SerializedName("groupCreatedDate")
    @Expose
    private String groupCreatedDate;
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
    @SerializedName("exVar1")
    @Expose
    private String exVar1;
    @SerializedName("exVar2")
    @Expose
    private String exVar2;
    @SerializedName("createdByName")
    @Expose
    private String createdByName;
    @SerializedName("userNames")
    @Expose
    private String userNames;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public Integer getGroupCreatedUserId() {
        return groupCreatedUserId;
    }

    public void setGroupCreatedUserId(Integer groupCreatedUserId) {
        this.groupCreatedUserId = groupCreatedUserId;
    }

    public String getGroupCreatedDate() {
        return groupCreatedDate;
    }

    public void setGroupCreatedDate(String groupCreatedDate) {
        this.groupCreatedDate = groupCreatedDate;
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

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getUserNames() {
        return userNames;
    }

    public void setUserNames(String userNames) {
        this.userNames = userNames;
    }

    @Override
    public String toString() {
        return "GroupList{" +
                "groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", groupDesc='" + groupDesc + '\'' +
                ", userIds='" + userIds + '\'' +
                ", groupCreatedUserId=" + groupCreatedUserId +
                ", groupCreatedDate='" + groupCreatedDate + '\'' +
                ", isActive=" + isActive +
                ", delStatus=" + delStatus +
                ", exInt1=" + exInt1 +
                ", exInt2=" + exInt2 +
                ", exVar1='" + exVar1 + '\'' +
                ", exVar2='" + exVar2 + '\'' +
                ", createdByName='" + createdByName + '\'' +
                ", userNames='" + userNames + '\'' +
                '}';
    }
}
