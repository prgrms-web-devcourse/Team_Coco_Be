package com.cocodan.triplan.schedule.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VotingContentMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voting_content_id", referencedColumnName = "id")
    private VotingContent votingContent;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Builder
    public VotingContentMember(VotingContent votingContent, Long memberId) {
        this.votingContent = votingContent;
        this.memberId = memberId;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }
}
