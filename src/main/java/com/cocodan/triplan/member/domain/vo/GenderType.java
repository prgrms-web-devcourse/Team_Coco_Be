package com.cocodan.triplan.member.domain.vo;

import java.util.Arrays;

public enum GenderType {
    MALE("남성"),
    FEMALE("여성"),
    DEFAULT("미입력");

    private final String typeStr;

    GenderType(String typeStr) {
        this.typeStr = typeStr;
    }

    public static GenderType of(String genderType) {
        return Arrays.stream(GenderType.values())
                .filter(content -> content.typeStr.equals(genderType))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("성별 정보를 확인하세요"));
    }

    public String getTypeStr() {
        return typeStr;
    }
}
