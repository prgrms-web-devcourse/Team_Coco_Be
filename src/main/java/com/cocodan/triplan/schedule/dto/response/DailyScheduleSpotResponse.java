package com.cocodan.triplan.schedule.dto.response;

import com.cocodan.triplan.schedule.domain.DailyScheduleSpot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
public class DailyScheduleSpotResponse {

    private Long spotId;

    private int date;

    private int order;

    @Builder
    private DailyScheduleSpotResponse(Long spotId, int date, int order) {
        this.spotId = spotId;
        this.date = date;
        this.order = order;
    }

    public static DailyScheduleSpotResponse from(DailyScheduleSpot dailyScheduleSpot) {
        return DailyScheduleSpotResponse.builder()
                .spotId(dailyScheduleSpot.getSpotId())
                .date(dailyScheduleSpot.getDate())
                .order(dailyScheduleSpot.getOrder())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyScheduleSpotResponse that = (DailyScheduleSpotResponse) o;
        return spotId.equals(that.spotId) && date == that.date && order == that.order;
    }
}
