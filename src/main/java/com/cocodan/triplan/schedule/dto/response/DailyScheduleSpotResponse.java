package com.cocodan.triplan.schedule.dto.response;

import com.cocodan.triplan.schedule.domain.DailyScheduleSpot;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class DailyScheduleSpotResponse {

    private final Long spotId;

    private final int date;

    private final int order;

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

    @Override
    public int hashCode() {
        int result = spotId.hashCode();
        result = 31 * result + date;
        result = 31 * result + order;
        return result;
    }
}
