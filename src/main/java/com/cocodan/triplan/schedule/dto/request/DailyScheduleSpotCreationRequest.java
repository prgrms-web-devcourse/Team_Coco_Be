package com.cocodan.triplan.schedule.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DailyScheduleSpotCreationRequest {

    private Long id;

    private String addressName;

    private String roadAddressName;

    private String phone;

    private String placeName;

    private double x;

    private double y;

    private LocalDate date;

    private int order;

}
