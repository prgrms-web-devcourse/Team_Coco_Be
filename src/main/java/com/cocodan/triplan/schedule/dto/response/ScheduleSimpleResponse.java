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

    private final List<String> themes;

    public static ScheduleSimpleResponse from(Schedule schedule) {
        return ScheduleSimpleResponse.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .themes(getThemes(schedule))
                .build();
    }

    public static List<String> getThemes(Schedule schedule) {
        return schedule.getScheduleThemes().stream()
                .map(ScheduleTheme::getTheme)
                .map(Theme::toString)
                .collect(Collectors.toList());
    }
}
