package com.cocodan.triplan.schedule.repository;

import com.cocodan.triplan.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
            "SELECT count(*) " +
            "FROM schedule_member sm " +
            "WHERE sm.schedule_id = :scheduleId AND sm.member_id = :memberId",
    nativeQuery = true)
    int countByScheduleIdAndMemberId(Long scheduleId, Long memberId);
}
