package com.ats.dutyapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailList {

    @SerializedName("checklistDetailId")
    @Expose
    private Integer checklistDetailId;
    @SerializedName("checklistHeaderId")
    @Expose
    private Integer checklistHeaderId;
    @SerializedName("checklist_desc")
    @Expose
    private String checklistDesc;
    @SerializedName("isPhoto")
    @Expose
    private Integer isPhoto;
    @SerializedName("isUsed")
    @Expose
    private Integer isUsed;
    @SerializedName("delStatus")
    @Expose
    private Integer delStatus;
    @SerializedName("createdBy")
    @Expose
    private Integer createdBy;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
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

    public Integer getChecklistDetailId() {
        return checklistDetailId;
    }

    public void setChecklistDetailId(Integer checklistDetailId) {
        this.checklistDetailId = checklistDetailId;
    }

    public Integer getChecklistHeaderId() {
        return checklistHeaderId;
    }

    public void setChecklistHeaderId(Integer checklistHeaderId) {
        this.checklistHeaderId = checklistHeaderId;
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

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "DetailList{" +
                "checklistDetailId=" + checklistDetailId +
                ", checklistHeaderId=" + checklistHeaderId +
                ", checklistDesc='" + checklistDesc + '\'' +
                ", isPhoto=" + isPhoto +
                ", isUsed=" + isUsed +
                ", delStatus=" + delStatus +
                ", createdBy=" + createdBy +
                ", createdDate='" + createdDate + '\'' +
                ", exInt1=" + exInt1 +
                ", exInt2=" + exInt2 +
                ", exVar1='" + exVar1 + '\'' +
                ", exVar2='" + exVar2 + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}
