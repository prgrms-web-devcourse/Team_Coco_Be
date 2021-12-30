package com.cocodan.triplan.post.schedule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SchedulePostCreationResponse {
    public final Long postId;

    public static SchedulePostCreationResponse from(Long postId) {
        return new SchedulePostCreationResponse(postId);
    }
}
