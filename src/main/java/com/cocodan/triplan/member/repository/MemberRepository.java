package com.cocodan.triplan.member.repository;

import com.cocodan.triplan.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
