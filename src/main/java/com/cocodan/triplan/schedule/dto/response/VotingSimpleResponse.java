package com.cocodan.triplan.schedule.dto.response;

import com.cocodan.triplan.schedule.domain.Voting;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VotingSimpleResponse {

    private final Long id;

    private final String title;

    private final int memberCount;

    public static VotingSimpleResponse from(Voting voting) {
        return VotingSimpleResponse.builder()
                .id(voting.getId())
                .title(voting.getTitle())
                .memberCount(voting.getNumOfTotalParticipants())
                .build();
    }
}
