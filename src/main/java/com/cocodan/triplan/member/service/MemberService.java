package com.cocodan.triplan.member.service;

import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // TODO: 2021.12.07 Teru - Taid 에 의한 구현이 완성되면 제거되어야 한다.
    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("There is no such member with ID: " + id));
    }

    // TODO: 2021.12.07 Teru - Taid 에 의한 구현이 완성되면 제거되어야 한다.
    public Long save(Member member) {
        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }
}
