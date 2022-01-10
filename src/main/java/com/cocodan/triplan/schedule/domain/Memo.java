package com.cocodan.triplan.schedule.domain;

import com.cocodan.triplan.common.BaseEntity;
import com.google.common.collect.Range;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Memo extends BaseEntity {

    public static final int TITLE_MIN_LENGTH = 1;
    public static final int TITLE_MAX_LENGTH = 16;

    public static final int CONTENT_MIN_LENGTH = 1;
    public static final int CONTENT_MAX_LENGTH = 255;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", referencedColumnName = "id")
    private Schedule schedule;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Builder
    private Memo(Schedule schedule, String title, String content, Long memberId) {
        checkArgument(schedule != null, "Schedule is required");
        checkTitle(title);
        checkContent(content);
        checkMemberId(memberId);

        this.schedule = schedule;
        this.title = title;
        this.content = content;
        this.memberId = memberId;
        this.schedule.getMemos().add(this);
    }

    private void checkMemberId(Long memberId) {
        checkArgument(memberId != null, "MemberId is required");
        checkArgument(memberId > 0, "MemberId must be positive");
    }

    public void checkTitle(String title) {
        checkArgument(title != null, "Title is required");
        checkArgument(
                Range.closed(TITLE_MIN_LENGTH, TITLE_MAX_LENGTH).contains(title.length()),
                "Title length is invalid"
        );
    }

    private void checkContent(String content) {
        checkArgument(content != null, "Content is required");
        checkArgument(
                Range.closed(CONTENT_MIN_LENGTH, CONTENT_MAX_LENGTH).contains(content.length()),
                "Content length is invalid"
        );
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void modify(String title, String content) {
        checkTitle(title);
        checkContent(content);
        this.title = title;
        this.content = content;
    }
}
