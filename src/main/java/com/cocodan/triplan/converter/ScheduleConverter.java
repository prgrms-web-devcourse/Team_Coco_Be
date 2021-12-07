package com.cocodan.triplan.converter;

import com.cocodan.triplan.schedule.domain.DailyScheduleSpot;
import com.cocodan.triplan.schedule.domain.Schedule;
import com.cocodan.triplan.schedule.domain.ScheduleThema;
import com.cocodan.triplan.schedule.domain.vo.Thema;
import com.cocodan.triplan.schedule.dto.request.DailyScheduleSpotCreation;
import com.cocodan.triplan.schedule.dto.request.ScheduleCreation;
import com.cocodan.triplan.schedule.dto.response.ScheduleDetail;
import com.cocodan.triplan.schedule.dto.response.ScheduleSimple;
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

    public Schedule convertSchedule(ScheduleCreation scheduleCreation) {
        Schedule schedule = Schedule.builder()
                .title(scheduleCreation.getTitle())
                .startDate(scheduleCreation.getStartDate())
                .endDate(scheduleCreation.getEndDate())
                .build();

        scheduleCreation.getThemas()
                .stream()
                .map(s -> Thema.valueOf(s.toUpperCase()))
                .map(thema -> new ScheduleThema(schedule, thema))
                .collect(Collectors.toList());

        scheduleCreation.getDailyScheduleSpotCreations()
                .stream()
                .map(dailyScheduleSpotCreation -> getDailyScheduleSpot(schedule, dailyScheduleSpotCreation))
                .collect(Collectors.toList());

        return schedule;
    }

    private DailyScheduleSpot getDailyScheduleSpot(Schedule schedule, DailyScheduleSpotCreation dailyScheduleSpotCreation) {
        return new DailyScheduleSpot(
                schedule,
                dailyScheduleSpotCreation.getSpotId(),
                dailyScheduleSpotCreation.getDate(),
                dailyScheduleSpotCreation.getOrder()
        );
    }


    public ScheduleSimple convertScheduleSimple(Schedule schedule) {
        return ScheduleSimple.builder()
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

    public ScheduleDetail convertScheduleDetail(Schedule schedule, List<Spot> spots, List<String> imageUrls) {
        return ScheduleDetail.builder()
                .id(schedule.getId())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .title(schedule.getTitle())
                .themas(getThema(schedule))
                .spotSimpleList(getSpotSimple(spots))
                .memberImageUrls(imageUrls)
                .build();

        //TODO : memberImageUrl 추가
    }

    private List<SpotSimple> getSpotSimple(List<Spot> spots) {
        return spots.stream()
                .map(spotConverter::convertSpotSimple)
                .collect(Collectors.toList());
    }
}
