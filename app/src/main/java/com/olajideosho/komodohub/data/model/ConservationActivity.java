package com.olajideosho.komodohub.data.model;

public class ConservationActivity {
    private int activityId;
    private String activityName;
    private String description;
    private int teacherId;

    public ConservationActivity(
            int activityId,
            String activityName,
            String description,
            int teacherId
    ) {
        this.activityId = activityId;
        this.activityName = activityName;
        this.description = description;
        this.teacherId = teacherId;
    }

    public int getActivityId() {
        return activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getDescription() {
        return description;
    }

    public int getTeacherId() {
        return teacherId;
    }
}