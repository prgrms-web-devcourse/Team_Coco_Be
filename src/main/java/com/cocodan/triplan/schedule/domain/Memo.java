package com.cocodan.triplan.schedule.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Memo {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", referencedColumnName = "id")
    private Schedule schedule;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Builder
    public Memo(Schedule schedule, String content, Long memberId) {
        this.schedule = schedule;
        this.content = content;
        this.memberId = memberId;
        this.schedule.getMemos().add(this);
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Long getMemberId() {
        return memberId;
    }
    public void modifyContent(String content) {
        this.content = content;
    }
}
