package com.cocodan.triplan.post.schedule.domain;

import com.cocodan.triplan.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "schedule_posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SchedulePost extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;
    @Column(name = "schedule_id", nullable = false)
    private Long scheduleId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "views", nullable = false)
    private Long views;

    @Column(name = "liked", nullable = false)
    private Long liked;

    @Builder
    public SchedulePost(Long memberId, Long scheduleId, String title, String content, Long views, Long liked) {
        this.memberId = memberId;
        this.scheduleId = scheduleId;
        this.title = title;
        this.content = content;
        this.views = views;
        this.liked = liked;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Long getViews() {
        return views;
    }

    public Long getLiked() {
        return liked;
    }
}
