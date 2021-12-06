package com.cocodan.triplan.post.schedule.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "schedule_post_comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SchedulePostComment {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "schedule_post_id", referencedColumnName = "id")
    private SchedulePost schedulePost;

    @Column(name = "content", nullable = false)
    private String content;

    @Builder
    public SchedulePostComment(Long memberId, SchedulePost schedulePost, String content) {
        this.memberId = memberId;
        this.schedulePost = schedulePost;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public SchedulePost getSchedulePost() {
        return schedulePost;
    }

    public String getContent() {
        return content;
    }
}
