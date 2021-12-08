package com.cocodan.triplan.schedule.controller;

import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.schedule.dto.request.*;
import com.cocodan.triplan.schedule.dto.response.ScheduleDetail;
import com.cocodan.triplan.schedule.dto.response.ScheduleSimple;
import com.cocodan.triplan.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    // 일정
    @PostMapping()
    public ResponseEntity<Long> createSchedule(@Valid @RequestBody ScheduleCreation scheduleCreation) {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long savedId = scheduleService.createSchedule(scheduleCreation, member.getId());

        return new ResponseEntity<>(savedId, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<ScheduleSimple>> getSchedules() {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<ScheduleSimple> schedules = scheduleService.getSchedules(member.getId());

        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleDetail> getSchedule(@PathVariable Long scheduleId) {
        ScheduleDetail schedule = scheduleService.getSchedule(scheduleId);

        return new ResponseEntity<>(schedule, HttpStatus.OK);
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<Void> modifySchedule(@PathVariable Long scheduleId, @RequestBody @Valid ScheduleModification scheduleModification) {
        scheduleService.modifySchedule(scheduleId, scheduleModification);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId) {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        scheduleService.deleteSchedule(scheduleId, member.getId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 메모
    @PostMapping("/{scheduleId}/memos")
    public ResponseEntity<Long> createMemo(@PathVariable Long scheduleId, @RequestBody @Valid MemoCreation memoCreation) {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long savedId = scheduleService.createMemo(scheduleId, memoCreation, member.getId());

        return new ResponseEntity<>(savedId, HttpStatus.CREATED);
    }

    // 체크리스트
    @PostMapping("/{scheduleId}/checklists")
    public ResponseEntity<Long> createChecklist(@PathVariable Long scheduleId, @RequestBody @Valid ChecklistCreation checklistCreation) {
        Long savedId = scheduleService.createChecklist(scheduleId, checklistCreation);

        return new ResponseEntity<>(savedId, HttpStatus.CREATED);
    }

    // 투표
    @PostMapping("/{scheduleId}/votings/")
    public ResponseEntity<Long> createVoting(@PathVariable Long scheduleId, @RequestBody @Valid VotingCreation votingCreation) {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long savedId = scheduleService.createVoting(scheduleId, votingCreation, member.getId());

        return new ResponseEntity<>(savedId, HttpStatus.CREATED);
    }
}
