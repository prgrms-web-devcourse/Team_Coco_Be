package com.cocodan.triplan.util;

import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.member.domain.vo.GenderType;
import com.cocodan.triplan.member.dto.response.MemberCreateResponse;
import com.cocodan.triplan.member.dto.response.MemberDeleteResponse;
import com.cocodan.triplan.member.dto.response.MemberGetOneResponse;
import com.cocodan.triplan.member.dto.response.MemberUpdateResponse;
import org.springframework.stereotype.Component;

@Component
public class MemberConverter {
    public Member toMemberEntity(String email, String name, String phoneNumber, String birth, String gender, String nickname, String profileImage) {
        return Member.builder()
                .email(email)
                .name(name)
                .phoneNumber(phoneNumber)
                .birth(birth)
                .gender(GenderType.of(gender))
                .nickname(nickname)
                .profileImage(profileImage)
                .build();
    }

    public MemberGetOneResponse toMemberFindAllResponse(Member member) {
        return MemberGetOneResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .phoneNumber(member.getPhoneNumber())
                .birth(member.getBirth())
                .gender(member.getGender())
                .nickname(member.getNickname())
                .profileImage(member.getProfileImage())
                .build();
    }

    public MemberCreateResponse toMemberCreateResponse(Member member) {
        return MemberCreateResponse.builder()
                .id(member.getId())
                .build();
    }

    public MemberUpdateResponse toMemberUpdateResponse(Member member) {
        return MemberUpdateResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .phoneNumber(member.getPhoneNumber())
                .birth(member.getBirth())
                .gender(member.getGender())
                .build();
    }

    public MemberDeleteResponse toMemberDeleteResponse(Member member) {
        return MemberDeleteResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .phoneNumber(member.getPhoneNumber())
                .birth(member.getBirth())
                .gender(member.getGender())
                .build();
    }
}
