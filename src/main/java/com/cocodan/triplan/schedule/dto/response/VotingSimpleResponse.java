package com.cocodan.triplan.schedule.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class VotingSimpleResponse {

    private String title;
    private int memberCount;

    @Builder
    public VotingSimpleResponse(String title, int memberCount) {
        this.title = title;
        this.memberCount = memberCount;
    }
}
