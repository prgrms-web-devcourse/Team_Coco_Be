package com.cocodan.triplan.post.schedule.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SchedulePostLikeRequest {

    @NotNull
    private Long schedulePostId;

    @NotNull
    private Boolean flag;
}
