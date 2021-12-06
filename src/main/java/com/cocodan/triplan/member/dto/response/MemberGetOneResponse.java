package com.cocodan.triplan.member.dto.response;

import com.cocodan.triplan.member.domain.vo.GenderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MemberGetOneResponse {
    private final Long id;
    private final String email;
    private final String name;
    private final String phoneNumber;
    private final String birth;
    private final GenderType gender;
    private final String nickname;
    private final String profileImage;

    @Override
    public String toString() {
        return "MemberGetOneResponse{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", birth='" + birth + '\'' +
                ", gender=" + gender +
                ", nickname='" + nickname + '\'' +
                ", profileImage='" + profileImage + '\'' +
                '}';
    }
}
