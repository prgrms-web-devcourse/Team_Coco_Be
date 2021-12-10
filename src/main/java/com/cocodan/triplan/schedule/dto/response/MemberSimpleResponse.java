package com.cocodan.triplan.schedule.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberSimpleResponse {

    private Long id;

    private String nickname;

}
