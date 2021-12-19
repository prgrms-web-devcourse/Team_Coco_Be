package com.cocodan.triplan.schedule.domain;

import com.cocodan.triplan.common.BaseEntity;
import com.google.common.collect.Range;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VotingContent extends BaseEntity {

    public static final int MIN_LENGTH = 1;
    public static final int MAX_LENGTH = 16;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voting_id", referencedColumnName = "id")
    private Voting voting;

    @OneToMany(mappedBy = "votingContent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VotingContentMember> votingContentMembers = new ArrayList<>();

    @Builder
    private VotingContent(Voting voting, String content) {
        checkArgument(voting != null, "Voting is required");
        checkContent(content);

        this.voting = voting;
        this.content = content;
        this.voting.getVotingContents().add(this);
    }

    private void checkContent(String content) {
        checkArgument(content != null, "Content is required");
        checkArgument(Range.closed(MIN_LENGTH, MAX_LENGTH).contains(content.length()));
    }

    public void vote(Long memberId) {
        checkMemberId(memberId);

        for (VotingContentMember votingContentMember : votingContentMembers) {
            if (votingContentMember.getMemberId().equals(memberId)) {
                return;
            }
        }

        votingContentMembers.add(createVotingMember(memberId));
    }

    private void checkMemberId(Long memberId) {
        checkArgument(memberId != null, "memberId is required");
        checkArgument(memberId > 0, "memberId must be positive, you supplied %d", memberId);
    }

    private VotingContentMember createVotingMember(Long memberId) {
        checkMemberId(memberId);

        return VotingContentMember.builder()
                .votingContent(this)
                .memberId(memberId)
                .build();
    }

    public void cancel(Long memberId) {
        checkMemberId(memberId);

        votingContentMembers.removeIf(votingContentMember -> votingContentMember.getMemberId().equals(memberId));
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public int getNumOfParticipants() {
        return votingContentMembers.size();
    }

    public List<VotingContentMember> getVotingContentMembers() {
        return votingContentMembers;
    }
}
