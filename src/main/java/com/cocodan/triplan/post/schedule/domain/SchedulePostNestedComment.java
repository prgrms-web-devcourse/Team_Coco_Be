package com.cocodan.triplan.post.schedule.domain;

import com.cocodan.triplan.common.BaseEntity;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostNestedCommentResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
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
@Getter
@Table(name = "schedule_post_nested_comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SchedulePostNestedComment extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment", referencedColumnName = "id")
    private SchedulePostComment comment; // parent comment

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "content", nullable = false)
    private String content;

    @Builder
    public SchedulePostNestedComment(SchedulePostComment comment, Long memberId, String content) {
        this.comment = comment;
        this.memberId = memberId;
        this.content = content;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
