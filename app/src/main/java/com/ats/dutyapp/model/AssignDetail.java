package com.ats.dutyapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AssignDetail {

    @SerializedName("assignId")
    @Expose
    private Integer assignId;
    @SerializedName("assignDate")
    @Expose
    private String assignDate;
    @SerializedName("dutyId")
    @Expose
    private Integer dutyId;
    @SerializedName("empIds")
    @Expose
    private String empIds;
    @SerializedName("notifyTime")
    @Expose
    private String notifyTime;
    @SerializedName("taskAssignUserId")
    @Expose
    private Integer taskAssignUserId;
    @SerializedName("lastEditDate")
    @Expose
    private String lastEditDate;
    @SerializedName("lastEditUserId")
    @Expose
    private Integer lastEditUserId;
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
    @SerializedName("dutyName")
    @Expose
    private String dutyName;
    @SerializedName("shiftName")
    @Expose
    private String shiftName;
    @SerializedName("shiftFromTime")
    @Expose
    private String shiftFromTime;
    @SerializedName("shiftToTime")
    @Expose
    private String shiftToTime;
    @SerializedName("noOfHr")
    @Expose
    private String noOfHr;
    @SerializedName("taskAssignUserName")
    @Expose
    private String taskAssignUserName;
    @SerializedName("lastEditUserName")
    @Expose
    private String lastEditUserName;
    @SerializedName("empList")
    @Expose
    private List<EmpList> empList = null;

    public Integer getAssignId() {
        return assignId;
    }

    public void setAssignId(Integer assignId) {
        this.assignId = assignId;
    }

    public String getAssignDate() {
        return assignDate;
    }

    public void setAssignDate(String assignDate) {
        this.assignDate = assignDate;
    }

    public Integer getDutyId() {
        return dutyId;
    }

    public void setDutyId(Integer dutyId) {
        this.dutyId = dutyId;
    }

    public String getEmpIds() {
        return empIds;
    }

    public void setEmpIds(String empIds) {
        this.empIds = empIds;
    }

    public String getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(String notifyTime) {
        this.notifyTime = notifyTime;
    }

    public Integer getTaskAssignUserId() {
        return taskAssignUserId;
    }

    public void setTaskAssignUserId(Integer taskAssignUserId) {
        this.taskAssignUserId = taskAssignUserId;
    }

    public String getLastEditDate() {
        return lastEditDate;
    }

    public void setLastEditDate(String lastEditDate) {
        this.lastEditDate = lastEditDate;
    }

    public Integer getLastEditUserId() {
        return lastEditUserId;
    }

    public void setLastEditUserId(Integer lastEditUserId) {
        this.lastEditUserId = lastEditUserId;
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

    public String getDutyName() {
        return dutyName;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public String getShiftFromTime() {
        return shiftFromTime;
    }

    public void setShiftFromTime(String shiftFromTime) {
        this.shiftFromTime = shiftFromTime;
    }

    public String getShiftToTime() {
        return shiftToTime;
    }

    public void setShiftToTime(String shiftToTime) {
        this.shiftToTime = shiftToTime;
    }

    public String getNoOfHr() {
        return noOfHr;
    }

    public void setNoOfHr(String noOfHr) {
        this.noOfHr = noOfHr;
    }

    public String getTaskAssignUserName() {
        return taskAssignUserName;
    }

    public void setTaskAssignUserName(String taskAssignUserName) {
        this.taskAssignUserName = taskAssignUserName;
    }

    public String getLastEditUserName() {
        return lastEditUserName;
    }

    public void setLastEditUserName(String lastEditUserName) {
        this.lastEditUserName = lastEditUserName;
    }

    public List<EmpList> getEmpList() {
        return empList;
    }

    public void setEmpList(List<EmpList> empList) {
        this.empList = empList;
    }

    @Override
    public String toString() {
        return "AssignDetail{" +
                "assignId=" + assignId +
                ", assignDate='" + assignDate + '\'' +
                ", dutyId=" + dutyId +
                ", empIds='" + empIds + '\'' +
                ", notifyTime='" + notifyTime + '\'' +
                ", taskAssignUserId=" + taskAssignUserId +
                ", lastEditDate='" + lastEditDate + '\'' +
                ", lastEditUserId=" + lastEditUserId +
                ", delStatus=" + delStatus +
                ", exInt1=" + exInt1 +
                ", exInt2=" + exInt2 +
                ", exInt3=" + exInt3 +
                ", exVar1='" + exVar1 + '\'' +
                ", exVar2='" + exVar2 + '\'' +
                ", exVar3='" + exVar3 + '\'' +
                ", dutyName='" + dutyName + '\'' +
                ", shiftName='" + shiftName + '\'' +
                ", shiftFromTime='" + shiftFromTime + '\'' +
                ", shiftToTime='" + shiftToTime + '\'' +
                ", noOfHr='" + noOfHr + '\'' +
                ", taskAssignUserName='" + taskAssignUserName + '\'' +
                ", lastEditUserName='" + lastEditUserName + '\'' +
                ", empList=" + empList +
                '}';
    }
}
