package com.cocodan.triplan.schedule.dto.response;

import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.member.dto.response.MemberSimpleResponse;
import com.cocodan.triplan.schedule.domain.DailyScheduleSpot;
import com.cocodan.triplan.schedule.domain.Schedule;
import com.cocodan.triplan.schedule.domain.ScheduleTheme;
import com.cocodan.triplan.schedule.domain.vo.Theme;
import com.cocodan.triplan.spot.domain.Spot;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduleResponse {

    private final Long id;

    private final String title;

    private final String startDate;

    private final String endDate;

    private final List<Theme> themes;

    private final List<ScheduleSpotResponse> spotResponseList;

    private final List<MemberSimpleResponse> memberSimpleResponses;

    public static ScheduleResponse from(Schedule schedule) {
        return ScheduleResponse.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .startDate(schedule.getStartDate().toString())
                .endDate(schedule.getEndDate().toString())
                .themes(getThemes(schedule))
                .build();
    }

    public static List<Theme> getThemes(Schedule schedule) {
        return schedule.getScheduleThemes().stream()
                .map(ScheduleTheme::getTheme)
                .collect(Collectors.toList());
    }

    public static ScheduleResponse of(Schedule schedule, List<Spot> spotList, List<Member> members) {
        return ScheduleResponse.builder()
                .id(schedule.getId())
                .id(schedule.getId())
                .title(schedule.getTitle())
                .startDate(schedule.getStartDate().toString())
                .endDate(schedule.getEndDate().toString())
                .themes(getThemes(schedule))
                .spotResponseList(getScheduleSpots(schedule.getDailyScheduleSpots(), spotList))
                .memberSimpleResponses(getMemberSimpleResponse(members))
                .build();
    }

    private static List<ScheduleSpotResponse> getScheduleSpots(List<DailyScheduleSpot> dailyScheduleSpots, List<Spot> spots) {
        return dailyScheduleSpots.stream()
                .sorted(ScheduleResponse::sortByDateAndOrder)
                .flatMap(dailyScheduleSpot -> spots.stream()
                        .filter(spot -> spot.getId().equals(dailyScheduleSpot.getSpotId()))
                        .map(spot -> ScheduleSpotResponse.of(spot, dailyScheduleSpot)))
                .collect(Collectors.toList());
    }

    private static int sortByDateAndOrder(DailyScheduleSpot o1, DailyScheduleSpot o2) {
        int compareResult = o1.getDateOrder() - o2.getDateOrder();
        if (compareResult == 0) {
            return o1.getSpotOrder() - o2.getSpotOrder();
        }

        return compareResult;
    }

    private static List<MemberSimpleResponse> getMemberSimpleResponse(List<Member> members) {
        return members.stream()
                .map(ScheduleResponse::getMemberSimpleResponse)
                .collect(Collectors.toList());
    }

    private static MemberSimpleResponse getMemberSimpleResponse(Member member) {
        return MemberSimpleResponse.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .imageUrl(member.getProfileImage())
                .build();
    }
}
