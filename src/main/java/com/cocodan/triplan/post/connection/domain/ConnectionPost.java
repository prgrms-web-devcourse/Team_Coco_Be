package com.cocodan.triplan.post.connection.domain;

import com.cocodan.triplan.schedule.domain.DailyScheduleSpot;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "connection_posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConnectionPost {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: 2021.12.06 Teru - 회원 엔티티 구현되면 연결하기

    @OneToOne
    @JoinColumn(name = "daily_schedule_spot_id", referencedColumnName = "id")
    private DailyScheduleSpot dailyScheduleSpot;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    // TODO: 2021.12.05 Teru - 회원의 성별과 연령 Enum이 구현되면 연결하기
    // @Column(name = "gender_condition")
    // @Enumerated(value = EnumType.STRING)
    // private Gender genderCondition;
    //
    // @Column(name = "age_condition")
    // @Enumerated(value = EnumType.STRING)
    // private Age ageCondition;

    @Builder
    public ConnectionPost(DailyScheduleSpot dailyScheduleSpot, String title, String content) {
        this.dailyScheduleSpot = dailyScheduleSpot;
        this.title = title;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public DailyScheduleSpot getDailyScheduleSpot() {
        return dailyScheduleSpot;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
