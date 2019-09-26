package com.ats.dutyapp.model;

public class SaveAssigneChecklist {

    private int assignId;
    private int deptId;
    private int checklistHeaderId;
    private String assignEmpIds;
    private int assignedBy;
    private String assignedDate;
    private int delStatus;
    private Integer exInt1;
    private Integer exInt2;
    private String exVar1;
    private String exVar2;

    public SaveAssigneChecklist(int assignId, int deptId, int checklistHeaderId, String assignEmpIds, int assignedBy, String assignedDate, int delStatus, Integer exInt1, Integer exInt2, String exVar1, String exVar2) {
        this.assignId = assignId;
        this.deptId = deptId;
        this.checklistHeaderId = checklistHeaderId;
        this.assignEmpIds = assignEmpIds;
        this.assignedBy = assignedBy;
        this.assignedDate = assignedDate;
        this.delStatus = delStatus;
        this.exInt1 = exInt1;
        this.exInt2 = exInt2;
        this.exVar1 = exVar1;
        this.exVar2 = exVar2;
    }

    public int getAssignId() {
        return assignId;
    }

    public void setAssignId(int assignId) {
        this.assignId = assignId;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public int getChecklistHeaderId() {
        return checklistHeaderId;
    }

    public void setChecklistHeaderId(int checklistHeaderId) {
        this.checklistHeaderId = checklistHeaderId;
    }

    public String getAssignEmpIds() {
        return assignEmpIds;
    }

    public void setAssignEmpIds(String assignEmpIds) {
        this.assignEmpIds = assignEmpIds;
    }

    public int getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(int assignedBy) {
        this.assignedBy = assignedBy;
    }

    public String getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(String assignedDate) {
        this.assignedDate = assignedDate;
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
        return "AssigneSave{" +
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
                '}';
    }
}
