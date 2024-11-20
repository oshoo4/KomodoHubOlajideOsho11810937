package com.olajideosho.komodohub.data.model;

import org.threeten.bp.LocalDateTime;

public class Message {
    private int messageId;
    private int senderId;
    private Integer receiverId;
    private Integer classroomId;
    private String content;
    private LocalDateTime timestamp;

    public Message(
            int messageId,
            int senderId,
            Integer receiverId,
            Integer classroomId,
            String content,
            LocalDateTime timestamp
    ) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.classroomId = classroomId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public int getMessageId() {
        return messageId;
    }

    public int getSenderId() {
        return senderId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public Integer getClassroomId() {
        return classroomId;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setContent(String content) {
        this.content = content;
    }
}