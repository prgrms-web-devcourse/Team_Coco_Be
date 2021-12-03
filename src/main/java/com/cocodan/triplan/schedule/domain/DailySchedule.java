package com.cocodan.triplan.schedule.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Table(name = "daily_schedule")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailySchedule {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @OneToMany(mappedBy = "dailySchedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DailyScheduleSpot> dailyScheduleSpots = new ArrayList<>();

    @Builder
    public DailySchedule(LocalDate date, Schedule schedule) {
        this.date = date;
        this.schedule = schedule;
        this.schedule.getDailySchedules().add(this);
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<DailyScheduleSpot> getDailyScheduleSpots() {
        return dailyScheduleSpots;
    }
}