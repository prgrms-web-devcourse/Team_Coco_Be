package com.cocodan.triplan.member.dto.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@Builder
public class MemberGetOneRequest {
    @NotNull(message = "아이디를 입력하세요.")
    private final Long id;
}
