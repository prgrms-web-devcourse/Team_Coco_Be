package com.cocodan.triplan.post.schedule.dto.response;

import com.cocodan.triplan.member.domain.vo.GenderType;
import com.cocodan.triplan.member.dto.response.MemberGetOneResponse;
import com.cocodan.triplan.post.schedule.vo.Ages;
import com.cocodan.triplan.schedule.domain.Schedule;
import com.cocodan.triplan.schedule.domain.vo.Theme;
import com.cocodan.triplan.spot.domain.vo.City;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class SchedulePostResponse {

    private String profileImageUrl;

    private String nickname;

    private String title;

    private Ages ages;

    private GenderType genderType;

    private City city;

    private List<Theme> themes;

    private LocalDate startDate;

    private LocalDate endDate;

    public static SchedulePostResponse from(MemberGetOneResponse member, Schedule schedule, City city, List<Theme> themes, String title) {
        return SchedulePostResponse.builder()
                .profileImageUrl(member.getProfileImage())
                .nickname(member.getNickname())
                .title(title)
                .ages(Ages.from(member.getBirth()))
                .genderType(member.getGender())
                .city(city)
                .themes(themes)
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .build();
    }
}
