package com.cocodan.triplan.schedule.repository;

import com.cocodan.triplan.schedule.domain.Checklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChecklistRepository extends JpaRepository<Checklist, Long> {

    List<Checklist> findByScheduleId(Long scheduleId);
}
