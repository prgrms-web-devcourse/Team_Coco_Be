package com.cocodan.triplan.member.dto.response;

import com.cocodan.triplan.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberSimpleResponse {

    private Long id;

    private String nickname;

    private String imageUrl;

    @Builder
    private MemberSimpleResponse(Long id, String nickname, String imageUrl) {
        this.id = id;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }

    public static MemberSimpleResponse from(Member member) {
        return MemberSimpleResponse.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .imageUrl(member.getProfileImage())
                .build();
    }
}
