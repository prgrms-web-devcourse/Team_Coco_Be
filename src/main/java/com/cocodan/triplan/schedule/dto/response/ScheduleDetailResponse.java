package com.cocodan.triplan.schedule.dto.response;

import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.schedule.domain.DailyScheduleSpot;
import com.cocodan.triplan.schedule.domain.Schedule;
import com.cocodan.triplan.spot.domain.Spot;
import com.cocodan.triplan.spot.dto.response.SpotResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ScheduleDetailResponse {

    private final ScheduleSimpleResponse scheduleSimpleResponse;

    private final List<ScheduleSpotResponse> spotResponseList;

    private final List<MemberSimpleResponse> memberSimpleResponses;

    public static ScheduleDetailResponse of(Schedule schedule, List<Spot> spotList, List<Member> members) {
        return ScheduleDetailResponse.builder()
                .scheduleSimpleResponse(ScheduleSimpleResponse.from(schedule))
                .spotResponseList(getScheduleSpots(schedule.getDailyScheduleSpots(), spotList))
                .memberSimpleResponses(getMemberSimpleResponse(members))
                .build();
    }

    private static List<ScheduleSpotResponse> getScheduleSpots(List<DailyScheduleSpot> dailyScheduleSpots, List<Spot> spots) {
        List<ScheduleSpotResponse> result = new ArrayList<>();

        dailyScheduleSpots.forEach(
                dailyScheduleSpot -> spots.forEach(
                        spot -> {
                            if (dailyScheduleSpot.getSpotId().equals(spot.getId())) {
                                result.add(ScheduleSpotResponse.of(spot, dailyScheduleSpot));
                            }
                        }));

        return result;
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
