package com.cocodan.triplan.spot.service;

import com.cocodan.triplan.converter.SpotConverter;
import com.cocodan.triplan.schedule.dto.request.DailyScheduleSpotCreationRequest;
import com.cocodan.triplan.spot.domain.Spot;
import com.cocodan.triplan.spot.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpotService {

    private final SpotConverter spotConverter;

    private final SpotRepository spotRepository;

    @Transactional(readOnly = true)
    public List<Spot> findByIdIn(List<Long> spotIds) {
        return spotRepository.findByIdIn(spotIds);
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long spotId) {
        return spotRepository.existsById(spotId);
    }

    @Transactional
    public void createSpot(DailyScheduleSpotCreationRequest dailyScheduleSpotCreationRequest) {
        Spot spot = spotConverter.convertSpot(dailyScheduleSpotCreationRequest);

        spotRepository.save(spot);
    }
}
