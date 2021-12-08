package com.cocodan.triplan.schedule.controller;

import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.schedule.dto.request.*;
import com.cocodan.triplan.schedule.dto.response.ScheduleDetailResponse;
import com.cocodan.triplan.schedule.dto.response.ScheduleSimpleResponse;
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
    public ResponseEntity<Long> createSchedule(@Valid @RequestBody ScheduleCreationRequest scheduleCreationRequest) {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long savedId = scheduleService.createSchedule(scheduleCreationRequest, member.getId());

        return new ResponseEntity<>(savedId, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<ScheduleSimpleResponse>> getSchedules() {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<ScheduleSimpleResponse> schedules = scheduleService.getSchedules(member.getId());

        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleDetailResponse> getSchedule(@PathVariable Long scheduleId) {
        ScheduleDetailResponse schedule = scheduleService.getSchedule(scheduleId);

        return new ResponseEntity<>(schedule, HttpStatus.OK);
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<Void> modifySchedule(@PathVariable Long scheduleId, @RequestBody @Valid ScheduleModificationRequest scheduleModificationRequest) {
        scheduleService.modifySchedule(scheduleId, scheduleModificationRequest);

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
    public ResponseEntity<Long> createMemo(@PathVariable Long scheduleId, @RequestBody @Valid MemoCreationRequest memoCreationRequest) {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long savedId = scheduleService.createMemo(scheduleId, memoCreationRequest, member.getId());

        return new ResponseEntity<>(savedId, HttpStatus.CREATED);
    }

    // 체크리스트
    @PostMapping("/{scheduleId}/checklists")
    public ResponseEntity<Long> createChecklist(@PathVariable Long scheduleId, @RequestBody @Valid ChecklistCreationRequest checklistCreationRequest) {
        Long savedId = scheduleService.createChecklist(scheduleId, checklistCreationRequest);

        return new ResponseEntity<>(savedId, HttpStatus.CREATED);
    }

    // 투표
    @PostMapping("/{scheduleId}/votings")
    public ResponseEntity<Long> createVoting(@PathVariable Long scheduleId, @RequestBody @Valid VotingCreationRequest votingCreationRequest) {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long savedId = scheduleService.createVoting(scheduleId, votingCreationRequest, member.getId());

        return new ResponseEntity<>(savedId, HttpStatus.CREATED);
    }
}
