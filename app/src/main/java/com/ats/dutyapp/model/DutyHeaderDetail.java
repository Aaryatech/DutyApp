package com.ats.dutyapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DutyHeaderDetail {

    @SerializedName("dutyId")
    @Expose
    private Integer dutyId;
    @SerializedName("dutyName")
    @Expose
    private String dutyName;
    @SerializedName("deptId")
    @Expose
    private Integer deptId;
    @SerializedName("desgId")
    @Expose
    private Integer desgId;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("typeDesc")
    @Expose
    private String typeDesc;
    @SerializedName("shiftId")
    @Expose
    private Integer shiftId;
    @SerializedName("createdBy")
    @Expose
    private Integer createdBy;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("totalTaskWt")
    @Expose
    private Integer totalTaskWt;
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
    @SerializedName("deptName")
    @Expose
    private String deptName;
    @SerializedName("desgName")
    @Expose
    private String desgName;
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
    @SerializedName("createdByName")
    @Expose
    private String createdByName;
    @SerializedName("taskDetailDisplay")
    @Expose
    private List<TaskDetailDisplay> taskDetailDisplay = null;

    public Integer getDutyId() {
        return dutyId;
    }

    public void setDutyId(Integer dutyId) {
        this.dutyId = dutyId;
    }

    public String getDutyName() {
        return dutyName;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public Integer getDesgId() {
        return desgId;
    }

    public void setDesgId(Integer desgId) {
        this.desgId = desgId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public Integer getShiftId() {
        return shiftId;
    }

    public void setShiftId(Integer shiftId) {
        this.shiftId = shiftId;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getTotalTaskWt() {
        return totalTaskWt;
    }

    public void setTotalTaskWt(Integer totalTaskWt) {
        this.totalTaskWt = totalTaskWt;
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

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDesgName() {
        return desgName;
    }

    public void setDesgName(String desgName) {
        this.desgName = desgName;
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

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public List<TaskDetailDisplay> getTaskDetailDisplay() {
        return taskDetailDisplay;
    }

    public void setTaskDetailDisplay(List<TaskDetailDisplay> taskDetailDisplay) {
        this.taskDetailDisplay = taskDetailDisplay;
    }

    @Override
    public String toString() {
        return "DutyHeaderDetail{" +
                "dutyId=" + dutyId +
                ", dutyName='" + dutyName + '\'' +
                ", deptId=" + deptId +
                ", desgId=" + desgId +
                ", type=" + type +
                ", typeDesc='" + typeDesc + '\'' +
                ", shiftId=" + shiftId +
                ", createdBy=" + createdBy +
                ", createdDate='" + createdDate + '\'' +
                ", totalTaskWt=" + totalTaskWt +
                ", delStatus=" + delStatus +
                ", exInt1=" + exInt1 +
                ", exInt2=" + exInt2 +
                ", exInt3=" + exInt3 +
                ", exVar1='" + exVar1 + '\'' +
                ", exVar2='" + exVar2 + '\'' +
                ", exVar3='" + exVar3 + '\'' +
                ", deptName='" + deptName + '\'' +
                ", desgName='" + desgName + '\'' +
                ", shiftName='" + shiftName + '\'' +
                ", shiftFromTime='" + shiftFromTime + '\'' +
                ", shiftToTime='" + shiftToTime + '\'' +
                ", noOfHr='" + noOfHr + '\'' +
                ", createdByName='" + createdByName + '\'' +
                ", taskDetailDisplay=" + taskDetailDisplay +
                '}';
    }
}
