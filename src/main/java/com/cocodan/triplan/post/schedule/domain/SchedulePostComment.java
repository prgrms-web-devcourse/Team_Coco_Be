package com.cocodan.triplan.post.schedule.domain;

import com.cocodan.triplan.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
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
@Table(name = "schedule_post_comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SchedulePostComment extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_post", referencedColumnName = "id")
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

    public void updateContent(String content) {
        this.content = content;
    }
}
