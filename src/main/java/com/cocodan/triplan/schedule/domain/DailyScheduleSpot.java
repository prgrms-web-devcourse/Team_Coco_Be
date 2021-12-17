package com.cocodan.triplan.schedule.domain;

import com.cocodan.triplan.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @Column(name = "place_name", nullable = false)
    private String placeName;

    @Column(name = "date_order", nullable = false)
    private int dateOrder;

    @Column(name = "spot_order", nullable = false)
    private int spotOrder;

    @Builder
    private DailyScheduleSpot(Schedule schedule, Long spotId, String placeName, int dateOrder, int spotOrder) {
        this.schedule = schedule;
        this.spotId = spotId;
        this.placeName = placeName;
        this.dateOrder = dateOrder;
        this.spotOrder = spotOrder;
        schedule.getDailyScheduleSpots().add(this);
    }

    public Long getId() {
        return id;
    }

    public Long getSpotId() {
        return spotId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public int getDateOrder() {
        return dateOrder;
    }

    public int getSpotOrder() {
        return spotOrder;
    }
}
