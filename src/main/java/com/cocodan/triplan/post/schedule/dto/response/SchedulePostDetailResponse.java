package com.cocodan.triplan.post.schedule.dto.response;

import com.cocodan.triplan.member.domain.vo.GenderType;
import com.cocodan.triplan.post.schedule.domain.SchedulePost;
import com.cocodan.triplan.post.schedule.vo.Ages;
import com.cocodan.triplan.schedule.dto.response.DailyScheduleSpotResponse;
import com.cocodan.triplan.spot.domain.vo.City;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class SchedulePostDetailResponse {

    private Long writerId;

    private String nickname;

    private Ages ages;

    private GenderType gender;

    private City city;

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
            List<SchedulePostCommentResponse> comments,
            Boolean isLiked
    ) {
        return SchedulePostDetailResponse.builder()
                .writerId(schedulePost.getMember().getId())
                .nickname(schedulePost.getMember().getNickname())
                .ages(Ages.from(schedulePost.getMember().getBirth()))
                .gender(schedulePost.getMember().getGender())
                .city(schedulePost.getCity())
                .startDate(schedulePost.getSchedule().getStartDate().toString())
                .endDate(schedulePost.getSchedule().getEndDate().toString())
                .title(schedulePost.getTitle())
                .content(schedulePost.getContent())
                .dailyScheduleSpots(schedulePost.getSchedule().getDailyScheduleSpots().stream()
                        .map(DailyScheduleSpotResponse::from)
                        .collect(Collectors.toList())
                )
                .createdAt(schedulePost.getCreatedDate().toString())
                .views(schedulePost.getViews())
                .likeCount(schedulePost.getLiked())
                .comments(comments)
                .isLiked(isLiked)
                .scheduleId(schedulePost.getSchedule().getId())
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
