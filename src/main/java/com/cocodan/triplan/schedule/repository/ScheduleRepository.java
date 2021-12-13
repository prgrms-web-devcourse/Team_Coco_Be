package com.cocodan.triplan.schedule.repository;

import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query(value =
            "SELECT s " +
            "FROM Schedule s " +
                "JOIN FETCH s.scheduleThema " +
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

    boolean existsByIdAndMemberId(Long id, Long memberId);

    @Query(value = "SELECT s FROM Schedule s JOIN FETCH s.scheduleMembers WHERE s.id = :id")
    Optional<Schedule> findScheduleMemosById(Long id);
}
