package com.ats.dutyapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupEmp {
    @SerializedName("empId")
    @Expose
    private Integer empId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("userType")
    @Expose
    private Integer userType;

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "GroupEmp{" +
                "empId=" + empId +
                ", name='" + name + '\'' +
                ", photo='" + photo + '\'' +
                ", userType=" + userType +
                '}';
    }
}
