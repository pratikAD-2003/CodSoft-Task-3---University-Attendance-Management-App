package com.example.universityattendancemanagement.Faculty;

public class AttendanceModel {
    public String getDate() {
        return date;
    }

    public AttendanceModel(String date, String attendanceCount) {
        this.date = date;
        this.attendanceCount = attendanceCount;
    }

    public AttendanceModel() {
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAttendanceCount() {
        return attendanceCount;
    }

    public void setAttendanceCount(String attendanceCount) {
        this.attendanceCount = attendanceCount;
    }

    String date,attendanceCount;
}
