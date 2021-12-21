package com.cocodan.triplan.post.schedule.vo;

import com.cocodan.triplan.post.schedule.domain.SchedulePost;
import com.querydsl.jpa.impl.JPAQuery;

import java.util.Arrays;

import static com.cocodan.triplan.post.schedule.domain.QSchedulePost.schedulePost;

public enum SchedulePostSortingRule {
    VIEWS("조회순") {
        public void sort(JPAQuery<SchedulePost> query) {
            query.orderBy(schedulePost.views.desc());
        }
    },
    LIKE("좋아요순") {
        public void sort(JPAQuery<SchedulePost> query) {
            query.orderBy(schedulePost.liked.desc());
        }
    },
    COMMENTS("댓글순") {
        public void sort(JPAQuery<SchedulePost> query) {
//            query.orderBy(schedulePost.views.desc());
        }
    },
    RECENT("최신순") {
        public void sort(JPAQuery<SchedulePost> query) {
            query.orderBy(schedulePost.createdDate.desc());
        }
    };

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

    public abstract void sort(JPAQuery<SchedulePost> query);
}
