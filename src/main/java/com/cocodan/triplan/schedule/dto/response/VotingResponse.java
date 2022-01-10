package com.cocodan.triplan.schedule.dto.response;

import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.member.dto.response.MemberSimpleResponse;
import com.cocodan.triplan.schedule.domain.Voting;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class VotingResponse {

    private Long id;

    private String title;

    private int numOfTotalParticipants;

    private List<VotingContentResponse> votingContentResponses;

    private MemberSimpleResponse memberSimpleResponse;

    private boolean multipleFlag;

    public static VotingResponse from(Voting voting) {
        return VotingResponse.builder()
                .id(voting.getId())
                .title(voting.getTitle())
                .numOfTotalParticipants(voting.getNumOfTotalParticipants())
                .build();
    }

    public static VotingResponse of(Voting voting, Member member, Long memberId) {
        int numOfTotalParticipants = voting.getNumOfTotalParticipants();

        List<VotingContentResponse> votingContentResponses = voting.getVotingContents().stream()
                .map(votingContent -> VotingContentResponse.convertVotingContentResponse(votingContent, memberId))
                .collect(Collectors.toList());

        return VotingResponse.builder()
                .numOfTotalParticipants(numOfTotalParticipants)
                .id(voting.getId())
                .title(voting.getTitle())
                .memberSimpleResponse(
                        MemberSimpleResponse.from(member)
                )
                .votingContentResponses(votingContentResponses)
                .multipleFlag(voting.isMultipleFlag())
                .build();
    }
}
