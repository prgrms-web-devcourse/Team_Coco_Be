package com.cocodan.triplan.schedule.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VotingContent {

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
    public VotingContent(String content, Voting voting) {
        this.content = content;
        this.voting = voting;
        this.voting.getVotingContents().add(this);
    }

    public void vote(Long memberId) {
        for (VotingContentMember votingContentMember : votingContentMembers) {
            if (votingContentMember.getMemberId().equals(memberId)) {
                return ;
            }
        }

        votingContentMembers.add(createVotingMember(memberId));
    }

    private VotingContentMember createVotingMember(Long memberId) {
        return VotingContentMember.builder()
                .votingContent(this)
                .memberId(memberId)
                .build();
    }

    public void cancel(Long memberId) {
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
