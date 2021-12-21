package com.cocodan.triplan.schedule.dto.request;

import com.cocodan.triplan.schedule.domain.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleCreationRequest {

    @Length(min = Schedule.TITLE_MIN_LENGTH, max = Schedule.TITLE_MAX_LENGTH)
    private String title;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @Size(max = Schedule.THEME_MAX_COUNT)
    private List<String> themes;

    @NotNull
    private List<DailyScheduleSpotCreationRequest> dailyScheduleSpotCreationRequests;

    @NotNull
    private List<@Positive Long> idsOfFriends;
}
