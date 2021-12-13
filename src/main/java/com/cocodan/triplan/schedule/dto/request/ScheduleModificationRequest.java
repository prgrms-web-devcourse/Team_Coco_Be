package com.cocodan.triplan.schedule.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleModificationRequest {

    private String title;

    private List<String> themeList;

    private List<DailyScheduleSpotCreationRequest> dailyScheduleSpotCreationRequests;

}
