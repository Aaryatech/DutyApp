package com.ats.dutyapp.model;

import java.util.List;

public class ChecklistActionHeader {

    private int actionHeaderId;
    private int assignId;
    private int deptId;
    private int checklistHeaderId;
    private String checklistName;
    private int status;
    private int actionBy;
    private String actionDate;
    private String actionDatetime;
    private int closedBy;
    private String closedDate;
    private String closedDatetime;
    private int delStatus;
    private Integer exInt1;
    private Integer exInt2;
    private String exVar1;
    private String exVar2;
    private List<ChecklistActionDetail> checklistActionDetail;

    public ChecklistActionHeader(int actionHeaderId, int assignId, int deptId, int checklistHeaderId, String checklistName, int status, int actionBy, String actionDate, String actionDatetime, int closedBy, String closedDate, String closedDatetime, int delStatus, Integer exInt1, Integer exInt2, String exVar1, String exVar2, List<ChecklistActionDetail> checklistActionDetail) {
        this.actionHeaderId = actionHeaderId;
        this.assignId = assignId;
        this.deptId = deptId;
        this.checklistHeaderId = checklistHeaderId;
        this.checklistName = checklistName;
        this.status = status;
        this.actionBy = actionBy;
        this.actionDate = actionDate;
        this.actionDatetime = actionDatetime;
        this.closedBy = closedBy;
        this.closedDate = closedDate;
        this.closedDatetime = closedDatetime;
        this.delStatus = delStatus;
        this.exInt1 = exInt1;
        this.exInt2 = exInt2;
        this.exVar1 = exVar1;
        this.exVar2 = exVar2;
        this.checklistActionDetail = checklistActionDetail;
    }

    public int getActionHeaderId() {
        return actionHeaderId;
    }

    public void setActionHeaderId(int actionHeaderId) {
        this.actionHeaderId = actionHeaderId;
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

    public String getChecklistName() {
        return checklistName;
    }

    public void setChecklistName(String checklistName) {
        this.checklistName = checklistName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getActionBy() {
        return actionBy;
    }

    public void setActionBy(int actionBy) {
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

    public int getClosedBy() {
        return closedBy;
    }

    public void setClosedBy(int closedBy) {
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

    public List<ChecklistActionDetail> getChecklistActionDetail() {
        return checklistActionDetail;
    }

    public void setChecklistActionDetail(List<ChecklistActionDetail> checklistActionDetail) {
        this.checklistActionDetail = checklistActionDetail;
    }

    @Override
    public String toString() {
        return "ChecklistActionHeader{" +
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
                ", checklistActionDetail=" + checklistActionDetail +
                '}';
    }
}
