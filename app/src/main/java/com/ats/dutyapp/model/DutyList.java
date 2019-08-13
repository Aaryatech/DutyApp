package com.ats.dutyapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DutyList {

    @SerializedName("dutyId")
    @Expose
    private Integer dutyId;
    @SerializedName("dutyName")
    @Expose
    private String dutyName;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("typeDesc")
    @Expose
    private String typeDesc;
    @SerializedName("totalTaskWt")
    @Expose
    private Integer totalTaskWt;
    @SerializedName("shiftId")
    @Expose
    private Integer shiftId;
    @SerializedName("shiftName")
    @Expose
    private String shiftName;
    @SerializedName("shiftFromTime")
    @Expose
    private String shiftFromTime;
    @SerializedName("shiftToTime")
    @Expose
    private String shiftToTime;
    @SerializedName("taskList")
    @Expose
    private List<TaskList> taskList = null;
    int visibleStatus;

    public int getVisibleStatus() {
        return visibleStatus;
    }

    public void setVisibleStatus(int visibleStatus) {
        this.visibleStatus = visibleStatus;
    }

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

    public Integer getTotalTaskWt() {
        return totalTaskWt;
    }

    public void setTotalTaskWt(Integer totalTaskWt) {
        this.totalTaskWt = totalTaskWt;
    }

    public Integer getShiftId() {
        return shiftId;
    }

    public void setShiftId(Integer shiftId) {
        this.shiftId = shiftId;
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

    public List<TaskList> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<TaskList> taskList) {
        this.taskList = taskList;
    }

    @Override
    public String toString() {
        return "DutyList{" +
                "dutyId=" + dutyId +
                ", dutyName='" + dutyName + '\'' +
                ", type=" + type +
                ", typeDesc='" + typeDesc + '\'' +
                ", totalTaskWt=" + totalTaskWt +
                ", shiftId=" + shiftId +
                ", shiftName='" + shiftName + '\'' +
                ", shiftFromTime='" + shiftFromTime + '\'' +
                ", shiftToTime='" + shiftToTime + '\'' +
                ", taskList=" + taskList +
                ", visibleStatus=" + visibleStatus +
                '}';
    }
}
