package com.cocodan.triplan.post.schedule.repository;

import com.cocodan.triplan.post.schedule.domain.SchedulePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface SchedulePostRepository extends JpaRepository<SchedulePost, Long> , CustomSchedulePostRepository{

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select sp from SchedulePost sp where sp.id = :schedulePostId")
    Optional<SchedulePost> findByIdForLikedCountUpdate(Long schedulePostId);

    @Query("select sp from SchedulePost sp where sp.member.id = :memberId")
    List<SchedulePost> findAllByMemberId(Long memberId);
}
