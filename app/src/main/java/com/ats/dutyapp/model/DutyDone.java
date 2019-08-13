package com.ats.dutyapp.model;

public class DutyDone {
    private int taskDoneHeaderId;
    private String taskDate;
    private int dutyId;
    private int empId;
    private int empIdOld;
    private String taskShiftRemark;
    private int taskShiftUserId;
    private int taskAssignUserId;
    private int delStatus;
    private int status;
    private int dutyWeight;
    private int taskCompleteWt;
    private String completionPercent;
    private Integer exInt1;
    private Integer exInt2;
    private Integer exInt3;
    private String exVar1;
    private String exVar2;
    private String exVar3;


    public DutyDone(int taskDoneHeaderId, String taskDate, int dutyId, int empId, int empIdOld, String taskShiftRemark, int taskShiftUserId, int taskAssignUserId, int delStatus, int status, int dutyWeight, int taskCompleteWt, String completionPercent, Integer exInt1, Integer exInt2, Integer exInt3, String exVar1, String exVar2, String exVar3) {
        this.taskDoneHeaderId = taskDoneHeaderId;
        this.taskDate = taskDate;
        this.dutyId = dutyId;
        this.empId = empId;
        this.empIdOld = empIdOld;
        this.taskShiftRemark = taskShiftRemark;
        this.taskShiftUserId = taskShiftUserId;
        this.taskAssignUserId = taskAssignUserId;
        this.delStatus = delStatus;
        this.status = status;
        this.dutyWeight = dutyWeight;
        this.taskCompleteWt = taskCompleteWt;
        this.completionPercent = completionPercent;
        this.exInt1 = exInt1;
        this.exInt2 = exInt2;
        this.exInt3 = exInt3;
        this.exVar1 = exVar1;
        this.exVar2 = exVar2;
        this.exVar3 = exVar3;
    }

    public int getTaskDoneHeaderId() {
        return taskDoneHeaderId;
    }

    public void setTaskDoneHeaderId(int taskDoneHeaderId) {
        this.taskDoneHeaderId = taskDoneHeaderId;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public int getDutyId() {
        return dutyId;
    }

    public void setDutyId(int dutyId) {
        this.dutyId = dutyId;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public int getEmpIdOld() {
        return empIdOld;
    }

    public void setEmpIdOld(int empIdOld) {
        this.empIdOld = empIdOld;
    }

    public String getTaskShiftRemark() {
        return taskShiftRemark;
    }

    public void setTaskShiftRemark(String taskShiftRemark) {
        this.taskShiftRemark = taskShiftRemark;
    }

    public int getTaskShiftUserId() {
        return taskShiftUserId;
    }

    public void setTaskShiftUserId(int taskShiftUserId) {
        this.taskShiftUserId = taskShiftUserId;
    }

    public int getTaskAssignUserId() {
        return taskAssignUserId;
    }

    public void setTaskAssignUserId(int taskAssignUserId) {
        this.taskAssignUserId = taskAssignUserId;
    }

    public int getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(int delStatus) {
        this.delStatus = delStatus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDutyWeight() {
        return dutyWeight;
    }

    public void setDutyWeight(int dutyWeight) {
        this.dutyWeight = dutyWeight;
    }

    public int getTaskCompleteWt() {
        return taskCompleteWt;
    }

    public void setTaskCompleteWt(int taskCompleteWt) {
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

    @Override
    public String toString() {
        return "DutyDone{" +
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
                '}';
    }
}
