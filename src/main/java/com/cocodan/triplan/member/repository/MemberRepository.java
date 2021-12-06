package com.cocodan.triplan.member.repository;

import com.cocodan.triplan.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Override
    Optional<Member> findById(Long aLong);

    @Override
    List<Member> findAll();

    @Override
    Member save(Member entity);

    @Override
    void delete(Member entity);
}
