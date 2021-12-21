package com.cocodan.triplan.post.schedule.repository;

import com.cocodan.triplan.post.schedule.domain.SchedulePostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SchedulePostCommentRepository extends JpaRepository<SchedulePostComment, Long> {

    @Query("select cmt from SchedulePostComment cmt where cmt.schedulePost.id = :schedulePostId")
    List<SchedulePostComment> findAllBySchedulePostId(Long schedulePostId);
}
