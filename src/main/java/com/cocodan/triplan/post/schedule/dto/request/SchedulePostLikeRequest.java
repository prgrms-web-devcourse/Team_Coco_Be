package com.cocodan.triplan.post.schedule.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class SchedulePostLikeRequest {

    @NotNull
    private Long schedulePostId;

    @NotNull
    private Boolean flag;
}
