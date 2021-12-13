package com.cocodan.triplan.post.schedule.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SchedulePostLikeRequest {

    private Long schedulePostId;

    private Boolean flag;
}
