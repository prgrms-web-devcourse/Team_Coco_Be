package com.cocodan.triplan.util;

import com.cocodan.triplan.member.domain.Group;
import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.member.domain.vo.GenderType;
import com.cocodan.triplan.member.dto.response.MemberCreateResponse;
import com.cocodan.triplan.member.dto.response.MemberDeleteResponse;
import com.cocodan.triplan.member.dto.response.MemberGetOneResponse;
import com.cocodan.triplan.member.dto.response.MemberUpdateResponse;
import com.cocodan.triplan.member.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MemberConverter {
    @Autowired
    GroupRepository groupRepository;

    public Member toMemberEntity(String email, String name, String birth, String gender, String nickname, String profileImage, String passwd, Long groupId) {
        Optional<Group> group = groupRepository.findById(groupId);
        return Member.builder()
                .email(email)
                .name(name)
                .birth(birth)
                .gender(GenderType.of(gender))
                .nickname(nickname)
                .profileImage(profileImage)
                .passwd(passwd)
                .group(group.get())
                .build();
    }

    public MemberGetOneResponse toMemberFindResponse(Member member) {
        return MemberGetOneResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
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
                .birth(member.getBirth())
                .gender(member.getGender())
                .build();
    }

    public MemberDeleteResponse toMemberDeleteResponse(Member member) {
        return MemberDeleteResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .birth(member.getBirth())
                .gender(member.getGender())
                .build();
    }
}
