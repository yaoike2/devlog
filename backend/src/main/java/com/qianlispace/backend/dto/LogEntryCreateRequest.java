package com.qianlispace.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class LogEntryCreateRequest {

    @NotBlank(message = "内容不能为空")
    private String content;

    private String tags;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
