package com.ats.dutyapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupEmp {
    private int empId;
    private String name;
    private String photo;
    private int userType;

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
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

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
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
