package com.cocodan.triplan.post.schedule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SchedulePostCreateResponse {
    public final Long postId;

    public static SchedulePostCreateResponse from(Long postId) {
        return new SchedulePostCreateResponse(postId);
    }
}
