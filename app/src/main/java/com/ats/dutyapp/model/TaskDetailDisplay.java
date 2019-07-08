package com.ats.dutyapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaskDetailDisplay {

    @SerializedName("taskId")
    @Expose
    private Integer taskId;
    @SerializedName("dutyId")
    @Expose
    private Integer dutyId;
    @SerializedName("taskNameEng")
    @Expose
    private String taskNameEng;
    @SerializedName("taskNameMar")
    @Expose
    private String taskNameMar;
    @SerializedName("taskNameHin")
    @Expose
    private String taskNameHin;
    @SerializedName("taskDescEng")
    @Expose
    private String taskDescEng;
    @SerializedName("taskDescMar")
    @Expose
    private String taskDescMar;
    @SerializedName("taskDescHin")
    @Expose
    private String taskDescHin;
    @SerializedName("photoReq")
    @Expose
    private Integer photoReq;
    @SerializedName("remarkReq")
    @Expose
    private Integer remarkReq;
    @SerializedName("taskWeight")
    @Expose
    private Integer taskWeight;
    @SerializedName("createdBy")
    @Expose
    private Integer createdBy;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
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
    @SerializedName("createdByName")
    @Expose
    private String createdByName;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getDutyId() {
        return dutyId;
    }

    public void setDutyId(Integer dutyId) {
        this.dutyId = dutyId;
    }

    public String getTaskNameEng() {
        return taskNameEng;
    }

    public void setTaskNameEng(String taskNameEng) {
        this.taskNameEng = taskNameEng;
    }

    public String getTaskNameMar() {
        return taskNameMar;
    }

    public void setTaskNameMar(String taskNameMar) {
        this.taskNameMar = taskNameMar;
    }

    public String getTaskNameHin() {
        return taskNameHin;
    }

    public void setTaskNameHin(String taskNameHin) {
        this.taskNameHin = taskNameHin;
    }

    public String getTaskDescEng() {
        return taskDescEng;
    }

    public void setTaskDescEng(String taskDescEng) {
        this.taskDescEng = taskDescEng;
    }

    public String getTaskDescMar() {
        return taskDescMar;
    }

    public void setTaskDescMar(String taskDescMar) {
        this.taskDescMar = taskDescMar;
    }

    public String getTaskDescHin() {
        return taskDescHin;
    }

    public void setTaskDescHin(String taskDescHin) {
        this.taskDescHin = taskDescHin;
    }

    public Integer getPhotoReq() {
        return photoReq;
    }

    public void setPhotoReq(Integer photoReq) {
        this.photoReq = photoReq;
    }

    public Integer getRemarkReq() {
        return remarkReq;
    }

    public void setRemarkReq(Integer remarkReq) {
        this.remarkReq = remarkReq;
    }

    public Integer getTaskWeight() {
        return taskWeight;
    }

    public void setTaskWeight(Integer taskWeight) {
        this.taskWeight = taskWeight;
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

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    @Override
    public String toString() {
        return "TaskDetailDisplay{" +
                "taskId=" + taskId +
                ", dutyId=" + dutyId +
                ", taskNameEng='" + taskNameEng + '\'' +
                ", taskNameMar='" + taskNameMar + '\'' +
                ", taskNameHin='" + taskNameHin + '\'' +
                ", taskDescEng='" + taskDescEng + '\'' +
                ", taskDescMar='" + taskDescMar + '\'' +
                ", taskDescHin='" + taskDescHin + '\'' +
                ", photoReq=" + photoReq +
                ", remarkReq=" + remarkReq +
                ", taskWeight=" + taskWeight +
                ", createdBy=" + createdBy +
                ", createdDate='" + createdDate + '\'' +
                ", delStatus=" + delStatus +
                ", exInt1=" + exInt1 +
                ", exInt2=" + exInt2 +
                ", exInt3=" + exInt3 +
                ", exVar1='" + exVar1 + '\'' +
                ", exVar2='" + exVar2 + '\'' +
                ", exVar3='" + exVar3 + '\'' +
                ", dutyName='" + dutyName + '\'' +
                ", createdByName='" + createdByName + '\'' +
                '}';
    }
}
