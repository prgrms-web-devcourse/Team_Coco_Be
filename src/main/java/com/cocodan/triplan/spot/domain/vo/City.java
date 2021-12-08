package com.cocodan.triplan.spot.domain.vo;

import java.util.Arrays;

public enum City {
    SEOUL("서울"),
    BUSAN("부산"),
    INCHEON("인천"),
    JEJU("제주");

    private final String city;

    City(String city) {
        this.city = city;
    }

    public static City of(String city) {
        return Arrays.stream(values())
                .filter(iter -> iter.isEqualTo(city))
                .findAny()
                .orElseThrow(
                        // TODO: 2021.12.07 Teru - Custom Exception 구현 후 수정
                        () -> new RuntimeException("No Such City exists in the list")
                );
    }

    private boolean isEqualTo(String city) {
        return this.city.equals(city);
    }
}
