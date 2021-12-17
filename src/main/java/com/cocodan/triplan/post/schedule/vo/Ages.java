package com.cocodan.triplan.post.schedule.vo;

import java.time.LocalDate;

public enum Ages {
    TEENS("10대"),
    TWENTIES("20대"),
    THIRTIES("30대"),
    FORTIES("40대"),
    FIFTIES("50대"),
    SIXTIES("60대"),
    ELDER("70대 이상");

    private final String ages;

    Ages(String ages) {
        this.ages = ages;
    }

    public static Ages from(String birth) {
        int thisYear = LocalDate.now().getYear();
        int birthYear = Integer.parseInt(birth.substring(0, 4));
        int age = thisYear - birthYear + 1; // 잘못된 birth는 없다고 가정

        switch (age / 10) {
            case 0: case 1:
                return Ages.TEENS;
            case 2:
                return Ages.TWENTIES;
            case 3:
                return Ages.THIRTIES;
            case 4:
                return Ages.FORTIES;
            case 5:
                return Ages.FIFTIES;
            case 6:
                return Ages.SIXTIES;
            default:
                return Ages.ELDER;
        }
    }
}
