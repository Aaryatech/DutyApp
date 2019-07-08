package com.ats.dutyapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DutyHeader {

    @SerializedName("taskDoneHeaderId")
    @Expose
    private Integer taskDoneHeaderId;
    @SerializedName("taskDate")
    @Expose
    private String taskDate;
    @SerializedName("dutyId")
    @Expose
    private Integer dutyId;
    @SerializedName("empId")
    @Expose
    private Integer empId;
    @SerializedName("empIdOld")
    @Expose
    private Integer empIdOld;
    @SerializedName("taskShiftRemark")
    @Expose
    private String taskShiftRemark;
    @SerializedName("taskShiftUserId")
    @Expose
    private Integer taskShiftUserId;
    @SerializedName("taskAssignUserId")
    @Expose
    private Integer taskAssignUserId;
    @SerializedName("delStatus")
    @Expose
    private Integer delStatus;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("dutyWeight")
    @Expose
    private Integer dutyWeight;
    @SerializedName("taskCompleteWt")
    @Expose
    private Integer taskCompleteWt;
    @SerializedName("completionPercent")
    @Expose
    private String completionPercent;
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
    @SerializedName("taskDoneDetailDisplayList")
    @Expose
    private Object taskDoneDetailDisplayList;

    public Integer getTaskDoneHeaderId() {
        return taskDoneHeaderId;
    }

    public void setTaskDoneHeaderId(Integer taskDoneHeaderId) {
        this.taskDoneHeaderId = taskDoneHeaderId;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public Integer getDutyId() {
        return dutyId;
    }

    public void setDutyId(Integer dutyId) {
        this.dutyId = dutyId;
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public Integer getEmpIdOld() {
        return empIdOld;
    }

    public void setEmpIdOld(Integer empIdOld) {
        this.empIdOld = empIdOld;
    }

    public String getTaskShiftRemark() {
        return taskShiftRemark;
    }

    public void setTaskShiftRemark(String taskShiftRemark) {
        this.taskShiftRemark = taskShiftRemark;
    }

    public Integer getTaskShiftUserId() {
        return taskShiftUserId;
    }

    public void setTaskShiftUserId(Integer taskShiftUserId) {
        this.taskShiftUserId = taskShiftUserId;
    }

    public Integer getTaskAssignUserId() {
        return taskAssignUserId;
    }

    public void setTaskAssignUserId(Integer taskAssignUserId) {
        this.taskAssignUserId = taskAssignUserId;
    }

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDutyWeight() {
        return dutyWeight;
    }

    public void setDutyWeight(Integer dutyWeight) {
        this.dutyWeight = dutyWeight;
    }

    public Integer getTaskCompleteWt() {
        return taskCompleteWt;
    }

    public void setTaskCompleteWt(Integer taskCompleteWt) {
        this.taskCompleteWt = taskCompleteWt;
    }

    public String getCompletionPercent() {
        return completionPercent;
    }

    public void setCompletionPercent(String completionPercent) {
        this.completionPercent = completionPercent;
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

    public Object getTaskDoneDetailDisplayList() {
        return taskDoneDetailDisplayList;
    }

    public void setTaskDoneDetailDisplayList(Object taskDoneDetailDisplayList) {
        this.taskDoneDetailDisplayList = taskDoneDetailDisplayList;
    }

    @Override
    public String toString() {
        return "DutyHeader{" +
                "taskDoneHeaderId=" + taskDoneHeaderId +
                ", taskDate='" + taskDate + '\'' +
                ", dutyId=" + dutyId +
                ", empId=" + empId +
                ", empIdOld=" + empIdOld +
                ", taskShiftRemark='" + taskShiftRemark + '\'' +
                ", taskShiftUserId=" + taskShiftUserId +
                ", taskAssignUserId=" + taskAssignUserId +
                ", delStatus=" + delStatus +
                ", status=" + status +
                ", dutyWeight=" + dutyWeight +
                ", taskCompleteWt=" + taskCompleteWt +
                ", completionPercent='" + completionPercent + '\'' +
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
                ", taskDoneDetailDisplayList=" + taskDoneDetailDisplayList +
                '}';
    }
}
