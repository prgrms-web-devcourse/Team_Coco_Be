package com.cocodan.triplan.schedule.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.StringJoiner;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Memo {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", referencedColumnName = "id")
    private Schedule schedule;

    @Column(name = "content", nullable = false)
    private String content;

    @Builder
    public Memo(Schedule schedule, String content) {
        this.schedule = schedule;
        this.content = content;
    }

    public Long getId() {
        return id;
    }
}
