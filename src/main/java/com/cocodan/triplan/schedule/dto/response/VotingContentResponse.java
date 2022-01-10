package com.cocodan.triplan.schedule.dto.response;

import com.cocodan.triplan.schedule.domain.VotingContent;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class VotingContentResponse {

    private Long id;

    private String content;

    private int numOfParticipants;

    private boolean participantFlag;

    public static VotingContentResponse convertVotingContentResponse(VotingContent votingContent, Long memberId) {
        return VotingContentResponse.builder()
                .id(votingContent.getId())
                .content(votingContent.getContent())
                .numOfParticipants(votingContent.getNumOfParticipants())
                .participantFlag(checkParticipant(votingContent, memberId))
                .build();
    }

    private static boolean checkParticipant(VotingContent votingContent, Long memberId) {
        return votingContent.getVotingContentMembers().stream()
                .anyMatch(votingContentMember -> votingContentMember.getMemberId().equals(memberId));
    }
}
