package com.cocodan.triplan.post.schedule.dto.response;

import com.cocodan.triplan.member.domain.vo.GenderType;
import com.cocodan.triplan.post.schedule.domain.SchedulePost;
import com.cocodan.triplan.post.schedule.vo.Ages;
import com.cocodan.triplan.schedule.domain.Schedule;
import com.cocodan.triplan.schedule.dto.response.DailyScheduleSpotResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class SchedulePostDetailResponse {

    private Long writerId;

    private String nickname;

    private Ages ages;

    private GenderType gender;

    private String city;

    private String startDate;

    private String endDate;

    private String title;

    private String content;

    private List<DailyScheduleSpotResponse> dailyScheduleSpots;

    private String createdAt;

    private Long views;

    private Long likeCount;

    private List<SchedulePostCommentResponse> comments;

    private Boolean isLiked;

    private Long scheduleId;

    public static SchedulePostDetailResponse of(
            SchedulePost schedulePost,
            Schedule schedule,
            List<SchedulePostCommentResponse> comments,
            Boolean isLiked
    ) {
        return SchedulePostDetailResponse.builder()
                .writerId(schedulePost.getMember().getId())
                .nickname(schedulePost.getMember().getNickname())
                .ages(Ages.from(schedulePost.getMember().getBirth()))
                .gender(schedulePost.getMember().getGender())
                .city(schedulePost.getCity().toString())
                .startDate(schedule.getStartDate().toString())
                .endDate(schedule.getEndDate().toString())
                .title(schedulePost.getTitle())
                .content(schedulePost.getContent())
                .dailyScheduleSpots(schedule.getDailyScheduleSpots().stream()
                        .map(DailyScheduleSpotResponse::from)
                        .collect(Collectors.toList())
                )
                .createdAt(schedulePost.getCreatedDate().toString())
                .views(schedulePost.getViews())
                .likeCount(schedulePost.getLiked())
                .comments(comments)
                .isLiked(isLiked)
                .scheduleId(schedule.getId())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchedulePostDetailResponse that = (SchedulePostDetailResponse) o;
        return nickname.equals(that.nickname) && createdAt.equals(that.createdAt);
    }
}
