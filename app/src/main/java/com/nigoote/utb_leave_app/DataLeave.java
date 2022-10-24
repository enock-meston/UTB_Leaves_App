package com.nigoote.utb_leave_app;

public class DataLeave {
    private String Lid;
    private String title;
    private String days;

    public DataLeave(String lid, String title,String days) {
        Lid = lid;
        this.title = title;
        this.days = days;
    }

    public String getLid() {
        return Lid;
    }

    public String getTitle() {
        return title;
    }

    public String getDays() {
        return days;
    }
}
