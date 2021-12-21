package com.cocodan.triplan.schedule.domain.vo;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Theme {

    ACTIVITY("ACTIVITY"),
    FOOD("FOOD"),
    ART("ART"),
    NATURE("NATURE"),
    HISTORY("HISTORY"),
    ALL("ALL"); // 검색 전용

    private String value;

    Theme(String value) {
        this.value = value;
    }

    public static Theme from(String theme) {
        return Arrays.stream(values())
                .filter(iter -> iter.isEqualTo(theme))
                .findAny()
                .orElseThrow(
                        () -> new RuntimeException("Invalid Theme")
                );
    }

    private boolean isEqualTo(String theme) {
        return this.value.equals(theme);
    }
}
