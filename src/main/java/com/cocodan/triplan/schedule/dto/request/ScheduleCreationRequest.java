package com.cocodan.triplan.schedule.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleCreationRequest {

    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<String> themas;
    private List<DailyScheduleSpotCreationRequest> dailyScheduleSpotCreationRequests;
}
