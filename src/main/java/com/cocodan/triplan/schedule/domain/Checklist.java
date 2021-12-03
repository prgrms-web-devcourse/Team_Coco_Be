package com.cocodan.triplan.schedule.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.StringJoiner;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Checklist {

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

    @Column(name = "day", columnDefinition = "tinyint default 0")
    private int day;

    @Builder
    public Checklist(Schedule schedule, String content, int day) {
        this.schedule = schedule;
        this.content = content;
        this.day = day;
    }

    public void check(){
        checked = !checked;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Schedule getSchedule() {
        return schedule;
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

    public int getDay() {
        return day;
    }
}
