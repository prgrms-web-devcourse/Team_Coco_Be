package com.cocodan.triplan.post.schedule.repository;

import com.cocodan.triplan.post.schedule.domain.SchedulePostNestedComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SchedulePostNestedCommentRepository extends JpaRepository<SchedulePostNestedComment, Long> {
    @Query("select nc from SchedulePostNestedComment nc where nc.parentComment.id = :commentId")
    List<SchedulePostNestedComment> findAllByCommentId(Long commentId);

    @Query("DELETE from SchedulePostNestedComment nc where nc.parentComment.id = :commentId")
    void deleteAllByCommentId(Long commentId);
}
