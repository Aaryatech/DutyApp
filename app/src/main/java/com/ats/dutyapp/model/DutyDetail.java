package com.ats.dutyapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DutyDetail {

    @SerializedName("taskDoneDetailId")
    @Expose
    private Integer taskDoneDetailId;
    @SerializedName("taskDoneHeaderId")
    @Expose
    private Integer taskDoneHeaderId;
    @SerializedName("dutyId")
    @Expose
    private Integer dutyId;
    @SerializedName("taskId")
    @Expose
    private Integer taskId;
    @SerializedName("taskName")
    @Expose
    private String taskName;
    @SerializedName("taskDesc")
    @Expose
    private String taskDesc;
    @SerializedName("completedDate")
    @Expose
    private String completedDate;
    @SerializedName("photo1")
    @Expose
    private String photo1;
    @SerializedName("photo2")
    @Expose
    private String photo2;
    @SerializedName("photo3")
    @Expose
    private String photo3;
    @SerializedName("photo4")
    @Expose
    private String photo4;
    @SerializedName("photo5")
    @Expose
    private String photo5;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("taskWeight")
    @Expose
    private Integer taskWeight;
    @SerializedName("taskStatus")
    @Expose
    private Integer taskStatus;
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

    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public Integer getTaskDoneDetailId() {
        return taskDoneDetailId;
    }

    public void setTaskDoneDetailId(Integer taskDoneDetailId) {
        this.taskDoneDetailId = taskDoneDetailId;
    }

    public Integer getTaskDoneHeaderId() {
        return taskDoneHeaderId;
    }

    public void setTaskDoneHeaderId(Integer taskDoneHeaderId) {
        this.taskDoneHeaderId = taskDoneHeaderId;
    }

    public Integer getDutyId() {
        return dutyId;
    }

    public void setDutyId(Integer dutyId) {
        this.dutyId = dutyId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public String getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }

    public String getPhoto1() {
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public String getPhoto2() {
        return photo2;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

    public String getPhoto3() {
        return photo3;
    }

    public void setPhoto3(String photo3) {
        this.photo3 = photo3;
    }

    public String getPhoto4() {
        return photo4;
    }

    public void setPhoto4(String photo4) {
        this.photo4 = photo4;
    }

    public String getPhoto5() {
        return photo5;
    }

    public void setPhoto5(String photo5) {
        this.photo5 = photo5;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getTaskWeight() {
        return taskWeight;
    }

    public void setTaskWeight(Integer taskWeight) {
        this.taskWeight = taskWeight;
    }

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
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

    @Override
    public String toString() {
        return "DutyDetail{" +
                "taskDoneDetailId=" + taskDoneDetailId +
                ", taskDoneHeaderId=" + taskDoneHeaderId +
                ", dutyId=" + dutyId +
                ", taskId=" + taskId +
                ", taskName='" + taskName + '\'' +
                ", taskDesc='" + taskDesc + '\'' +
                ", completedDate='" + completedDate + '\'' +
                ", photo1='" + photo1 + '\'' +
                ", photo2='" + photo2 + '\'' +
                ", photo3='" + photo3 + '\'' +
                ", photo4='" + photo4 + '\'' +
                ", photo5='" + photo5 + '\'' +
                ", remark='" + remark + '\'' +
                ", taskWeight=" + taskWeight +
                ", taskStatus=" + taskStatus +
                ", delStatus=" + delStatus +
                ", exInt1=" + exInt1 +
                ", exInt2=" + exInt2 +
                ", exInt3=" + exInt3 +
                ", exVar1='" + exVar1 + '\'' +
                ", exVar2='" + exVar2 + '\'' +
                ", exVar3='" + exVar3 + '\'' +
                ", taskNameEng='" + taskNameEng + '\'' +
                ", taskNameMar='" + taskNameMar + '\'' +
                ", taskNameHin='" + taskNameHin + '\'' +
                ", taskDescEng='" + taskDescEng + '\'' +
                ", taskDescMar='" + taskDescMar + '\'' +
                ", taskDescHin='" + taskDescHin + '\'' +
                ", photoReq=" + photoReq +
                ", remarkReq=" + remarkReq +
                ", isChecked=" + isChecked +
                '}';
    }
}
