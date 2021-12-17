package com.cocodan.triplan.schedule.dto.response;

import com.cocodan.triplan.schedule.domain.DailyScheduleSpot;
import com.cocodan.triplan.schedule.dto.request.Position;
import com.cocodan.triplan.spot.domain.Spot;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class ScheduleSpotResponse {

    private final Long id;

    private final int dateOrder;

    private final int spotOrder;

    private final Long spotId;

    private final String placeName;

    private final String addressName;

    private final String roadAddressName;

    private final String phone;

    private final Position position;

    public static ScheduleSpotResponse of(Spot spot, DailyScheduleSpot dailyScheduleSpot) {
        return ScheduleSpotResponse.builder()
                .id(dailyScheduleSpot.getId())
                .dateOrder(dailyScheduleSpot.getDateOrder())
                .spotOrder(dailyScheduleSpot.getSpotOrder())
                .spotId(spot.getId())
                .placeName(spot.getPlaceName())
                .addressName(spot.getAddressName())
                .roadAddressName(spot.getRoadAddressName())
                .phone(spot.getPhone())
                .position(new Position(spot.getLatitude(), spot.getLongitude()))
                .build();
    }
}
