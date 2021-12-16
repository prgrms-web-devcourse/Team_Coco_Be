package com.cocodan.triplan.post.schedule.domain;

import com.cocodan.triplan.common.BaseEntity;
import com.cocodan.triplan.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

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
@Table(name = "schedule_post_comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SchedulePostComment extends BaseEntity {

    public static final int COMMENT_MIN_LENGTH = 1;
    public static final int COMMENT_MAX_LENGTH = 255;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_post", referencedColumnName = "id")
    private SchedulePost schedulePost;

    @Column(name = "content", nullable = false)
    @Length(min = COMMENT_MIN_LENGTH, max = COMMENT_MAX_LENGTH)
    private String content;

    @Builder
    public SchedulePostComment(Member member, SchedulePost schedulePost, String content) {
        this.member = member;
        this.schedulePost = schedulePost;
        this.content = content;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
