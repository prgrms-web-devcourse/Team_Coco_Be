package com.cocodan.triplan.schedule.dto.response;

import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.member.dto.response.MemberSimpleResponse;
import com.cocodan.triplan.schedule.domain.DailyScheduleSpot;
import com.cocodan.triplan.schedule.domain.Schedule;
import com.cocodan.triplan.spot.domain.Spot;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ScheduleDetailResponse {

    private final Long id;

    private final ScheduleSimpleResponse scheduleSimpleResponse;

    private final List<ScheduleSpotResponse> spotResponseList;

    private final List<MemberSimpleResponse> memberSimpleResponses;

    @Builder
    private ScheduleDetailResponse(Long id, ScheduleSimpleResponse scheduleSimpleResponse, List<ScheduleSpotResponse> spotResponseList, List<MemberSimpleResponse> memberSimpleResponses) {
        this.id = id;
        this.scheduleSimpleResponse = scheduleSimpleResponse;
        this.spotResponseList = spotResponseList;
        this.memberSimpleResponses = memberSimpleResponses;
    }

    public static ScheduleDetailResponse of(Schedule schedule, List<Spot> spotList, List<Member> members) {
        return ScheduleDetailResponse.builder()
                .id(schedule.getId())
                .scheduleSimpleResponse(ScheduleSimpleResponse.from(schedule))
                .spotResponseList(getScheduleSpots(schedule.getDailyScheduleSpots(), spotList))
                .memberSimpleResponses(getMemberSimpleResponse(members))
                .build();
    }

    private static List<ScheduleSpotResponse> getScheduleSpots(List<DailyScheduleSpot> dailyScheduleSpots, List<Spot> spots) {
        return dailyScheduleSpots.stream()
                .sorted(ScheduleDetailResponse::sortByDateAndOrder)
                .flatMap(dailyScheduleSpot -> spots.stream()
                        .filter(spot -> spot.getId().equals(dailyScheduleSpot.getSpotId()))
                        .map(spot -> ScheduleSpotResponse.of(spot, dailyScheduleSpot)))
                .collect(Collectors.toList());
    }

    private static int sortByDateAndOrder(DailyScheduleSpot o1, DailyScheduleSpot o2) {
        int compareResult = o1.getDate() - o2.getDate();
        if (compareResult == 0) {
            return o1.getOrder() - o2.getOrder();
        }

        return compareResult;
    }

    private static List<MemberSimpleResponse> getMemberSimpleResponse(List<Member> members) {
        return members.stream()
                .map(ScheduleDetailResponse::getMemberSimpleResponse)
                .collect(Collectors.toList());
    }

    private static MemberSimpleResponse getMemberSimpleResponse(Member member) {
        return MemberSimpleResponse.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .build();
    }
}
