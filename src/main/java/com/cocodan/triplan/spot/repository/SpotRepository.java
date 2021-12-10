package com.cocodan.triplan.spot.repository;

import com.cocodan.triplan.spot.domain.Spot;
import com.cocodan.triplan.spot.dto.response.SpotSimple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface SpotRepository extends JpaRepository<Spot, Long> {

    @Query(value =
            "select new com.cocodan.triplan.spot.dto.response.SpotSimple(s.id, s.placeName, s.latitude, s.longitude) " +
            "from Spot s " +
            "where s.id = :spotId")
    Optional<SpotSimple> findBySpotId(Long spotId);

    List<Spot> findByIdIn(List<Long> ids);

}
