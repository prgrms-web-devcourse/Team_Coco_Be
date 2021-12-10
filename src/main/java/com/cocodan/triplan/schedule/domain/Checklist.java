package com.cocodan.triplan.schedule.domain;

import com.cocodan.triplan.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Checklist extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", referencedColumnName = "id")
    private Schedule schedule;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "checked", columnDefinition = "boolean default false")
    private boolean checked;

    @Column(name = "trip_date")
    private LocalDate date;

    @Builder
    public Checklist(Schedule schedule, String content, LocalDate date) {
        this.schedule = schedule;
        this.content = content;
        this.date = date;
        schedule.getChecklists().add(this);
    }

    public void check(boolean flag) {
        checked = flag;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public boolean isChecked() {
        return checked;
    }

    public LocalDate getDate() {
        return date;
    }
}
