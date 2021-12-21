package com.cocodan.triplan.schedule.repository;

import com.cocodan.triplan.schedule.domain.Checklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChecklistRepository extends JpaRepository<Checklist, Long> {

    @Query(value = "SELECT * FROM checklist WHERE schedule_id = :scheduleId", nativeQuery = true)
    List<Checklist> findByScheduleId(Long scheduleId);

    boolean existsByIdAndScheduleId(Long id, Long scheduleId);
}
