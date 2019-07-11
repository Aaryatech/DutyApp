package com.ats.dutyapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeptCount {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("deptId")
    @Expose
    private Integer deptId;
    @SerializedName("deptName")
    @Expose
    private String deptName;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("completed")
    @Expose
    private Integer completed;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getCompleted() {
        return completed;
    }

    public void setCompleted(Integer completed) {
        this.completed = completed;
    }

    public DeptCount(String id, Integer deptId, String deptName, Integer total, Integer completed) {
        this.id = id;
        this.deptId = deptId;
        this.deptName = deptName;
        this.total = total;
        this.completed = completed;
    }
}
