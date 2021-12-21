package com.cocodan.triplan.schedule.repository;

import com.cocodan.triplan.schedule.domain.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.nio.channels.FileChannel;
import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    boolean existsByIdAndScheduleId(Long id, Long scheduleId);

    @Query(value = "SELECT * FROM memo WHERE schedule_id = :scheduleId", nativeQuery = true)
    List<Memo> findByScheduleId(Long scheduleId);
}
