package com.cocodan.triplan.schedule.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VotingContentResponse {

    private final Long id;
    private final String content;
    private final int numOfParticipants;
    private final boolean participantFlag;
}
