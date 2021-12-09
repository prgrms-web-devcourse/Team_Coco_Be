package com.cocodan.triplan.post.schedule.domain;

import com.cocodan.triplan.spot.domain.vo.City;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "schedule_post_cities")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SchedulePostCity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "schedule_post", referencedColumnName = "id")
    private SchedulePost schedulePost;

    @Column(name = "city", nullable = false)
    private City city;

    public SchedulePostCity(SchedulePost schedulePost, City city) {
        this.schedulePost = schedulePost;
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public SchedulePost getSchedulePost() {
        return schedulePost;
    }

    public City getCity() {
        return city;
    }
}
