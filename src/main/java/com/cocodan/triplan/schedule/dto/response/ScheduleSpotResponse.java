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

    private Long id;

    private int dateOrder;

    private int spotOrder;

    private Long spotId;

    private String placeName;

    private String addressName;

    private String roadAddressName;

    private String phone;

    private Position position;

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
