package com.cocodan.triplan.converter;

import com.cocodan.triplan.schedule.domain.DailyScheduleSpot;
import com.cocodan.triplan.schedule.domain.Schedule;
import com.cocodan.triplan.schedule.domain.ScheduleThema;
import com.cocodan.triplan.schedule.domain.vo.Thema;
import com.cocodan.triplan.schedule.dto.request.ScheduleCreation;
import com.cocodan.triplan.schedule.dto.response.ScheduleDetail;
import com.cocodan.triplan.schedule.dto.response.ScheduleSimple;
import com.cocodan.triplan.spot.dto.response.SpotSimple;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScheduleConverter {
    public Schedule convertSchedule(ScheduleCreation scheduleCreation) {
        Schedule schedule = Schedule.builder()
                .title(scheduleCreation.getTitle())
                .startDate(scheduleCreation.getStartDate())
                .endDate(scheduleCreation.getEndDate())
                .build();

        scheduleCreation.getThemas()
                .stream()
                .map(Thema::valueOf)
                .map(thema -> new ScheduleThema(schedule, thema))
                .collect(Collectors.toList());

        scheduleCreation.getDailyScheduleSpotCreations()
                .stream()
                .map(dailyScheduleSpotCreation ->
                        new DailyScheduleSpot(
                                schedule,
                                dailyScheduleSpotCreation.getSpotId(),
                                dailyScheduleSpotCreation.getDate(),
                                dailyScheduleSpotCreation.getOrder()
                        )
                )
                .collect(Collectors.toList());

        return schedule;
    }


    public ScheduleSimple convertScheduleSimple(Schedule schedule) {
        return ScheduleSimple.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .themas(
                        schedule.getScheduleThemas()
                                .stream()
                                .map(ScheduleThema::getThema)
                                .collect(Collectors.toList())
                )
                .build();
    }

    public ScheduleDetail convertScheduleDetail(Schedule schedule, List<List<SpotSimple>> simples) {
        return ScheduleDetail.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .themas(
                        schedule.getScheduleThemas().stream()
                                .map(ScheduleThema::getThema)
                                .collect(Collectors.toList())
                )
                .spotSimpleList(simples)
                .memberImageUrls(null)
                .build();
    }
}
