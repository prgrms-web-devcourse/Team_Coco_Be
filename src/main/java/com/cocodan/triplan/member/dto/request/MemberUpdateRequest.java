package com.cocodan.triplan.member.dto.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Getter
@Builder
public class MemberUpdateRequest {
    @Email
    private String email;

    @Pattern(regexp="^[ㄱ-ㅎ|가-힣|a-z|A-Z]{2,20}$", message="이름은 공백없는 2~20자이어야 합니다")
    private String name;

    @Pattern(regexp="^[ㄱ-ㅎ|가-힣|a-z|A-Z]{1,15}$", message="닉네임은 1~15자이어야 합니다")
    private String nickname;

    private String profileImage;
}
