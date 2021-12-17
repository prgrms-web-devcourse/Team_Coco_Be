package com.cocodan.triplan.schedule.domain.vo;

import lombok.Getter;

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
}
