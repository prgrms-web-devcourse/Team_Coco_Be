package com.cocodan.triplan.schedule.dto.response;

import com.cocodan.triplan.schedule.domain.VotingContent;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VotingContentResponse {

    private final Long id;

    private final String content;

    private final int numOfParticipants;

    private final boolean participantFlag;

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
