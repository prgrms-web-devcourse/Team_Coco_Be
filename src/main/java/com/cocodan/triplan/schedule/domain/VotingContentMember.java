package com.cocodan.triplan.schedule.domain;

import com.cocodan.triplan.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VotingContentMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voting_content_id", referencedColumnName = "id")
    private VotingContent votingContent;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Builder
    private VotingContentMember(VotingContent votingContent, Long memberId) {
        checkNotNull(votingContent, "VotingContent is required");
        checkMemberId(memberId);

        this.votingContent = votingContent;
        this.memberId = memberId;
    }

    private void checkMemberId(Long memberId) {
        checkNotNull(memberId, "MemberId is required");
        checkArgument(memberId > 0, "MemberId must be positive, you supplied %d", memberId);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }
}
