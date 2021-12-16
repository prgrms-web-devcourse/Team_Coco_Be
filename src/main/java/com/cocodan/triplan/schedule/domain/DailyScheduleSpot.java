package com.cocodan.triplan.schedule.domain;

import com.cocodan.triplan.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyScheduleSpot extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", referencedColumnName = "id")
    private Schedule schedule;

    @Column(name = "spotId", nullable = false)
    private Long spotId;

    @Column(name = "trip_date", nullable = false)
    private int date;

    @Column(name = "trip_order", nullable = false)
    private int order;

    @Builder
    public DailyScheduleSpot(Schedule schedule, Long spotId, int date, int order) {
        this.schedule = schedule;
        this.spotId = spotId;
        this.date = date;
        this.order = order;
        schedule.getDailyScheduleSpots().add(this);
    }

    public Long getId() {
        return id;
    }

    public Long getSpotId() {
        return spotId;
    }

    public int getDate() {
        return date;
    }

    public int getOrder() {
        return order;
    }
}
