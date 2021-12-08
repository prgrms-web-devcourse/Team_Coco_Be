package com.cocodan.triplan.schedule.domain;

import com.cocodan.triplan.schedule.domain.vo.Thema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleThema {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", referencedColumnName = "id")
    private Schedule schedule;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "tag")
    private Thema thema;

    @Builder
    public ScheduleThema(Schedule schedule, Thema thema) {
        this.schedule = schedule;
        this.thema = thema;
        this.schedule.getScheduleThemas().add(this);
    }

    public Long getId() {
        return id;
    }

    public Thema getThema() {
        return thema;
    }
}
