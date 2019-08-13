package com.ats.dutyapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaskList {

    @SerializedName("taskId")
    @Expose
    private Integer taskId;
    @SerializedName("taskNameEng")
    @Expose
    private String taskNameEng;
    @SerializedName("taskDescEng")
    @Expose
    private String taskDescEng;
    @SerializedName("taskWeight")
    @Expose
    private Integer taskWeight;
    @SerializedName("photoReq")
    @Expose
    private Integer photoReq;
    @SerializedName("remarkReq")
    @Expose
    private Integer remarkReq;
    @SerializedName("timeReq")
    @Expose
    private Integer timeReq;
    @SerializedName("taskTime")
    @Expose
    private String taskTime;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getTaskNameEng() {
        return taskNameEng;
    }

    public void setTaskNameEng(String taskNameEng) {
        this.taskNameEng = taskNameEng;
    }

    public String getTaskDescEng() {
        return taskDescEng;
    }

    public void setTaskDescEng(String taskDescEng) {
        this.taskDescEng = taskDescEng;
    }

    public Integer getTaskWeight() {
        return taskWeight;
    }

    public void setTaskWeight(Integer taskWeight) {
        this.taskWeight = taskWeight;
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

    public Integer getTimeReq() {
        return timeReq;
    }

    public void setTimeReq(Integer timeReq) {
        this.timeReq = timeReq;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }

    @Override
    public String toString() {
        return "TaskList{" +
                "taskId=" + taskId +
                ", taskNameEng='" + taskNameEng + '\'' +
                ", taskDescEng='" + taskDescEng + '\'' +
                ", taskWeight=" + taskWeight +
                ", photoReq=" + photoReq +
                ", remarkReq=" + remarkReq +
                ", timeReq=" + timeReq +
                ", taskTime='" + taskTime + '\'' +
                '}';
    }
}
