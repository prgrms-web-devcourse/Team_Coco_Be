package com.cocodan.triplan.member.repository;

import com.cocodan.triplan.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByIdIn(List<Long> ids);

    @Query(value = "SELECT count(0) > 0 FROM FRIEND f WHERE f.to_id = :friendId AND f.from_id = :id", nativeQuery = true)
    boolean existsByIdAndFriendId(Long id, long friendId);
}
