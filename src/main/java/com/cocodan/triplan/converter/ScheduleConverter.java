package com.cocodan.triplan.converter;

import com.cocodan.triplan.schedule.domain.DailyScheduleSpot;
import com.cocodan.triplan.schedule.domain.Schedule;
import com.cocodan.triplan.schedule.domain.ScheduleThema;
import com.cocodan.triplan.schedule.domain.vo.Thema;
import com.cocodan.triplan.schedule.dto.request.DailyScheduleSpotCreationRequest;
import com.cocodan.triplan.schedule.dto.request.ScheduleCreationRequest;
import com.cocodan.triplan.schedule.dto.request.ScheduleModificationRequest;
import com.cocodan.triplan.schedule.dto.response.ScheduleDetailResponse;
import com.cocodan.triplan.schedule.dto.response.ScheduleSimpleResponse;
import com.cocodan.triplan.spot.domain.Spot;
import com.cocodan.triplan.spot.dto.response.SpotSimple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ScheduleConverter {

    private final SpotConverter spotConverter;

    public Schedule convertSchedule(ScheduleCreationRequest scheduleCreationRequest, Long memberId) {
        Schedule schedule = Schedule.builder()
                .title(scheduleCreationRequest.getTitle())
                .startDate(scheduleCreationRequest.getStartDate())
                .endDate(scheduleCreationRequest.getEndDate())
                .memberId(memberId)
                .build();

        scheduleCreationRequest.getThemas()
                .stream()
                .map(s -> Thema.valueOf(s.toUpperCase()))
                .map(thema -> new ScheduleThema(schedule, thema))
                .collect(Collectors.toList());

        scheduleCreationRequest.getDailyScheduleSpotCreationRequests()
                .stream()
                .map(dailyScheduleSpotCreationRequest -> getDailyScheduleSpot(schedule, dailyScheduleSpotCreationRequest))
                .collect(Collectors.toList());

        return schedule;
    }

    private DailyScheduleSpot getDailyScheduleSpot(Schedule schedule, DailyScheduleSpotCreationRequest dailyScheduleSpotCreationRequest) {
        return DailyScheduleSpot.builder()
                .spotId(dailyScheduleSpotCreationRequest.getSpotId())
                .date(dailyScheduleSpotCreationRequest.getDate())
                .schedule(schedule)
                .build();
    }


    public ScheduleSimpleResponse convertScheduleSimple(Schedule schedule) {
        return ScheduleSimpleResponse.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .themas(getThema(schedule))
                .build();
    }

    private List<Thema> getThema(Schedule schedule) {
        return schedule.getScheduleThemas().stream()
                .map(ScheduleThema::getThema)
                .collect(Collectors.toList());
    }

    public ScheduleDetailResponse convertScheduleDetail(Schedule schedule, List<Spot> spotList, List<String> imageUrls) {
        return ScheduleDetailResponse.builder()
                .id(schedule.getId())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .title(schedule.getTitle())
                .themas(getThema(schedule))
                .spotSimpleList(getSpotSimple(spotList))
                .memberImageUrls(imageUrls)
                .build();

        //TODO : memberImageUrl 추가
    }

    private List<SpotSimple> getSpotSimple(List<Spot> spotList) {
        return spotList.stream()
                .map(spotConverter::convertSpotSimple)
                .collect(Collectors.toList());
    }

    public List<DailyScheduleSpot> convertDailyScheduleSpotList(Schedule schedule, ScheduleModificationRequest scheduleModificationRequest) {
        return scheduleModificationRequest.getDailyScheduleSpotCreationRequests().stream()
                .map(dailyScheduleSpotCreationRequest -> getDailyScheduleSpot(schedule, dailyScheduleSpotCreationRequest))
                .collect(Collectors.toList());
    }
}
