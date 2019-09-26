package com.ats.dutyapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AssignChecklist {

    @SerializedName("assignId")
    @Expose
    private Integer assignId;
    @SerializedName("deptId")
    @Expose
    private Integer deptId;
    @SerializedName("checklistHeaderId")
    @Expose
    private Integer checklistHeaderId;
    @SerializedName("assignEmpIds")
    @Expose
    private String assignEmpIds;
    @SerializedName("assignedBy")
    @Expose
    private Integer assignedBy;
    @SerializedName("assignedDate")
    @Expose
    private String assignedDate;
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
    @SerializedName("checklistName")
    @Expose
    private String checklistName;
    @SerializedName("assignEmpName")
    @Expose
    private String assignEmpName;
    @SerializedName("assignByName")
    @Expose
    private String assignByName;

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

    public String getAssignEmpIds() {
        return assignEmpIds;
    }

    public void setAssignEmpIds(String assignEmpIds) {
        this.assignEmpIds = assignEmpIds;
    }

    public Integer getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(Integer assignedBy) {
        this.assignedBy = assignedBy;
    }

    public String getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(String assignedDate) {
        this.assignedDate = assignedDate;
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

    public String getChecklistName() {
        return checklistName;
    }

    public void setChecklistName(String checklistName) {
        this.checklistName = checklistName;
    }

    public String getAssignEmpName() {
        return assignEmpName;
    }

    public void setAssignEmpName(String assignEmpName) {
        this.assignEmpName = assignEmpName;
    }

    public String getAssignByName() {
        return assignByName;
    }

    public void setAssignByName(String assignByName) {
        this.assignByName = assignByName;
    }

    @Override
    public String toString() {
        return "AssignCheckTemp{" +
                "assignId=" + assignId +
                ", deptId=" + deptId +
                ", checklistHeaderId=" + checklistHeaderId +
                ", assignEmpIds='" + assignEmpIds + '\'' +
                ", assignedBy=" + assignedBy +
                ", assignedDate='" + assignedDate + '\'' +
                ", delStatus=" + delStatus +
                ", exInt1=" + exInt1 +
                ", exInt2=" + exInt2 +
                ", exVar1='" + exVar1 + '\'' +
                ", exVar2='" + exVar2 + '\'' +
                ", deptName='" + deptName + '\'' +
                ", checklistName='" + checklistName + '\'' +
                ", assignEmpName='" + assignEmpName + '\'' +
                ", assignByName='" + assignByName + '\'' +
                '}';
    }
}
