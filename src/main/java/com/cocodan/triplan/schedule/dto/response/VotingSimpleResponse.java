package com.cocodan.triplan.schedule.dto.response;

import com.cocodan.triplan.schedule.domain.Voting;
import lombok.Builder;
import lombok.Getter;

@Getter
public class VotingSimpleResponse {

    private final Long id;

    private final String title;

    private final int memberCount;

    @Builder
    private VotingSimpleResponse(Long id, String title, int memberCount) {
        this.id = id;
        this.title = title;
        this.memberCount = memberCount;
    }

    public static VotingSimpleResponse from(Voting voting) {
        return VotingSimpleResponse.builder()
                .id(voting.getId())
                .title(voting.getTitle())
                .memberCount(voting.getNumOfTotalParticipants())
                .build();
    }
}
