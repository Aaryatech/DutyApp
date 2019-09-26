package com.ats.dutyapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActionDetailList {
    @SerializedName("actionDetailId")
    @Expose
    private Integer actionDetailId;
    @SerializedName("actionHeaderId")
    @Expose
    private Integer actionHeaderId;
    @SerializedName("checklistMasterHeaderId")
    @Expose
    private Integer checklistMasterHeaderId;
    @SerializedName("checklistMasterDetailId")
    @Expose
    private Integer checklistMasterDetailId;
    @SerializedName("checklist_desc")
    @Expose
    private String checklistDesc;
    @SerializedName("isPhoto")
    @Expose
    private Integer isPhoto;
    @SerializedName("checkStatus")
    @Expose
    private Integer checkStatus;
    @SerializedName("actionPhoto")
    @Expose
    private String actionPhoto;
    @SerializedName("closedPhoto")
    @Expose
    private String closedPhoto;
    @SerializedName("checkDate")
    @Expose
    private String checkDate;
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

    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public Integer getActionDetailId() {
        return actionDetailId;
    }

    public void setActionDetailId(Integer actionDetailId) {
        this.actionDetailId = actionDetailId;
    }

    public Integer getActionHeaderId() {
        return actionHeaderId;
    }

    public void setActionHeaderId(Integer actionHeaderId) {
        this.actionHeaderId = actionHeaderId;
    }

    public Integer getChecklistMasterHeaderId() {
        return checklistMasterHeaderId;
    }

    public void setChecklistMasterHeaderId(Integer checklistMasterHeaderId) {
        this.checklistMasterHeaderId = checklistMasterHeaderId;
    }

    public Integer getChecklistMasterDetailId() {
        return checklistMasterDetailId;
    }

    public void setChecklistMasterDetailId(Integer checklistMasterDetailId) {
        this.checklistMasterDetailId = checklistMasterDetailId;
    }

    public String getChecklistDesc() {
        return checklistDesc;
    }

    public void setChecklistDesc(String checklistDesc) {
        this.checklistDesc = checklistDesc;
    }

    public Integer getIsPhoto() {
        return isPhoto;
    }

    public void setIsPhoto(Integer isPhoto) {
        this.isPhoto = isPhoto;
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getActionPhoto() {
        return actionPhoto;
    }

    public void setActionPhoto(String actionPhoto) {
        this.actionPhoto = actionPhoto;
    }

    public String getClosedPhoto() {
        return closedPhoto;
    }

    public void setClosedPhoto(String closedPhoto) {
        this.closedPhoto = closedPhoto;
    }

    public String getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
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

    @Override
    public String toString() {
        return "ActionDetailList{" +
                "actionDetailId=" + actionDetailId +
                ", actionHeaderId=" + actionHeaderId +
                ", checklistMasterHeaderId=" + checklistMasterHeaderId +
                ", checklistMasterDetailId=" + checklistMasterDetailId +
                ", checklistDesc='" + checklistDesc + '\'' +
                ", isPhoto=" + isPhoto +
                ", checkStatus=" + checkStatus +
                ", actionPhoto='" + actionPhoto + '\'' +
                ", closedPhoto='" + closedPhoto + '\'' +
                ", checkDate='" + checkDate + '\'' +
                ", delStatus=" + delStatus +
                ", exInt1=" + exInt1 +
                ", exInt2=" + exInt2 +
                ", exVar1='" + exVar1 + '\'' +
                ", exVar2='" + exVar2 + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}
