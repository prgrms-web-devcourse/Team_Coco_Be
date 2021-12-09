package com.cocodan.triplan.post.schedule.repository;

import com.cocodan.triplan.post.schedule.domain.SchedulePost;
import com.cocodan.triplan.post.schedule.vo.SchedulePostSortingRule;
import com.cocodan.triplan.schedule.domain.Schedule;
import com.cocodan.triplan.schedule.domain.vo.Thema;
import com.cocodan.triplan.spot.domain.vo.City;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;

import java.util.List;

public interface SchedulePostRepository extends JpaRepository<SchedulePost, Long> {

    // TODO: 2021.12.09 Teru - List가 아니라 Page 형태로 반환하도록 수정하기
    List<SchedulePost> findAllByOrderByCreatedDateDesc(Pageable pageable);
    List<SchedulePost> findAllByOrderByViewsDesc(Pageable pageable);
    // @Query("select sp from SchedulePost as sp where (sp.title like %:search% or sp.content like %:search%) order by sp.createdDate desc")
    List<SchedulePost> findAllByTitleOrContentContainingOrderByCreatedDateDesc(String title, String content, Pageable pageable);
    List<SchedulePost> findAllByTitleOrContentContainingOrderByViewsDesc(String title, String content, Pageable pageable);
    List<SchedulePost> findAllByCityOrderByCreatedDateDesc(City city, Pageable pageable);
    List<SchedulePost> findAllByCityOrderByViewsDesc(City city, Pageable pageable);
    List<SchedulePost> findAllByCityAndTitleOrContentContainingOrderByCreatedDateDesc(City city, String title, String content, Pageable pageable);
    List<SchedulePost> findAllByCityAndTitleOrContentContainingOrderByViewsDesc(City city, String title, String content, Pageable pageable);
}
