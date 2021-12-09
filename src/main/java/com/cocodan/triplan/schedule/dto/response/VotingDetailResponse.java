package com.cocodan.triplan.schedule.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class VotingDetailResponse {

    private Long id;
    private String title;
    private int numOfTotalParticipants;
    private List<VotingContentResponse> votingContentResponses;

    private Long ownerId;
    private String ownerNickname;
}
