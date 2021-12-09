package com.cocodan.triplan.schedule.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VotingContentResponse {

    private Long id;
    private String content;
    private int numOfParticipants;
    private boolean participantFlag;
}
