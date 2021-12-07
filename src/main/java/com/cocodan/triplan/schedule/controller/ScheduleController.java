package com.cocodan.triplan.schedule.controller;

import com.cocodan.triplan.schedule.dto.request.ScheduleCreation;
import com.cocodan.triplan.schedule.dto.response.ScheduleDetail;
import com.cocodan.triplan.schedule.dto.response.ScheduleSimple;
import com.cocodan.triplan.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping()
    public ResponseEntity<Long> createSchedule(@Valid @RequestBody ScheduleCreation scheduleCreation) {
        Long savedId = scheduleService.createSchedule(scheduleCreation);

        return new ResponseEntity<>(savedId, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<ScheduleSimple>> getSchedules(){
        // TODO : Member 도메인 생성되면 SecurityContextHolder.getContext().getAuthentication().getPrincipal().getId(); 로 id 얻기
        List<ScheduleSimple> schedules = scheduleService.getSchedules(1L);

        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleDetail> getSchedule(@PathVariable Long scheduleId) {
        ScheduleDetail schedule = scheduleService.getSchedule(scheduleId);

        return new ResponseEntity<>(schedule, HttpStatus.OK);
    }
}
