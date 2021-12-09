package com.cocodan.triplan.schedule.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VotingSimpleResponse {

    private final Long id;
    private final String title;
    private final int memberCount;
}
