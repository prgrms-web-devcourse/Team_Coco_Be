package com.cocodan.triplan.schedule.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DailyScheduleSpotCreationRequest {

    private Long spotId;

    private String addressName;

    private String roadAddressName;

    private String phone;

    private String placeName;

    private Position position;

    private LocalDate date;

    private int order;

}
