package com.ats.dutyapp.model;

public class TaskTemp {
    private String taskName;
    private String genrateDate;
    private String completionDate;
    private String empName;
    private String status;
    private String remark;

    public TaskTemp(String taskName,String genrateDate, String completionDate, String empName, String status, String remark) {
        this.taskName = taskName;
        this.genrateDate = genrateDate;
        this.completionDate = completionDate;
        this.empName = empName;
        this.status = status;
        this.remark = remark;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getGenrateDate() {
        return genrateDate;
    }

    public void setGenrateDate(String genrateDate) {
        this.genrateDate = genrateDate;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(String completionDate) {
        this.completionDate = completionDate;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "TaskTemp{" +
                "taskName='" + taskName + '\'' +
                ", genrateDate='" + genrateDate + '\'' +
                ", completionDate='" + completionDate + '\'' +
                ", empName='" + empName + '\'' +
                ", status='" + status + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
