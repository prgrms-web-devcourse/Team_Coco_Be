package com.cocodan.triplan.spot.domain.vo;

public enum City {
    SEOUL("서울"),
    BUSAN("부산"),
    INCHEON("인천"),
    JEJU("제주");

    private final String city;

    City(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }
}
