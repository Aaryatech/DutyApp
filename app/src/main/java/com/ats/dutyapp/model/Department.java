package com.ats.dutyapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Department {

    @SerializedName("empDeptId")
    @Expose
    private Integer empDeptId;
    @SerializedName("companyId")
    @Expose
    private Integer companyId;
    @SerializedName("empDeptName")
    @Expose
    private String empDeptName;
    @SerializedName("empDeptShortName")
    @Expose
    private String empDeptShortName;
    @SerializedName("empDeptRemarks")
    @Expose
    private String empDeptRemarks;
    @SerializedName("delStatus")
    @Expose
    private Integer delStatus;
    @SerializedName("isActive")
    @Expose
    private Integer isActive;
    @SerializedName("makerUserId")
    @Expose
    private Integer makerUserId;
    @SerializedName("makerEnterDatetime")
    @Expose
    private String makerEnterDatetime;
    @SerializedName("exInt1")
    @Expose
    private Object exInt1;
    @SerializedName("exInt2")
    @Expose
    private Object exInt2;
    @SerializedName("exInt3")
    @Expose
    private Object exInt3;
    @SerializedName("exVar1")
    @Expose
    private Object exVar1;
    @SerializedName("exVar2")
    @Expose
    private Object exVar2;
    @SerializedName("exVar3")
    @Expose
    private Object exVar3;

    public Integer getEmpDeptId() {
        return empDeptId;
    }

    public void setEmpDeptId(Integer empDeptId) {
        this.empDeptId = empDeptId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getEmpDeptName() {
        return empDeptName;
    }

    public void setEmpDeptName(String empDeptName) {
        this.empDeptName = empDeptName;
    }

    public String getEmpDeptShortName() {
        return empDeptShortName;
    }

    public void setEmpDeptShortName(String empDeptShortName) {
        this.empDeptShortName = empDeptShortName;
    }

    public String getEmpDeptRemarks() {
        return empDeptRemarks;
    }

    public void setEmpDeptRemarks(String empDeptRemarks) {
        this.empDeptRemarks = empDeptRemarks;
    }

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Integer getMakerUserId() {
        return makerUserId;
    }

    public void setMakerUserId(Integer makerUserId) {
        this.makerUserId = makerUserId;
    }

    public String getMakerEnterDatetime() {
        return makerEnterDatetime;
    }

    public void setMakerEnterDatetime(String makerEnterDatetime) {
        this.makerEnterDatetime = makerEnterDatetime;
    }

    public Object getExInt1() {
        return exInt1;
    }

    public void setExInt1(Object exInt1) {
        this.exInt1 = exInt1;
    }

    public Object getExInt2() {
        return exInt2;
    }

    public void setExInt2(Object exInt2) {
        this.exInt2 = exInt2;
    }

    public Object getExInt3() {
        return exInt3;
    }

    public void setExInt3(Object exInt3) {
        this.exInt3 = exInt3;
    }

    public Object getExVar1() {
        return exVar1;
    }

    public void setExVar1(Object exVar1) {
        this.exVar1 = exVar1;
    }

    public Object getExVar2() {
        return exVar2;
    }

    public void setExVar2(Object exVar2) {
        this.exVar2 = exVar2;
    }

    public Object getExVar3() {
        return exVar3;
    }

    public void setExVar3(Object exVar3) {
        this.exVar3 = exVar3;
    }

    @Override
    public String toString() {
        return "Department{" +
                "empDeptId=" + empDeptId +
                ", companyId=" + companyId +
                ", empDeptName='" + empDeptName + '\'' +
                ", empDeptShortName='" + empDeptShortName + '\'' +
                ", empDeptRemarks='" + empDeptRemarks + '\'' +
                ", delStatus=" + delStatus +
                ", isActive=" + isActive +
                ", makerUserId=" + makerUserId +
                ", makerEnterDatetime='" + makerEnterDatetime + '\'' +
                ", exInt1=" + exInt1 +
                ", exInt2=" + exInt2 +
                ", exInt3=" + exInt3 +
                ", exVar1=" + exVar1 +
                ", exVar2=" + exVar2 +
                ", exVar3=" + exVar3 +
                '}';
    }
}
