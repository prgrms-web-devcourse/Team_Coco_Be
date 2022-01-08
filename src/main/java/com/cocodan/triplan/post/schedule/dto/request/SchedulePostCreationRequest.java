package com.cocodan.triplan.post.schedule.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.cocodan.triplan.post.schedule.domain.SchedulePost.SCHEDULE_POST_CONTENT_MAX_LENGTH;
import static com.cocodan.triplan.post.schedule.domain.SchedulePost.SCHEDULE_POST_CONTENT_MIN_LENGTH;
import static com.cocodan.triplan.post.schedule.domain.SchedulePost.SCHEDULE_POST_TITLE_MAX_LENGTH;
import static com.cocodan.triplan.post.schedule.domain.SchedulePost.SCHEDULE_POST_TITLE_MIN_LENGTH;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SchedulePostCreationRequest {

    @Length(min = SCHEDULE_POST_TITLE_MIN_LENGTH, max = SCHEDULE_POST_TITLE_MAX_LENGTH)
    public String title;

    @Length(min = SCHEDULE_POST_CONTENT_MIN_LENGTH, max = SCHEDULE_POST_CONTENT_MAX_LENGTH)
    public String content;

    @NotBlank
    public String city;

    @NotNull
    public Long scheduleId;
}
