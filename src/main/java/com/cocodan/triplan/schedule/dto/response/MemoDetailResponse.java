package com.cocodan.triplan.schedule.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemoDetailResponse {

    private final Long id;
    private final String title;
    private final String content;

    private final Long ownerId;
    private final String ownerNickname;
    private final int ownerAge;
    private final String ownerGender;
}
