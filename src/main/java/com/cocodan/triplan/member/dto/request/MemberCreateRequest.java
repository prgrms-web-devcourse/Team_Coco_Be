package com.cocodan.triplan.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
@Builder
public class MemberCreateRequest {
    @NotBlank(message = "이메일은 공백이 아니여야 합니다")
    @Email
    private String email;

    @Pattern(regexp="^[ㄱ-ㅎ|가-힣|a-z|A-Z|]{2,20}$", message="이름은 공백없는 2~20자이어야 합니다")
    private String name;

    @Pattern(regexp="[0-9]{3}[0-9]{3,4}[0-9]{4}", message="'-'없이 입력해 주세요")
    private String phoneNumber;

    @Pattern(regexp="[0-9]{8}", message="'yyyymmdd'형식으로 작성해주세요")
    private String birth;

    @Pattern(regexp="^[ㄱ-ㅎ|가-힣|a-z|A-Z]{1,10}$", message="성별은 1~10자이어야 합니다")
    private String gender;

    private String nickname;
    private String profileImage;
}
