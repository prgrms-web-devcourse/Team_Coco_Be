package com.cocodan.triplan.schedule.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyScheduleSpot {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_schedule_id", referencedColumnName = "id")
    private DailySchedule dailySchedule;

    @Column(name = "spotId", nullable = false)
    private Long spotId;

    @Builder
    public DailyScheduleSpot(DailySchedule dailySchedule, Long spotId) {
        this.dailySchedule = dailySchedule;
        this.spotId = spotId;
        this.dailySchedule.getDailyScheduleSpots().add(this);
    }

    public Long getId() {
        return id;
    }

    public Long getSpotId() {
        return spotId;
    }
}
