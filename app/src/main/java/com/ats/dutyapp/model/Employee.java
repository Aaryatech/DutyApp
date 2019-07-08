package com.ats.dutyapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Employee {

    @SerializedName("empId")
    @Expose
    private Integer empId;
    @SerializedName("empDsc")
    @Expose
    private String empDsc;
    @SerializedName("empCode")
    @Expose
    private String empCode;
    @SerializedName("companyId")
    @Expose
    private Integer companyId;
    @SerializedName("empCatId")
    @Expose
    private Integer empCatId;
    @SerializedName("empTypeId")
    @Expose
    private Integer empTypeId;
    @SerializedName("empDeptId")
    @Expose
    private Integer empDeptId;
    @SerializedName("locId")
    @Expose
    private Integer locId;
    @SerializedName("empFname")
    @Expose
    private String empFname;
    @SerializedName("empMname")
    @Expose
    private String empMname;
    @SerializedName("empSname")
    @Expose
    private String empSname;
    @SerializedName("empPhoto")
    @Expose
    private String empPhoto;
    @SerializedName("empMobile1")
    @Expose
    private String empMobile1;
    @SerializedName("empMobile2")
    @Expose
    private String empMobile2;
    @SerializedName("empEmail")
    @Expose
    private String empEmail;
    @SerializedName("empAddressTemp")
    @Expose
    private String empAddressTemp;
    @SerializedName("empAddressPerm")
    @Expose
    private String empAddressPerm;
    @SerializedName("empBloodgrp")
    @Expose
    private String empBloodgrp;
    @SerializedName("empEmergencyPerson1")
    @Expose
    private String empEmergencyPerson1;
    @SerializedName("empEmergencyNo1")
    @Expose
    private String empEmergencyNo1;
    @SerializedName("empEmergencyPerson2")
    @Expose
    private String empEmergencyPerson2;
    @SerializedName("empEmergencyNo2")
    @Expose
    private String empEmergencyNo2;
    @SerializedName("empRatePerhr")
    @Expose
    private Integer empRatePerhr;
    @SerializedName("empJoiningDate")
    @Expose
    private String empJoiningDate;
    @SerializedName("empPrevExpYrs")
    @Expose
    private Integer empPrevExpYrs;
    @SerializedName("empPrevExpMonths")
    @Expose
    private Integer empPrevExpMonths;
    @SerializedName("empLeavingDate")
    @Expose
    private String empLeavingDate;
    @SerializedName("empLeavingReason")
    @Expose
    private String empLeavingReason;
    @SerializedName("lockPeriod")
    @Expose
    private String lockPeriod;
    @SerializedName("termConditions")
    @Expose
    private String termConditions;
    @SerializedName("salaryId")
    @Expose
    private Integer salaryId;
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
    private String exVar2;
    @SerializedName("exVar3")
    @Expose
    private Object exVar3;
    @SerializedName("grossSalary")
    @Expose
    private Integer grossSalary;
    @SerializedName("noOfHrs")
    @Expose
    private Integer noOfHrs;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("scanCopy1")
    @Expose
    private String scanCopy1;
    @SerializedName("scanCopy2")
    @Expose
    private String scanCopy2;
    @SerializedName("pf")
    @Expose
    private Integer pf;
    @SerializedName("esic")
    @Expose
    private Integer esic;
    @SerializedName("bonus")
    @Expose
    private Integer bonus;
    @SerializedName("cl")
    @Expose
    private Integer cl;
    @SerializedName("sl")
    @Expose
    private Integer sl;
    @SerializedName("pl")
    @Expose
    private Integer pl;

   private boolean isChecked;


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public String getEmpDsc() {
        return empDsc;
    }

    public void setEmpDsc(String empDsc) {
        this.empDsc = empDsc;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getEmpCatId() {
        return empCatId;
    }

    public void setEmpCatId(Integer empCatId) {
        this.empCatId = empCatId;
    }

    public Integer getEmpTypeId() {
        return empTypeId;
    }

    public void setEmpTypeId(Integer empTypeId) {
        this.empTypeId = empTypeId;
    }

    public Integer getEmpDeptId() {
        return empDeptId;
    }

    public void setEmpDeptId(Integer empDeptId) {
        this.empDeptId = empDeptId;
    }

    public Integer getLocId() {
        return locId;
    }

    public void setLocId(Integer locId) {
        this.locId = locId;
    }

    public String getEmpFname() {
        return empFname;
    }

    public void setEmpFname(String empFname) {
        this.empFname = empFname;
    }

    public String getEmpMname() {
        return empMname;
    }

    public void setEmpMname(String empMname) {
        this.empMname = empMname;
    }

    public String getEmpSname() {
        return empSname;
    }

    public void setEmpSname(String empSname) {
        this.empSname = empSname;
    }

    public String getEmpPhoto() {
        return empPhoto;
    }

    public void setEmpPhoto(String empPhoto) {
        this.empPhoto = empPhoto;
    }

    public String getEmpMobile1() {
        return empMobile1;
    }

    public void setEmpMobile1(String empMobile1) {
        this.empMobile1 = empMobile1;
    }

    public String getEmpMobile2() {
        return empMobile2;
    }

    public void setEmpMobile2(String empMobile2) {
        this.empMobile2 = empMobile2;
    }

    public String getEmpEmail() {
        return empEmail;
    }

    public void setEmpEmail(String empEmail) {
        this.empEmail = empEmail;
    }

    public String getEmpAddressTemp() {
        return empAddressTemp;
    }

    public void setEmpAddressTemp(String empAddressTemp) {
        this.empAddressTemp = empAddressTemp;
    }

    public String getEmpAddressPerm() {
        return empAddressPerm;
    }

    public void setEmpAddressPerm(String empAddressPerm) {
        this.empAddressPerm = empAddressPerm;
    }

    public String getEmpBloodgrp() {
        return empBloodgrp;
    }

    public void setEmpBloodgrp(String empBloodgrp) {
        this.empBloodgrp = empBloodgrp;
    }

    public String getEmpEmergencyPerson1() {
        return empEmergencyPerson1;
    }

    public void setEmpEmergencyPerson1(String empEmergencyPerson1) {
        this.empEmergencyPerson1 = empEmergencyPerson1;
    }

    public String getEmpEmergencyNo1() {
        return empEmergencyNo1;
    }

    public void setEmpEmergencyNo1(String empEmergencyNo1) {
        this.empEmergencyNo1 = empEmergencyNo1;
    }

    public String getEmpEmergencyPerson2() {
        return empEmergencyPerson2;
    }

    public void setEmpEmergencyPerson2(String empEmergencyPerson2) {
        this.empEmergencyPerson2 = empEmergencyPerson2;
    }

    public String getEmpEmergencyNo2() {
        return empEmergencyNo2;
    }

    public void setEmpEmergencyNo2(String empEmergencyNo2) {
        this.empEmergencyNo2 = empEmergencyNo2;
    }

    public Integer getEmpRatePerhr() {
        return empRatePerhr;
    }

    public void setEmpRatePerhr(Integer empRatePerhr) {
        this.empRatePerhr = empRatePerhr;
    }

    public String getEmpJoiningDate() {
        return empJoiningDate;
    }

    public void setEmpJoiningDate(String empJoiningDate) {
        this.empJoiningDate = empJoiningDate;
    }

    public Integer getEmpPrevExpYrs() {
        return empPrevExpYrs;
    }

    public void setEmpPrevExpYrs(Integer empPrevExpYrs) {
        this.empPrevExpYrs = empPrevExpYrs;
    }

    public Integer getEmpPrevExpMonths() {
        return empPrevExpMonths;
    }

    public void setEmpPrevExpMonths(Integer empPrevExpMonths) {
        this.empPrevExpMonths = empPrevExpMonths;
    }

    public String getEmpLeavingDate() {
        return empLeavingDate;
    }

    public void setEmpLeavingDate(String empLeavingDate) {
        this.empLeavingDate = empLeavingDate;
    }

    public String getEmpLeavingReason() {
        return empLeavingReason;
    }

    public void setEmpLeavingReason(String empLeavingReason) {
        this.empLeavingReason = empLeavingReason;
    }

    public String getLockPeriod() {
        return lockPeriod;
    }

    public void setLockPeriod(String lockPeriod) {
        this.lockPeriod = lockPeriod;
    }

    public String getTermConditions() {
        return termConditions;
    }

    public void setTermConditions(String termConditions) {
        this.termConditions = termConditions;
    }

    public Integer getSalaryId() {
        return salaryId;
    }

    public void setSalaryId(Integer salaryId) {
        this.salaryId = salaryId;
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

    public String getExVar2() {
        return exVar2;
    }

    public void setExVar2(String exVar2) {
        this.exVar2 = exVar2;
    }

    public Object getExVar3() {
        return exVar3;
    }

    public void setExVar3(Object exVar3) {
        this.exVar3 = exVar3;
    }

    public Integer getGrossSalary() {
        return grossSalary;
    }

    public void setGrossSalary(Integer grossSalary) {
        this.grossSalary = grossSalary;
    }

    public Integer getNoOfHrs() {
        return noOfHrs;
    }

    public void setNoOfHrs(Integer noOfHrs) {
        this.noOfHrs = noOfHrs;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getScanCopy1() {
        return scanCopy1;
    }

    public void setScanCopy1(String scanCopy1) {
        this.scanCopy1 = scanCopy1;
    }

    public String getScanCopy2() {
        return scanCopy2;
    }

    public void setScanCopy2(String scanCopy2) {
        this.scanCopy2 = scanCopy2;
    }

    public Integer getPf() {
        return pf;
    }

    public void setPf(Integer pf) {
        this.pf = pf;
    }

    public Integer getEsic() {
        return esic;
    }

    public void setEsic(Integer esic) {
        this.esic = esic;
    }

    public Integer getBonus() {
        return bonus;
    }

    public void setBonus(Integer bonus) {
        this.bonus = bonus;
    }

    public Integer getCl() {
        return cl;
    }

    public void setCl(Integer cl) {
        this.cl = cl;
    }

    public Integer getSl() {
        return sl;
    }

    public void setSl(Integer sl) {
        this.sl = sl;
    }

    public Integer getPl() {
        return pl;
    }

    public void setPl(Integer pl) {
        this.pl = pl;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "empId=" + empId +
                ", empDsc='" + empDsc + '\'' +
                ", empCode='" + empCode + '\'' +
                ", companyId=" + companyId +
                ", empCatId=" + empCatId +
                ", empTypeId=" + empTypeId +
                ", empDeptId=" + empDeptId +
                ", locId=" + locId +
                ", empFname='" + empFname + '\'' +
                ", empMname='" + empMname + '\'' +
                ", empSname='" + empSname + '\'' +
                ", empPhoto='" + empPhoto + '\'' +
                ", empMobile1='" + empMobile1 + '\'' +
                ", empMobile2='" + empMobile2 + '\'' +
                ", empEmail='" + empEmail + '\'' +
                ", empAddressTemp='" + empAddressTemp + '\'' +
                ", empAddressPerm='" + empAddressPerm + '\'' +
                ", empBloodgrp='" + empBloodgrp + '\'' +
                ", empEmergencyPerson1='" + empEmergencyPerson1 + '\'' +
                ", empEmergencyNo1='" + empEmergencyNo1 + '\'' +
                ", empEmergencyPerson2='" + empEmergencyPerson2 + '\'' +
                ", empEmergencyNo2='" + empEmergencyNo2 + '\'' +
                ", empRatePerhr=" + empRatePerhr +
                ", empJoiningDate='" + empJoiningDate + '\'' +
                ", empPrevExpYrs=" + empPrevExpYrs +
                ", empPrevExpMonths=" + empPrevExpMonths +
                ", empLeavingDate='" + empLeavingDate + '\'' +
                ", empLeavingReason='" + empLeavingReason + '\'' +
                ", lockPeriod='" + lockPeriod + '\'' +
                ", termConditions='" + termConditions + '\'' +
                ", salaryId=" + salaryId +
                ", delStatus=" + delStatus +
                ", isActive=" + isActive +
                ", makerUserId=" + makerUserId +
                ", makerEnterDatetime='" + makerEnterDatetime + '\'' +
                ", exInt1=" + exInt1 +
                ", exInt2=" + exInt2 +
                ", exInt3=" + exInt3 +
                ", exVar1=" + exVar1 +
                ", exVar2='" + exVar2 + '\'' +
                ", exVar3=" + exVar3 +
                ", grossSalary=" + grossSalary +
                ", noOfHrs=" + noOfHrs +
                ", gender='" + gender + '\'' +
                ", dob='" + dob + '\'' +
                ", scanCopy1='" + scanCopy1 + '\'' +
                ", scanCopy2='" + scanCopy2 + '\'' +
                ", pf=" + pf +
                ", esic=" + esic +
                ", bonus=" + bonus +
                ", cl=" + cl +
                ", sl=" + sl +
                ", pl=" + pl +
                ", isChecked=" + isChecked +
                '}';
    }
}
