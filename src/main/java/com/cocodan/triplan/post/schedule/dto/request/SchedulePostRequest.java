package com.cocodan.triplan.post.schedule.dto.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class SchedulePostRequest {

    @NotBlank
    public String title;

    @NotBlank
    public String content;

    @NotBlank
    public String city;

    @NotBlank
    public Long scheduleId;
}
