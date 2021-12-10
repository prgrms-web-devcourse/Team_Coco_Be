package com.cocodan.triplan.post.schedule.vo;

import java.util.Arrays;

public enum SchedulePostSortingRule {
    VIEWS("조회순"), // 조회수 내림차순
    LIKE("좋아요순"), // 좋아요 수 내림차순
    COMMENTS("댓글순"), // 코멘트 수 내림차순
    RECENT("최신순");

   private final String rule;

    SchedulePostSortingRule(String rule) {
        this.rule = rule;
    }

    public static SchedulePostSortingRule of(String rule) {
        return Arrays.stream(values())
                .filter(iter -> iter.isEqualTo(rule))
                .findAny()
                .orElseThrow(
                        () -> new RuntimeException("Invalid post sorting rule")
                );
    }

    private String getRule() {
        return rule;
    }

    private boolean isEqualTo(String rule) {
        return this.rule.equals(rule);
    }
}
