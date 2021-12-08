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

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("There is no such member with ID: " + id));
    }
}
