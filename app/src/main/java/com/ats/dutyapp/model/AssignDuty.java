package com.ats.dutyapp.model;

public class AssignDuty {
    private int assign_id;
    private String assign_date;
    private int duty_id;
    private String emp_ids;
    private String notify_time;
    private int task_assign_user_id;
    private String last_edit_date;
    private int last_edit_user_id;
    private int del_status;
    private int ex_int1;
    private int ex_int2;
    private int ex_int3;
    private String ex_var1;
    private String ex_var2;
    private String ex_var3;

    public AssignDuty(int assign_id, String assign_date, int duty_id, String emp_ids, String notify_time, int task_assign_user_id, String last_edit_date, int last_edit_user_id, int del_status, int ex_int1, int ex_int2, int ex_int3, String ex_var1, String ex_var2, String ex_var3) {
        this.assign_id = assign_id;
        this.assign_date = assign_date;
        this.duty_id = duty_id;
        this.emp_ids = emp_ids;
        this.notify_time = notify_time;
        this.task_assign_user_id = task_assign_user_id;
        this.last_edit_date = last_edit_date;
        this.last_edit_user_id = last_edit_user_id;
        this.del_status = del_status;
        this.ex_int1 = ex_int1;
        this.ex_int2 = ex_int2;
        this.ex_int3 = ex_int3;
        this.ex_var1 = ex_var1;
        this.ex_var2 = ex_var2;
        this.ex_var3 = ex_var3;
    }

    public int getAssign_id() {
        return assign_id;
    }

    public void setAssign_id(int assign_id) {
        this.assign_id = assign_id;
    }

    public String getAssign_date() {
        return assign_date;
    }

    public void setAssign_date(String assign_date) {
        this.assign_date = assign_date;
    }

    public int getDuty_id() {
        return duty_id;
    }

    public void setDuty_id(int duty_id) {
        this.duty_id = duty_id;
    }

    public String getEmp_ids() {
        return emp_ids;
    }

    public void setEmp_ids(String emp_ids) {
        this.emp_ids = emp_ids;
    }

    public String getNotify_time() {
        return notify_time;
    }

    public void setNotify_time(String notify_time) {
        this.notify_time = notify_time;
    }

    public int getTask_assign_user_id() {
        return task_assign_user_id;
    }

    public void setTask_assign_user_id(int task_assign_user_id) {
        this.task_assign_user_id = task_assign_user_id;
    }

    public String getLast_edit_date() {
        return last_edit_date;
    }

    public void setLast_edit_date(String last_edit_date) {
        this.last_edit_date = last_edit_date;
    }

    public int getLast_edit_user_id() {
        return last_edit_user_id;
    }

    public void setLast_edit_user_id(int last_edit_user_id) {
        this.last_edit_user_id = last_edit_user_id;
    }

    public int getDel_status() {
        return del_status;
    }

    public void setDel_status(int del_status) {
        this.del_status = del_status;
    }

    public int getEx_int1() {
        return ex_int1;
    }

    public void setEx_int1(int ex_int1) {
        this.ex_int1 = ex_int1;
    }

    public int getEx_int2() {
        return ex_int2;
    }

    public void setEx_int2(int ex_int2) {
        this.ex_int2 = ex_int2;
    }

    public int getEx_int3() {
        return ex_int3;
    }

    public void setEx_int3(int ex_int3) {
        this.ex_int3 = ex_int3;
    }

    public String getEx_var1() {
        return ex_var1;
    }

    public void setEx_var1(String ex_var1) {
        this.ex_var1 = ex_var1;
    }

    public String getEx_var2() {
        return ex_var2;
    }

    public void setEx_var2(String ex_var2) {
        this.ex_var2 = ex_var2;
    }

    public String getEx_var3() {
        return ex_var3;
    }

    public void setEx_var3(String ex_var3) {
        this.ex_var3 = ex_var3;
    }

    @Override
    public String toString() {
        return "AssignDuty{" +
                "assign_id=" + assign_id +
                ", assign_date='" + assign_date + '\'' +
                ", duty_id=" + duty_id +
                ", emp_ids='" + emp_ids + '\'' +
                ", notify_time='" + notify_time + '\'' +
                ", task_assign_user_id=" + task_assign_user_id +
                ", last_edit_date='" + last_edit_date + '\'' +
                ", last_edit_user_id=" + last_edit_user_id +
                ", del_status=" + del_status +
                ", ex_int1=" + ex_int1 +
                ", ex_int2=" + ex_int2 +
                ", ex_int3=" + ex_int3 +
                ", ex_var1='" + ex_var1 + '\'' +
                ", ex_var2='" + ex_var2 + '\'' +
                ", ex_var3='" + ex_var3 + '\'' +
                '}';
    }
}
