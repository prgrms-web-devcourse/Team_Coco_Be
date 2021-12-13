package com.cocodan.triplan.schedule.repository;

import com.cocodan.triplan.schedule.domain.Voting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.nio.channels.FileChannel;
import java.util.List;

public interface VotingRepository extends JpaRepository<Voting, Long> {

    boolean existsByIdAndScheduleId(Long id, Long scheduleId);

    @Query(value = "SELECT * FROM voting WHERE schedule_id = :scheduleId", nativeQuery = true)
    List<Voting> findByScheduleId(Long scheduleId);
}
