package com.cocodan.triplan.post.schedule.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

import static com.cocodan.triplan.post.schedule.domain.SchedulePostComment.COMMENT_MAX_LENGTH;
import static com.cocodan.triplan.post.schedule.domain.SchedulePostComment.COMMENT_MIN_LENGTH;

@Getter
@AllArgsConstructor
public class SchedulePostCommentRequest {

    @NotBlank // 빈 댓글 금지!
    @Length(min = COMMENT_MIN_LENGTH, max = COMMENT_MAX_LENGTH)
    private String content;

}
