package com.example.universityattendancemanagement.Faculty;

public class CourseModel {
    int classCount;
    String course, branch, session, date, pushId, isTodayHoliday;

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIsTodayHoliday() {
        return isTodayHoliday;
    }

    public void setIsTodayHoliday(String isTodayHoliday) {
        this.isTodayHoliday = isTodayHoliday;
    }

    public CourseModel(int classCount, String course, String branch, String session, String isTodayHoliday, String date, String pushId) {
        this.classCount = classCount;
        this.course = course;
        this.branch = branch;
        this.session = session;
        this.isTodayHoliday = isTodayHoliday;
        this.date = date;
        this.pushId = pushId;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public CourseModel() {
    }

    public int getClassCount() {
        return classCount;
    }

    public void setClassCount(int classCount) {
        this.classCount = classCount;
    }

}
