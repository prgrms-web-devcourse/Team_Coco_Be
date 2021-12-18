package com.cocodan.triplan.schedule.domain;

import com.cocodan.triplan.common.BaseEntity;
import com.cocodan.triplan.exception.common.NotFoundException;
import com.google.common.collect.Range;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Voting extends BaseEntity {

    public static final int TITLE_MIN_LENGTH = 1;
    public static final int TITLE_MAX_LENGTH = 16;

    public static final int CONTENT_COUNT_MAX = 10;
    public static final int CONTENT_COUNT_MIN = 2;

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
    private Voting(Schedule schedule, String title, Long memberId, boolean multipleFlag) {
        checkNotNull(schedule, "Schedule is required");
        checkNotNull(title, "Title is required");
        checkArgument(Range.closed(TITLE_MIN_LENGTH, TITLE_MAX_LENGTH).contains(title.length()));
        checkMemberId(memberId);

        this.schedule = schedule;
        this.title = title;
        this.memberId = memberId;
        this.multipleFlag = multipleFlag;
        this.schedule.getVotingList().add(this);
    }

    private void checkMemberId(Long memberId) {
        checkNotNull(memberId, "MemberId is required");
        checkArgument(memberId > 0, "MemberId must be positive, you supplied %d", memberId);
    }

    public void vote(Map<Long, Boolean> votingMap, Long memberId) {
        checkNotNull(votingMap);
        checkMemberId(memberId);
        checkArgument(votingMap.size() > 0, "Should vote at least one");
        checkMultipleMode(votingMap);


        votingMap.forEach((contentId, flag) -> {
            VotingContent votingContent = getVotingContent(contentId);
            voteByFlag(flag, memberId, votingContent);
        });
    }

    private void checkMultipleMode(Map<Long, Boolean> votingMap) {
        if (multipleFlag) {
            return ;
        }

        int trueCount = 0;
        for (Boolean value : votingMap.values()) {
            if(value) trueCount++;
        }

        if (trueCount > 1) {
            throw new IllegalArgumentException("It's single vote mode. You supplied more than one");
        }
    }

    private VotingContent getVotingContent(Long contentId) {
        return votingContents.stream()
                .filter(votingContent -> votingContent.getId().equals(contentId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(VotingContent.class, contentId));
    }

    private void voteByFlag(boolean flag, Long memberId, VotingContent votingContent) {
        checkNotNull(votingContent);
        checkMemberId(memberId);

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
