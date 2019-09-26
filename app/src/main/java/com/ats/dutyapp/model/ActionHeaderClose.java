package com.ats.dutyapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActionHeaderClose {
    @SerializedName("actionHeaderId")
    @Expose
    private Integer actionHeaderId;
    @SerializedName("assignId")
    @Expose
    private Integer assignId;
    @SerializedName("deptId")
    @Expose
    private Integer deptId;
    @SerializedName("checklistHeaderId")
    @Expose
    private Integer checklistHeaderId;
    @SerializedName("checklistName")
    @Expose
    private String checklistName;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("actionBy")
    @Expose
    private Integer actionBy;
    @SerializedName("actionDate")
    @Expose
    private String actionDate;
    @SerializedName("actionDatetime")
    @Expose
    private String actionDatetime;
    @SerializedName("closedBy")
    @Expose
    private Integer closedBy;
    @SerializedName("closedDate")
    @Expose
    private String closedDate;
    @SerializedName("closedDatetime")
    @Expose
    private String closedDatetime;
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
    @SerializedName("deptName")
    @Expose
    private String deptName;
    @SerializedName("actionByName")
    @Expose
    private String actionByName;
    @SerializedName("closedByName")
    @Expose
    private String closedByName;
    @SerializedName("actionDetailList")
    @Expose
    private List<ActionDetailList> actionDetailList = null;

    public Integer getActionHeaderId() {
        return actionHeaderId;
    }

    public void setActionHeaderId(Integer actionHeaderId) {
        this.actionHeaderId = actionHeaderId;
    }

    public Integer getAssignId() {
        return assignId;
    }

    public void setAssignId(Integer assignId) {
        this.assignId = assignId;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public Integer getChecklistHeaderId() {
        return checklistHeaderId;
    }

    public void setChecklistHeaderId(Integer checklistHeaderId) {
        this.checklistHeaderId = checklistHeaderId;
    }

    public String getChecklistName() {
        return checklistName;
    }

    public void setChecklistName(String checklistName) {
        this.checklistName = checklistName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getActionBy() {
        return actionBy;
    }

    public void setActionBy(Integer actionBy) {
        this.actionBy = actionBy;
    }

    public String getActionDate() {
        return actionDate;
    }

    public void setActionDate(String actionDate) {
        this.actionDate = actionDate;
    }

    public String getActionDatetime() {
        return actionDatetime;
    }

    public void setActionDatetime(String actionDatetime) {
        this.actionDatetime = actionDatetime;
    }

    public Integer getClosedBy() {
        return closedBy;
    }

    public void setClosedBy(Integer closedBy) {
        this.closedBy = closedBy;
    }

    public String getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(String closedDate) {
        this.closedDate = closedDate;
    }

    public String getClosedDatetime() {
        return closedDatetime;
    }

    public void setClosedDatetime(String closedDatetime) {
        this.closedDatetime = closedDatetime;
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

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getActionByName() {
        return actionByName;
    }

    public void setActionByName(String actionByName) {
        this.actionByName = actionByName;
    }

    public String getClosedByName() {
        return closedByName;
    }

    public void setClosedByName(String closedByName) {
        this.closedByName = closedByName;
    }

    public List<ActionDetailList> getActionDetailList() {
        return actionDetailList;
    }

    public void setActionDetailList(List<ActionDetailList> actionDetailList) {
        this.actionDetailList = actionDetailList;
    }

    @Override
    public String toString() {
        return "ActionHeaderClose{" +
                "actionHeaderId=" + actionHeaderId +
                ", assignId=" + assignId +
                ", deptId=" + deptId +
                ", checklistHeaderId=" + checklistHeaderId +
                ", checklistName='" + checklistName + '\'' +
                ", status=" + status +
                ", actionBy=" + actionBy +
                ", actionDate='" + actionDate + '\'' +
                ", actionDatetime='" + actionDatetime + '\'' +
                ", closedBy=" + closedBy +
                ", closedDate='" + closedDate + '\'' +
                ", closedDatetime='" + closedDatetime + '\'' +
                ", delStatus=" + delStatus +
                ", exInt1=" + exInt1 +
                ", exInt2=" + exInt2 +
                ", exVar1='" + exVar1 + '\'' +
                ", exVar2='" + exVar2 + '\'' +
                ", deptName='" + deptName + '\'' +
                ", actionByName='" + actionByName + '\'' +
                ", closedByName='" + closedByName + '\'' +
                ", actionDetailList=" + actionDetailList +
                '}';
    }
}
