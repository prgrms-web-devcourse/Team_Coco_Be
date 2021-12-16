package com.cocodan.triplan.spot.dto.response;

import com.cocodan.triplan.schedule.dto.request.Position;
import com.cocodan.triplan.spot.domain.Spot;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SpotResponse {

    private Long id;

    private String placeName;

    private String addressName;

    private String roadAddressName;

    private String phone;

    private Position position;

    public static SpotResponse from(Spot spot) {
        return SpotResponse.builder()
                .id(spot.getId())
                .placeName(spot.getPlaceName())
                .addressName(spot.getAddressName())
                .roadAddressName(spot.getRoadAddressName())
                .phone(spot.getPhone())
                .position(new Position(spot.getLatitude(), spot.getLongitude()))
                .build();
    }
}
