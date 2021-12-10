package com.cocodan.triplan.converter;

import com.cocodan.triplan.schedule.dto.request.DailyScheduleSpotCreationRequest;
import com.cocodan.triplan.spot.domain.Spot;
import com.cocodan.triplan.spot.dto.response.SpotSimple;
import org.springframework.stereotype.Component;

@Component
public class SpotConverter {

    public SpotSimple convertSpotSimple(Spot spot) {
        return SpotSimple.builder()
                .id(spot.getId())
                .name(spot.getPlaceName())
                .latitude(spot.getLatitude())
                .longitude(spot.getLongitude())
                .build();
    }

    public Spot convertSpot(DailyScheduleSpotCreationRequest dailyScheduleSpotCreationRequest) {
        return Spot.builder()
                .id(dailyScheduleSpotCreationRequest.getSpotId())
                .placeName(dailyScheduleSpotCreationRequest.getPlaceName())
                .phone(dailyScheduleSpotCreationRequest.getPhone())
                .addressName(dailyScheduleSpotCreationRequest.getAddressName())
                .roadAddressName(dailyScheduleSpotCreationRequest.getRoadAddressName())
                .latitude(dailyScheduleSpotCreationRequest.getPosition().getLat())
                .longitude((dailyScheduleSpotCreationRequest.getPosition().getLng()))
                .build();
    }
}
