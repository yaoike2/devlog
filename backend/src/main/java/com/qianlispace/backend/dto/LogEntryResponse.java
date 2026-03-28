package com.qianlispace.backend.dto;

import com.qianlispace.backend.entity.LogEntry;

import java.time.LocalDateTime;

public class LogEntryResponse {

    private Long id;
    private String content;
    private String tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static LogEntryResponse from(LogEntry entry) {
        LogEntryResponse resp = new LogEntryResponse();
        resp.id = entry.getId();
        resp.content = entry.getContent();
        resp.tags = entry.getTags();
        resp.createdAt = entry.getCreatedAt();
        resp.updatedAt = entry.getUpdatedAt();
        return resp;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getTags() {
        return tags;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
