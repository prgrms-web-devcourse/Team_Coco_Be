package com.cocodan.triplan.spot.domain.vo;

import java.util.Arrays;

public enum City {
    SEOUL("서울"),
    BUSAN("부산"),
    INCHEON("인천"),
    JEJU("제주"),
    ALL("전체"), // 검색 전용
    DAEGU("대구"),
    DAEJEON("대전"),
    GWANGJU("광주"),
    SUWON("수원"),
    ULSAN("울산"),
    HWASEONG("화성"),
    JEONJU("전주"),
    CHEONAN("천안"),
    GIMHAE("김해"),
    PYEONGTAEK("평택"),
    POHANG("포항"),
    WONJU("원주"),
    SEJONG("세종"),
    JINJU("진주"),
    ASAN("아산"),
    CHUNCHEON("춘천"),
    YEOSU("여수"),
    SUNCHEON("순천"),
    GYEONGJU("경주"),
    GEOJE("거제"),
    MOKPO("목포"),
    GANGNEUNG("강릉"),
    YANGJU("양주"),
    ANDONG("안동"),
    NONSAN("논산"),
    DONGHAE("동해"),
    SOKCHO("속초"),
    SAMCHEOK("삼척"),
    GAPYEONG("가평"),
    ULLEUNG("울릉");


    private final String city;

    City(String city) {
        this.city = city;
    }

    public static City from(String city) {
        return Arrays.stream(values())
                .filter(iter -> iter.isEqualTo(city))
                .findAny()
                .orElse(City.ALL);
    }

    private boolean isEqualTo(String city) {
        return this.city.equals(city);
    }

    @Override
    public String toString() {
        return city;
    }
}
