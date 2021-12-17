package com.cocodan.triplan.post.schedule.domain;

import com.cocodan.triplan.common.BaseEntity;
import com.cocodan.triplan.member.domain.Member;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "schedule_post_like")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// TODO: 2021.12.07 Teru - 더 직관적인 네이밍 고민
public class Like extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_post_id", referencedColumnName = "id")
    private SchedulePost schedulePost;

    public Like(Member member, SchedulePost schedulePost) {
        this.member = member;
        this.schedulePost = schedulePost;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public SchedulePost getSchedulePost() {
        return schedulePost;
    }
}
