package com.cocodan.triplan.post.schedule.domain;

import lombok.AccessLevel;
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

    // TODO: 2021.12.05 TP-28 Teru - Connect To Member
    // @ManyToOne
    // @JoinColumn(name = "member_id", referencedColumnName = "id")
    // private Member member;

    @ManyToOne
    @JoinColumn(name = "schedule_post_id", referencedColumnName = "id")
    private SchedulePost schedulePost;

    @Column(name = "content", nullable = false)
    private String content;

    public SchedulePostComment(SchedulePost schedulePost, String content) {
        this.schedulePost = schedulePost;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public SchedulePost getSchedulePost() {
        return schedulePost;
    }

    public String getContent() {
        return content;
    }
}
