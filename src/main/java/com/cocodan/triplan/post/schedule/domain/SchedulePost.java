package com.cocodan.triplan.post.schedule.domain;

import com.cocodan.triplan.common.BaseEntity;
import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.schedule.domain.Schedule;
import com.cocodan.triplan.spot.domain.vo.City;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Getter
@Table(name = "schedule_posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SchedulePost extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member", referencedColumnName = "id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule", referencedColumnName = "id")
    private Schedule schedule;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "text")
    private String content;

    @Column(name = "views", nullable = false)
    private Long views;

    @Column(name = "liked", nullable = false)
    private Long liked;

    @Column(name = "city", nullable = false)
    @Enumerated(EnumType.STRING)
    private City city;

    @Builder
    public SchedulePost(Member member, Schedule schedule, String title, String content, Long views, Long liked, City city) {
        this.member = member;
        this.schedule = schedule;
        this.title = title;
        this.content = content;
        this.views = views;
        this.liked = liked;
        this.city = city;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateCity(City city) {
        this.city = city;
    }
}
