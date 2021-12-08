package com.cocodan.triplan.member.dto.response;


import com.cocodan.triplan.member.domain.vo.GenderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MemberDeleteResponse {
    private final Long id;
    private final String email;
    private final String name;
    private final String phoneNumber;
    private final String birth;
    private final GenderType gender;
    private final String nickname;
    private final String profileImage;
}
