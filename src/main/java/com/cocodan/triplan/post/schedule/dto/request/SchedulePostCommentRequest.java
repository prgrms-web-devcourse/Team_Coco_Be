package com.cocodan.triplan.post.schedule.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import static com.cocodan.triplan.post.schedule.domain.SchedulePostComment.COMMENT_MAX_LENGTH;
import static com.cocodan.triplan.post.schedule.domain.SchedulePostComment.COMMENT_MIN_LENGTH;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SchedulePostCommentRequest {

    @Length(min = COMMENT_MIN_LENGTH, max = COMMENT_MAX_LENGTH)
    private String content;

}
