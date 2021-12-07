package com.cocodan.triplan.converter;

import com.cocodan.triplan.spot.domain.Spot;
import com.cocodan.triplan.spot.dto.response.SpotSimple;
import org.springframework.stereotype.Component;

@Component
public class SpotConverter {

    public SpotSimple convertSpotSimple(Spot spot) {
        return SpotSimple.builder()
                .id(spot.getId())
                .name(spot.getName())
                .latitude(spot.getLatitude())
                .longitude(spot.getLongitude())
                .build();
    }
}
