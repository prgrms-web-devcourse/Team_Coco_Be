package com.cocodan.triplan.schedule.domain;

import com.cocodan.triplan.schedule.domain.vo.Tag;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleTag {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", referencedColumnName = "id")
    private Schedule schedule;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "tag")
    private Tag tag;

    @Builder
    public ScheduleTag(Schedule schedule, Tag tag) {
        this.schedule = schedule;
        this.tag = tag;
        this.schedule.getScheduleTags().add(this);
    }

    public Long getId() {
        return id;
    }
}
