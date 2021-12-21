package com.cocodan.triplan.member.repository;

import com.cocodan.triplan.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByIdIn(List<Long> ids);

    @Query("select m from Member m join fetch m.group g left join fetch g.permissions gp join fetch gp.permission where m.email = :email")
    Optional<Member> findByLoginId(String email);

    Optional<Member> findByEmail(String email);

    List<Member> findAllByNicknameContaining(String nickname);

    Optional<Member> findByNickname(String nickname);
}
