package com.cocodan.triplan.post.schedule.dto.response;

public class SchedulePostCreateResponse {
    public final Long postId;

    private SchedulePostCreateResponse(Long postId) {
        this.postId = postId;
    }

    public Long getPostId() {
        return postId;
    }

    public static SchedulePostCreateResponse from(Long postId) {
        return new SchedulePostCreateResponse(postId);
    }
}
