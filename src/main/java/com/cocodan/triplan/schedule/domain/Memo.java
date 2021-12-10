package com.cocodan.triplan.schedule.domain;

import com.cocodan.triplan.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Memo extends BaseEntity {

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
    public Memo(Schedule schedule, String title, String content, Long memberId) {
        this.schedule = schedule;
        this.title = title;
        this.content = content;
        this.memberId = memberId;
        this.schedule.getMemos().add(this);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void modify(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
