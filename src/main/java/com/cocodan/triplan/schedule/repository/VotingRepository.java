package com.cocodan.triplan.schedule.repository;

import com.cocodan.triplan.schedule.domain.Voting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotingRepository extends JpaRepository<Voting, Long> {
}
