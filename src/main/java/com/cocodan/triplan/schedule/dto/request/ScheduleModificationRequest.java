package com.cocodan.triplan.schedule.dto.request;

import com.cocodan.triplan.schedule.domain.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleModificationRequest {

    @Length(min = Schedule.TITLE_MIN_LENGTH, max = Schedule.TITLE_MAX_LENGTH)
    private String title;

    @Size(max = Schedule.THEME_MAX_COUNT)
    private List<String> themes;

    @NotNull
    private List<DailyScheduleSpotCreationRequest> dailyScheduleSpotCreationRequests;
}
