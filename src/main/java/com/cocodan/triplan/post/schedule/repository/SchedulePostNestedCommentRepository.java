package com.cocodan.triplan.post.schedule.repository;

import com.cocodan.triplan.post.schedule.domain.SchedulePostNestedComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SchedulePostNestedCommentRepository extends JpaRepository<SchedulePostNestedComment, Long> {
    List<SchedulePostNestedComment> findAllByCommentId(Long commentId);
}
