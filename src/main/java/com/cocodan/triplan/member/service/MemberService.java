package com.cocodan.triplan.member.service;

import com.cocodan.triplan.config.WebSecurityConfigure;
import com.cocodan.triplan.util.MemberConverter;
import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.member.dto.response.MemberCreateResponse;
import com.cocodan.triplan.member.dto.response.MemberDeleteResponse;
import com.cocodan.triplan.member.dto.response.MemberGetOneResponse;
import com.cocodan.triplan.member.dto.response.MemberUpdateResponse;
import com.cocodan.triplan.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final WebSecurityConfigure webSecurityConfigure;

    private final MemberRepository memberRepository;
    private final MemberConverter converter;

    @Transactional
    public MemberCreateResponse create(String email, String name, String phoneNumber, String birth, String gender, String nickname, String profileImage, String passwd, Long groupId) {
        Member orginMember = converter.toMemberEntity(email, name, phoneNumber, birth, gender, nickname, profileImage, webSecurityConfigure.passwordEncoder().encode(passwd), groupId);
        Member memberEntity = memberRepository.save(orginMember);

        return converter.toMemberCreateResponse(memberEntity);
    }

    @Transactional(readOnly = true)
    public MemberGetOneResponse getOne(Long id) {
        return memberRepository.findById(id)
                .map(converter::toMemberFindResponse)
                .orElseThrow(
                        () -> new RuntimeException("사용자 id를 조회할 수 없습니다")
                );
    }

    @Transactional(readOnly = true)
    public Page<MemberGetOneResponse> getAll(Pageable pageable){
        return memberRepository.findAll(pageable)
                .map(converter::toMemberFindResponse);
    }

    @Transactional
    public MemberUpdateResponse update(Long id, String name, String phoneNumber, String nickname, String profileImage) {
        Member originMember = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자 id를 조회할 수 없습니다"));

        originMember.changeValues(name, phoneNumber, nickname, profileImage);
        return converter.toMemberUpdateResponse(originMember);
    }

    @Transactional
    public MemberDeleteResponse delete(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException("사용자 id를 조회할 수 없습니다")
                );

        MemberDeleteResponse result = converter.toMemberDeleteResponse(member);
        memberRepository.delete(member);
        return result;
    }

    @Transactional(readOnly = true)
    public Member login(String principal, String credentials) {
        checkArgument(isNotEmpty(principal), "principal must be provided.");
        checkArgument(isNotEmpty(credentials), "credentials must be provided.");

        Member member = memberRepository.findByLoginId(principal)
                .orElseThrow(() -> new UsernameNotFoundException("Could not found user for " + principal));

        member.checkPassword(webSecurityConfigure.passwordEncoder(), credentials);

        return member;
    }
}
