package com.cocodan.triplan.post.schedule.dto.request;

import lombok.Builder;

import javax.validation.constraints.NotBlank;

public class SchedulePostCreateRequest {

    @NotBlank
    public String title;

    @NotBlank
    public String content;

    @NotBlank
    public String city;

    @NotBlank
    public Long scheduleId;

    @Builder
    public SchedulePostCreateRequest(String title, String content, String city, Long scheduleId) {
        this.title = title;
        this.content = content;
        this.city = city;
        this.scheduleId = scheduleId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getCity() {
        return city;
    }

    public Long getScheduleId() {
        return scheduleId;
    }
}
