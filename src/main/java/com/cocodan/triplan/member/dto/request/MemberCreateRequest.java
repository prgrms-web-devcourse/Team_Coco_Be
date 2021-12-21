package com.cocodan.triplan.member.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberCreateRequest {
    @NotBlank(message = "이메일은 공백이 아니여야 합니다")
    @Email
    private String email;

    @NotNull
    @Length(min = 3)
    private String password;

    @Pattern(regexp="^[ㄱ-ㅎ|가-힣|a-z|A-Z|]{2,20}$", message="이름은 공백없는 2~20자이어야 합니다")
    private String name;

    @Pattern(regexp="^\\d{4}-\\d{2}-\\d{2}$", message="'yyyy-mm-dd'형식으로 작성해주세요")
    private String birth;

    @ApiModelProperty(required = true, example = "string(MALE or FEMALE)")
    @Pattern(regexp="^[ㄱ-ㅎ|가-힣|a-z|A-Z]{1,10}$", message="성별은 1~10자이어야 합니다")
    private String gender;

    private String nickname;

    private String profileImage;
}
