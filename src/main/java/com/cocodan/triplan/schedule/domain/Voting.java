package com.cocodan.triplan.schedule.domain;

import com.cocodan.triplan.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Voting extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", referencedColumnName = "id")
    private Schedule schedule;

    @OneToMany(mappedBy = "voting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VotingContent> votingContents = new ArrayList<>();

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "multiple_flag")
    private boolean multipleFlag;

    @Builder
    public Voting(String title, Schedule schedule, Long memberId, boolean multipleFlag) {
        this.schedule = schedule;
        this.title = title;
        this.memberId = memberId;
        this.multipleFlag = multipleFlag;
        this.schedule.getVotingList().add(this);
    }

    public void vote(Map<Long, Boolean> votingMap, Long memberId) {
        votingMap.forEach((contentId, flag) -> {
            VotingContent votingContent = getVotingContent(contentId);
            voteByFlag(flag, memberId, votingContent);
        });

    }

    private VotingContent getVotingContent(Long contentId) {
        return votingContents.stream()
                .filter(votingContent -> votingContent.getId().equals(contentId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(""));
    }

    private void voteByFlag(boolean flag, Long memberId, VotingContent votingContent) {
        if (flag) {
            votingContent.vote(memberId);
            return;
        }

        votingContent.cancel(memberId);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<VotingContent> getVotingContents() {
        return votingContents;
    }

    public Long getMemberId() {
        return memberId;
    }

    public boolean isMultipleFlag() {
        return multipleFlag;
    }

    public int getNumOfTotalParticipants() {
         return (int) votingContents.stream()
                 .flatMap(votingContent -> votingContent.getVotingContentMembers().stream())
                 .map(VotingContentMember::getMemberId)
                 .distinct()
                 .count();
    }
}
