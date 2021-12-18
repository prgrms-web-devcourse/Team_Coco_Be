package com.cocodan.triplan.schedule.domain;

import com.cocodan.triplan.common.BaseEntity;
import com.cocodan.triplan.spot.domain.Spot;
import com.google.common.collect.Range;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.awt.font.NumericShaper;

import static com.cocodan.triplan.schedule.domain.Schedule.*;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

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
        checkNotNull(schedule, "Schedule is required");
        checkNotNull(spotId, "SpotId is required");
        checkNotNull(placeName, "PlaceName is required");
        checkArgument(spotId > 0 ,"SpotId must be greater than 0");
        checkArgument(Range.closed(Spot.PLACE_NAME_MIN_LENGTH, Spot.PLACE_NAME_MAX_LENGTH).contains(placeName.length()), "PlaceName is invalid");
        checkArgument(Range.closed(DAY_MIN, DAY_MAX).contains(dateOrder), "DateOrder is invalid");
        checkArgument((Range.closed(NUM_OF_SPOT_MIN, NUM_OF_SPOT_MAX).contains(spotOrder)),"SpotOrder is invalid");

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
