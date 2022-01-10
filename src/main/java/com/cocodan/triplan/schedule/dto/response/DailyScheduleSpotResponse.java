package com.cocodan.triplan.schedule.dto.response;

import com.cocodan.triplan.schedule.domain.DailyScheduleSpot;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class DailyScheduleSpotResponse {

    private Long spotId;

    private int dateOrder;

    private int spotOrder;

    private String placeName;

    public static DailyScheduleSpotResponse from(DailyScheduleSpot dailyScheduleSpot) {
        return DailyScheduleSpotResponse.builder()
                .spotId(dailyScheduleSpot.getSpotId())
                .dateOrder(dailyScheduleSpot.getDateOrder())
                .spotOrder(dailyScheduleSpot.getSpotOrder())
                .placeName(dailyScheduleSpot.getPlaceName())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyScheduleSpotResponse that = (DailyScheduleSpotResponse) o;
        return spotId.equals(that.spotId) && dateOrder == that.dateOrder && spotOrder == that.spotOrder;
    }

    @Override
    public int hashCode() {
        int result = spotId.hashCode();
        result = 31 * result + dateOrder;
        result = 31 * result + spotOrder;
        return result;
    }
}
