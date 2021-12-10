package com.cocodan.triplan.schedule.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class VotingDetailResponse {

    private final Long id;
    private final String title;
    private final int numOfTotalParticipants;
    private final List<VotingContentResponse> votingContentResponses;
    private final Long ownerId;
    private final String ownerNickname;
    private final String ownerGender;
    private final int ownerAge;
}
