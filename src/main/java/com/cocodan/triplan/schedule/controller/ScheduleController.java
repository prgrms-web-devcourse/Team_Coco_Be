package com.cocodan.triplan.schedule.controller;

import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.member.dto.response.MemberDeleteResponse;
import com.cocodan.triplan.schedule.dto.request.*;
import com.cocodan.triplan.schedule.dto.response.*;
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
        Member member = getMember();
        Long savedId = scheduleService.createSchedule(scheduleCreationRequest, member.getId());

        return new ResponseEntity<>(savedId, HttpStatus.CREATED);
    }

    private Member getMember() {
        return (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @GetMapping()
    public ResponseEntity<List<ScheduleSimpleResponse>> getSchedules() {
        Member member = getMember();
        List<ScheduleSimpleResponse> schedules = scheduleService.getSchedules(member.getId());

        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleDetailResponse> getSchedule(@PathVariable Long scheduleId) {
        Member member = getMember();
        ScheduleDetailResponse schedule = scheduleService.getSchedule(scheduleId, member.getId());

        return new ResponseEntity<>(schedule, HttpStatus.OK);
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<Void> modifySchedule(@PathVariable Long scheduleId, @RequestBody @Valid ScheduleModificationRequest scheduleModificationRequest) {
        scheduleService.modifySchedule(scheduleId, scheduleModificationRequest);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId) {
        Member member = getMember();
        scheduleService.deleteSchedule(scheduleId, member.getId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 메모
    @PostMapping("/{scheduleId}/memos")
    public ResponseEntity<Long> createMemo(@PathVariable Long scheduleId, @RequestBody @Valid MemoRequest memoRequest) {
        Member member = getMember();
        Long savedId = scheduleService.createMemo(scheduleId, memoRequest, member.getId());

        return new ResponseEntity<>(savedId, HttpStatus.CREATED);
    }

    @GetMapping("/{scheduleId}/memos")
    public ResponseEntity<List<MemoSimpleResponse>> getMemos(@PathVariable Long scheduleId) {
        Member member = getMember();
        List<MemoSimpleResponse> memos = scheduleService.getMemos(scheduleId, member.getId());

        return new ResponseEntity<>(memos, HttpStatus.OK);
    }

    @GetMapping("/{scheduleId}/memos/{memoId}")
    public ResponseEntity<MemoDetailResponse> getMemo(@PathVariable Long scheduleId, @PathVariable Long memoId) {
        Member member = getMember();
        MemoDetailResponse memoDetailResponse = scheduleService.getMemo(scheduleId, memoId, member.getId());

        return new ResponseEntity<>(memoDetailResponse, HttpStatus.OK);
    }

    @PutMapping("/{scheduleId}/memos/{memoId}")
    public ResponseEntity<Void> modifyMemo(@PathVariable Long scheduleId, @PathVariable Long memoId, @RequestBody @Valid MemoRequest memoRequest) {
        Member member = getMember();
        scheduleService.modifyMemo(scheduleId, memoId, memoRequest, member.getId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{scheduleId}/memos/{memoId}")
    public ResponseEntity<Void> deleteMemo(@PathVariable Long scheduleId, @PathVariable Long memoId) {
        Member member = getMember();
        scheduleService.deleteMemo(scheduleId, memoId, member.getId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 체크리스트
    @PostMapping("/{scheduleId}/checklists")
    public ResponseEntity<Long> createChecklist(@PathVariable Long scheduleId, @RequestBody @Valid ChecklistCreationRequest checklistCreationRequest) {
        Long savedId = scheduleService.createChecklist(scheduleId, checklistCreationRequest);

        return new ResponseEntity<>(savedId, HttpStatus.CREATED);
    }

    @GetMapping("/{scheduleId}/checklists")
    public ResponseEntity<List<ChecklistResponse>> getChecklists(@PathVariable Long scheduleId) {
        Member member = getMember();
        List<ChecklistResponse> checklistResponses = scheduleService.getChecklists(scheduleId, member.getId());

        return new ResponseEntity<>(checklistResponses, HttpStatus.OK);
    }

    @PatchMapping("/{scheduleId}/checklists/{checklistId}")
    public ResponseEntity<Void> doCheck(@PathVariable Long scheduleId, @PathVariable Long checklistId, @RequestParam boolean flag) {
        Member member = getMember();
        scheduleService.doCheck(scheduleId, checklistId, member.getId(), flag);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{scheduleId}/checklists/{checklistId}")
    public ResponseEntity<Void> modifyChecklist(@PathVariable Long scheduleId, @PathVariable Long checklistId) {
        Member member = getMember();
        scheduleService.deleteChecklist(scheduleId, checklistId, member.getId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 투표
    @PostMapping("/{scheduleId}/votings")
    public ResponseEntity<Long> createVoting(@PathVariable Long scheduleId, @RequestBody @Valid VotingCreationRequest votingCreationRequest) {
        Member member = getMember();
        Long savedId = scheduleService.createVoting(scheduleId, votingCreationRequest, member.getId());

        return new ResponseEntity<>(savedId, HttpStatus.CREATED);
    }

    @GetMapping("/{scheduleId}/votings")
    public ResponseEntity<List<VotingSimpleResponse>> getVotingList(@PathVariable Long scheduleId) {
        List<VotingSimpleResponse> votingSimpleResponses = scheduleService.getVotingList(scheduleId);

        return new ResponseEntity<>(votingSimpleResponses, HttpStatus.OK);
    }

    @GetMapping("/{scheduleId}/votings/{votingId}")
    public ResponseEntity<VotingDetailResponse> getVoting(@PathVariable Long scheduleId, @PathVariable Long votingId) {
        Member member = getMember();
        VotingDetailResponse votingDetailResponse = scheduleService.getVoting(scheduleId, votingId, member.getId());

        return new ResponseEntity<>(votingDetailResponse, HttpStatus.OK);
    }

    @PatchMapping("/{scheduleId}/votings/{votingId}")
    public ResponseEntity<Void> doVote(@PathVariable Long scheduleId, @PathVariable Long votingId, @RequestBody @Valid VotingRequest votingRequest) {
        Member member = getMember();
        scheduleService.doVote(scheduleId, votingId, votingRequest, member.getId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{scheduleId}/votings/{votingId}")
    public ResponseEntity<Void> deleteVoting(@PathVariable Long scheduleId, @PathVariable Long votingId) {
        Member member = getMember();
        scheduleService.deleteVoting(scheduleId, votingId, member.getId());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
