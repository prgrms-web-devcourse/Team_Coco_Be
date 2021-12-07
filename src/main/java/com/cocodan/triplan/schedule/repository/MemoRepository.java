package com.cocodan.triplan.schedule.repository;

import com.cocodan.triplan.schedule.domain.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoRepository extends JpaRepository<Memo, Long> {
}
