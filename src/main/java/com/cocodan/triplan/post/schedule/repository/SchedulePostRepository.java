package com.cocodan.triplan.post.schedule.repository;

import com.cocodan.triplan.post.schedule.domain.SchedulePost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SchedulePostRepository extends JpaRepository<SchedulePost, Long> {

    // 최신순으로 page index부터 10개 게시글 불러오기
    List<SchedulePost> findAllByOrderByCreatedDateDesc(Pageable pageable);
}
