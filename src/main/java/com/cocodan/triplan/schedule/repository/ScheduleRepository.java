package com.cocodan.triplan.schedule.repository;

import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query(value =
            "SELECT s " +
            "FROM Schedule s " +
                "JOIN FETCH s.scheduleThemes " +
                "JOIN s.scheduleMembers sm " +
            "WHERE sm.memberId = :memberId"
    )
    List<Schedule> findByMemberId(Long memberId);

    @Query(value =
            "SELECT s " +
            "FROM Schedule s " +
                "LEFT JOIN FETCH s.dailyScheduleSpots " +
            "WHERE s.id = :id"
    )
    Optional<Schedule> findOneWithSpotsById(Long id);

    @Query(value =
            "SELECT count(0) > 0 " +
            "FROM schedule_member sm " +
            "WHERE sm.schedule_id = :scheduleId AND sm.member_id = :memberId"
            , nativeQuery = true)
    boolean existsByScheduleIdAndMemberId(Long scheduleId, Long memberId);
}
