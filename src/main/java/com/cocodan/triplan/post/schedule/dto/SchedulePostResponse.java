package com.cocodan.triplan.post.schedule.dto;

import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.member.domain.vo.GenderType;
import com.cocodan.triplan.post.schedule.vo.Ages;
import com.cocodan.triplan.schedule.domain.Schedule;
import com.cocodan.triplan.schedule.domain.vo.Thema;
import com.cocodan.triplan.spot.domain.vo.City;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

public class SchedulePostResponse {

    private String profileImageUrl;

    private String nickname;

    private String title;

    private Ages ages;

    private GenderType genderType;

    private City city;

    // TODO: 현재 테마가 Tag로 잘못 올라가 있음. Tag 명칭 수정되면 같이 수정
    private List<Thema> themes;

    private LocalDate startDate;

    private LocalDate endDate;

    @Builder
    private SchedulePostResponse(
            String profileImageUrl, String nickname, String title,
            Ages ages, GenderType genderType, City city,
            List<Thema> themes, LocalDate startDate, LocalDate endDate)
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

    // TODO: 2021.12.06 Teru - Tag 명칭 수정되면 수정
    public static SchedulePostResponse from(Member member, Schedule schedule, City city, List<Thema> themes, String title) {
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

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Ages getAges() {
        return ages;
    }

    public void setAges(Ages ages) {
        this.ages = ages;
    }

    public GenderType getGenderType() {
        return genderType;
    }

    public void setGenderType(GenderType genderType) {
        this.genderType = genderType;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public List<Thema> getThemes() {
        return themes;
    }

    public void setThemes(List<Thema> themes) {
        this.themes = themes;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
