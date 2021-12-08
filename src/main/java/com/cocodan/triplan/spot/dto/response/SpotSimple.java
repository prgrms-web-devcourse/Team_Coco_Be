package com.cocodan.triplan.spot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SpotSimple {

    private Long id;
    private String name;
    private double latitude;
    private double longitude;

    @Builder
    public SpotSimple(Long id, String name, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
