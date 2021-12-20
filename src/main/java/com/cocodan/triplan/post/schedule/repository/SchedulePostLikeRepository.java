package com.cocodan.triplan.post.schedule.repository;

import com.cocodan.triplan.post.schedule.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SchedulePostLikeRepository extends JpaRepository<Like, Long> {

    @Query("select l from Like l where l.member.id = :memberId and l.schedulePost.id = :schedulePostId")
    Optional<Like> findByMemberIdAndSchedulePostId(Long memberId, Long schedulePostId);

    List<Like> findAllByMemberId(Long memberId);

    @Modifying
    @Query("DELETE FROM Like l where l.schedulePost.id = :schedulePostId")
    void deleteAllBySchedulePostId(Long schedulePostId);
}
