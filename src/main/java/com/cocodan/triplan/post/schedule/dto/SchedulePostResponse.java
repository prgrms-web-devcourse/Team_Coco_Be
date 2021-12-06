package com.cocodan.triplan.post.schedule.dto;

import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.member.domain.vo.GenderType;
import com.cocodan.triplan.post.schedule.vo.Ages;
import com.cocodan.triplan.schedule.domain.Schedule;
import com.cocodan.triplan.schedule.domain.vo.Tag;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

public class SchedulePostResponse {

    public String profileImageUrl;

    public String nickname;

    public String title;

    public Ages ages;

    public GenderType genderType;

    // TODO: 2021.12.06 Teru - 도시 엔티티 구현되면 수정
    public String city;

    // TODO: 현재 테마가 Tag로 잘못 올라가 있음. Tag 명칭 수정되면 같이 수정
    public List<Tag> themes;

    public LocalDate startDate;

    public LocalDate endDate;

    @Builder
    private SchedulePostResponse(
            String profileImageUrl, String nickname, String title,
            Ages ages, GenderType genderType, String city,
            List<Tag> themes, LocalDate startDate, LocalDate endDate)
    {
        this.profileImageUrl = profileImageUrl;
        this.nickname = nickname;
        this.title = title;
        this.ages = ages;
        this.genderType = genderType;
        this.city = city;
        this.themes = themes;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // TODO: 2021.12.06 Teru - 도시 엔티니 구현되면 수정, Tag 명칭 수정되면 수정
    public static SchedulePostResponse from(Member member, Schedule schedule, String city, List<Tag> themes, String title) {
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
