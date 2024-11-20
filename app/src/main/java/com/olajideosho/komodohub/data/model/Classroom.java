package com.olajideosho.komodohub.data.model;

public class Classroom {
    private int classroomId;
    private String classroomName;
    private int teacherId;

    public Classroom(int classroomId, String classroomName, int teacherId) {
        this.classroomId = classroomId;
        this.classroomName = classroomName;
        this.teacherId = teacherId;
    }

    public int getClassroomId() {
        return classroomId;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }
}