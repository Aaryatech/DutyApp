package com.ats.dutyapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Document {
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("dutyType")
    @Expose
    private String dutyType;
    @SerializedName("dutyList")
    @Expose
    private List<DutyList> dutyList = null;
    int visibleStatus;

    public int getVisibleStatus() {
        return visibleStatus;
    }

    public void setVisibleStatus(int visibleStatus) {
        this.visibleStatus = visibleStatus;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDutyType() {
        return dutyType;
    }

    public void setDutyType(String dutyType) {
        this.dutyType = dutyType;
    }

    public List<DutyList> getDutyList() {
        return dutyList;
    }

    public void setDutyList(List<DutyList> dutyList) {
        this.dutyList = dutyList;
    }

    @Override
    public String toString() {
        return "Document{" +
                "type=" + type +
                ", dutyType='" + dutyType + '\'' +
                ", dutyList=" + dutyList +
                ", visibleStatus=" + visibleStatus +
                '}';
    }
}
