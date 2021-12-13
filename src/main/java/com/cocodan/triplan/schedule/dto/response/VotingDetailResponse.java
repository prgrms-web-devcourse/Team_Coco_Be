package com.cocodan.triplan.schedule.dto.response;

import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.member.domain.vo.GenderType;
import com.cocodan.triplan.schedule.domain.Voting;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class VotingDetailResponse {

    private final Long id;

    private final String title;

    private final int numOfTotalParticipants;

    private final List<VotingContentResponse> votingContentResponses;

    private final Long ownerId;

    private final String ownerNickname;

    private final GenderType ownerGender;

    private final int ownerAge;

    public static VotingDetailResponse of(Voting voting, Member member, Long memberId) {
        int numOfTotalParticipants = voting.getNumOfTotalParticipants();

        List<VotingContentResponse> votingContentResponses = voting.getVotingContents().stream()
                .map(votingContent -> VotingContentResponse.convertVotingContentResponse(votingContent, memberId))
                .collect(Collectors.toList());

        return VotingDetailResponse.builder()
                .numOfTotalParticipants(numOfTotalParticipants)
                .id(voting.getId())
                .title(voting.getTitle())
                .ownerId(member.getId())
                .ownerNickname(member.getNickname())
                .ownerAge(member.getAge())
                .ownerGender(member.getGender())
                .votingContentResponses(votingContentResponses)
                .build();
    }
}
