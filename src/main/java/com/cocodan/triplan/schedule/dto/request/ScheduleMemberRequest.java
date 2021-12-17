package com.cocodan.triplan.schedule.dto.request;

import lombok.Getter;

import javax.validation.constraints.Positive;

@Getter
public class ScheduleMemberRequest {

    @Positive
    private long friendId;
}
