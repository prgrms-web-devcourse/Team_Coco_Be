package com.cocodan.triplan.schedule.domain;

import com.cocodan.triplan.common.BaseEntity;
import com.google.common.collect.Range;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

import static com.cocodan.triplan.schedule.domain.Schedule.*;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Checklist extends BaseEntity {

    public static final int MIN_LENGTH = 1;
    public static final int MAX_LENGTH = 16;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", referencedColumnName = "id")
    private Schedule schedule;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "checked", columnDefinition = "boolean default false")
    private boolean checked;

    @Column(name = "day")
    private int day;

    @Builder
    private Checklist(Schedule schedule, String title, int day) {
        checkNotNull(schedule, "Schedule is required");
        checkNotNull(title, "Title is required");
        checkArgument(Range.closed(MIN_LENGTH, MAX_LENGTH).contains(title.length()),"Title is invalid");
        checkArgument(Range.closed(0, DAY_MAX).contains(day), "Day is invalid");
        this.schedule = schedule;
        this.title = title;
        this.day = day;
        schedule.getChecklists().add(this);
    }

    public void check(boolean flag) {
        checked = flag;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isChecked() {
        return checked;
    }

    public int getDay() {
        return day;
    }
}
