package com.cocodan.triplan.spot.repository;

import com.cocodan.triplan.spot.domain.Spot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpotRepository extends JpaRepository<Spot, Long> {

    List<Spot> findByIdIn(List<Long> ids);

}
