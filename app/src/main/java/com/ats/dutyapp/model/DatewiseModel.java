package com.ats.dutyapp.model;

public class DatewiseModel {

    private String value;
    private boolean isChecked;

    public DatewiseModel() {
    }

    public DatewiseModel(String value, boolean isChecked) {
        this.value = value;
        this.isChecked = isChecked;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "DatewiseModel{" +
                "value='" + value + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}

