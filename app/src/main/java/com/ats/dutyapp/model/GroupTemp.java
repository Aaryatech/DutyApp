package com.ats.dutyapp.model;

public class GroupTemp {
    private String grpName;
    private String empName;

    public GroupTemp(String grpName, String empName) {
        this.grpName = grpName;
        this.empName = empName;
    }

    public String getGrpName() {
        return grpName;
    }

    public void setGrpName(String grpName) {
        this.grpName = grpName;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    @Override
    public String toString() {
        return "GroupTemp{" +
                "grpName='" + grpName + '\'' +
                ", empName='" + empName + '\'' +
                '}';
    }
}
