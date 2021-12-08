package com.cocodan.triplan.schedule.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Voting {

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

    @OneToMany(mappedBy = "voting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VotingMember> memberIds = new ArrayList<>();

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
        memberIds.removeIf(m -> m.getMemberId().equals(memberId));

        if (votingMap.containsValue(true)) {
            VotingMember votingMember = createVotingMember(memberId);
            memberIds.add(votingMember);
        }

        votingMap.forEach( (contentId,flag) ->{
            VotingContent votingContent = getVotingContent(contentId);
            voteByFlag(flag, memberId, votingContent);
        });

    }

    private VotingContent getVotingContent(Long contentId) {
        return votingContents.stream()
                .filter(votingContent -> votingContent.getId().equals(contentId))
                .findFirst()
                .orElseThrow();
    }

    private VotingMember createVotingMember(Long memberId) {
        return VotingMember.builder()
                .voting(this)
                .memberId(memberId)
                .build();
    }

    private void voteByFlag(boolean flag, Long memberId, VotingContent votingContent) {
        if (flag) {
            votingContent.vote(memberId);
            return ;
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

    public int getVotingMemberCount(){
        return memberIds.size();
    }

    public boolean isMultipleFlag() {
        return multipleFlag;
    }
}
