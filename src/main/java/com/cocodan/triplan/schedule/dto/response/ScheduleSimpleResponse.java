package com.cocodan.triplan.schedule.dto.response;

import com.cocodan.triplan.schedule.domain.Schedule;
import com.cocodan.triplan.schedule.domain.ScheduleTheme;
import com.cocodan.triplan.schedule.domain.vo.Theme;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ScheduleSimpleResponse {

    private final Long id;

    private final String title;

    private final LocalDate startDate;

    private final LocalDate endDate;

    private final List<Theme> thema;

    public static ScheduleSimpleResponse from(Schedule schedule) {
        return ScheduleSimpleResponse.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .thema(getThemes(schedule))
                .build();
    }

    public static List<Theme> getThemes(Schedule schedule) {
        return schedule.getScheduleThema().stream()
                .map(ScheduleTheme::getTheme)
                .collect(Collectors.toList());
    }
}
