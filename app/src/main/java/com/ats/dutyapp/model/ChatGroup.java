package com.ats.dutyapp.model;

public class ChatGroup {

    private int groupId;
    private String groupName;
    private String groupDesc;
    private String userIds;
    private int groupCreatedUserId;
    private String groupCreatedDate;
    private int isActive;
    private int delStatus;
    private Integer exInt1;
    private Integer exInt2;
    private String exVar1;
    private String exVar2;

    public ChatGroup(int groupId, String groupName, String groupDesc, String userIds, int groupCreatedUserId, String groupCreatedDate, int isActive, int delStatus, Integer exInt1, Integer exInt2, String exVar1, String exVar2) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupDesc = groupDesc;
        this.userIds = userIds;
        this.groupCreatedUserId = groupCreatedUserId;
        this.groupCreatedDate = groupCreatedDate;
        this.isActive = isActive;
        this.delStatus = delStatus;
        this.exInt1 = exInt1;
        this.exInt2 = exInt2;
        this.exVar1 = exVar1;
        this.exVar2 = exVar2;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
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

    public int getGroupCreatedUserId() {
        return groupCreatedUserId;
    }

    public void setGroupCreatedUserId(int groupCreatedUserId) {
        this.groupCreatedUserId = groupCreatedUserId;
    }

    public String getGroupCreatedDate() {
        return groupCreatedDate;
    }

    public void setGroupCreatedDate(String groupCreatedDate) {
        this.groupCreatedDate = groupCreatedDate;
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

    @Override
    public String toString() {
        return "ChatGroup{" +
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
                '}';
    }
}
