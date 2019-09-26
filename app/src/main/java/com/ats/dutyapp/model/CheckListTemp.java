package com.ats.dutyapp.model;

public class CheckListTemp {
    private String checklistName;
    private String photo;

    public CheckListTemp(String checklistName) {
        this.checklistName = checklistName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getChecklistName() {
        return checklistName;
    }

    public void setChecklistName(String checklistName) {
        this.checklistName = checklistName;
    }

    @Override
    public String toString() {
        return "CheckListTemp{" +
                "checklistName='" + checklistName + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
