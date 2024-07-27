package com.example.universityattendancemanagement.Student;

public class StudentModel {
    String uid, name, roleNumber, session, course, branch, securityPin, date, type;
    int attendance, absents;
    public static final String STUDENT = "student";
    public static final String FACULTY = "faculty";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public StudentModel() {
    }

    public StudentModel(String uid, String name, String roleNumber, String session, String course, String branch, String securityPin, String date, String type, int attendance, int absents) {
        this.uid = uid;
        this.name = name;
        this.roleNumber = roleNumber;
        this.session = session;
        this.course = course;
        this.branch = branch;
        this.securityPin = securityPin;
        this.date = date;
        this.type = type;
        this.attendance = attendance;
        this.absents = absents;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoleNumber() {
        return roleNumber;
    }

    public void setRoleNumber(String roleNumber) {
        this.roleNumber = roleNumber;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
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

    public String getSecurityPin() {
        return securityPin;
    }

    public void setSecurityPin(String securityPin) {
        this.securityPin = securityPin;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    public int getAbsents() {
        return absents;
    }

    public void setAbsents(int absents) {
        this.absents = absents;
    }
}
