package com.cocodan.triplan.schedule.controller;

import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.schedule.dto.request.*;
import com.cocodan.triplan.schedule.dto.response.*;
import com.cocodan.triplan.schedule.service.ScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "Schedule")
@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    private final Long MEMBER_ID = 1L;

    // 일정
    @ApiOperation("일정 생성")
    @PostMapping()
    public ResponseEntity<Long> createSchedule(@Valid @RequestBody ScheduleCreationRequest scheduleCreationRequest) {
//        Member member = getMember();
//        Long savedId = scheduleService.createSchedule(scheduleCreationRequest, member.getId());
        Long savedId = scheduleService.saveSchedule(scheduleCreationRequest, MEMBER_ID);


        return ResponseEntity.status(HttpStatus.CREATED).body(savedId);
    }

    private Member getMember() {
        return (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @ApiOperation("회원이 속한 일정 목록 조회")
    @GetMapping()
    public ResponseEntity<List<ScheduleSimpleResponse>> getSchedules() {
//        Member member = getMember();
//        List<ScheduleSimpleResponse> schedules = scheduleService.getSchedules(member.getId());
        List<ScheduleSimpleResponse> schedules = scheduleService.getSchedules(MEMBER_ID);

        return ResponseEntity.ok(schedules);
    }

    @ApiOperation("일정 상세 조회")
    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleDetailResponse> getSchedule(@PathVariable Long scheduleId) {
//        Member member = getMember();
//        ScheduleDetailResponse schedule = scheduleService.getSchedule(scheduleId, member.getId());
        ScheduleDetailResponse schedule = scheduleService.getSchedule(scheduleId, MEMBER_ID);

        return ResponseEntity.ok(schedule);
    }

    @ApiOperation("일정 수정")
    @PutMapping("/{scheduleId}")
    public ResponseEntity<Void> modifySchedule(@PathVariable Long scheduleId, @RequestBody @Valid ScheduleModificationRequest scheduleModificationRequest) {
        scheduleService.modifySchedule(scheduleId, scheduleModificationRequest);

        return ResponseEntity.ok(null);
    }

    @ApiOperation("일정 삭제")
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId) {
//        Member member = getMember();
//        scheduleService.deleteSchedule(scheduleId, member.getId());
        scheduleService.deleteSchedule(scheduleId, MEMBER_ID);

        return ResponseEntity.ok(null);
    }

    // 메모
    @ApiOperation("메모 생성")
    @PostMapping("/{scheduleId}/memos")
    public ResponseEntity<Long> createMemo(@PathVariable Long scheduleId, @RequestBody @Valid MemoRequest memoRequest) {
//        Member member = getMember();
//        Long savedId = scheduleService.createMemo(scheduleId, memoRequest, member.getId());
        Long savedId = scheduleService.saveMemo(scheduleId, memoRequest, MEMBER_ID);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedId);
    }

    @ApiOperation("일정에 속한 메모 목록 조회")
    @GetMapping("/{scheduleId}/memos")
    public ResponseEntity<List<MemoSimpleResponse>> getMemos(@PathVariable Long scheduleId) {
//        Member member = getMember();
//        List<MemoSimpleResponse> memos = scheduleService.getMemos(scheduleId, member.getId());
        List<MemoSimpleResponse> memos = scheduleService.getMemos(scheduleId, MEMBER_ID);

        return ResponseEntity.ok(memos);
    }

    @ApiOperation("메모 상세 조회")
    @GetMapping("/{scheduleId}/memos/{memoId}")
    public ResponseEntity<MemoDetailResponse> getMemo(@PathVariable Long scheduleId, @PathVariable Long memoId) {
//        Member member = getMember();
//        MemoDetailResponse memoDetailResponse = scheduleService.getMemo(scheduleId, memoId, member.getId());
        MemoDetailResponse memoDetailResponse = scheduleService.getMemo(scheduleId, memoId, MEMBER_ID);

        return ResponseEntity.ok(memoDetailResponse);
    }

    @ApiOperation("메모 수정")
    @PutMapping("/{scheduleId}/memos/{memoId}")
    public ResponseEntity<Void> modifyMemo(@PathVariable Long scheduleId, @PathVariable Long
            memoId, @RequestBody @Valid MemoRequest memoRequest) {
//        Member member = getMember();
//        scheduleService.modifyMemo(scheduleId, memoId, memoRequest, member.getId());
        scheduleService.modifyMemo(scheduleId, memoId, memoRequest, MEMBER_ID);

        return ResponseEntity.ok(null);
    }

    @ApiOperation("메모 삭제")
    @DeleteMapping("/{scheduleId}/memos/{memoId}")
    public ResponseEntity<Void> deleteMemo(@PathVariable Long scheduleId, @PathVariable Long memoId) {
//        Member member = getMember();
//        scheduleService.deleteMemo(scheduleId, memoId, member.getId());
        scheduleService.deleteMemo(scheduleId, memoId, MEMBER_ID);

        return ResponseEntity.ok(null);
    }

    // 체크리스트
    @ApiOperation("체크리스트 생성")
    @PostMapping("/{scheduleId}/checklists")
    public ResponseEntity<Long> saveChecklist(@PathVariable Long
                                                      scheduleId, @RequestBody @Valid ChecklistCreationRequest checklistCreationRequest) {
        Long savedId = scheduleService.saveChecklist(scheduleId, checklistCreationRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedId);
    }

    @ApiOperation("일정에 속한 체크리스트 목록 조회")
    @GetMapping("/{scheduleId}/checklists")
    public ResponseEntity<List<ChecklistResponse>> getChecklists(@PathVariable Long scheduleId) {
//        Member member = getMember();
//        List<ChecklistResponse> checklistResponses = scheduleService.getChecklists(scheduleId, member.getId());
        List<ChecklistResponse> checklistResponses = scheduleService.getChecklists(scheduleId, MEMBER_ID);

        return ResponseEntity.ok(checklistResponses);
    }

    @ApiOperation("체크리스트 선택 및 해제")
    @PatchMapping("/{scheduleId}/checklists/{checklistId}")
    public ResponseEntity<Void> doCheck(@PathVariable Long scheduleId, @PathVariable Long checklistId,
                                        @RequestParam boolean flag) {
//        Member member = getMember();
//        scheduleService.doCheck(scheduleId, checklistId, member.getId(), flag);
        scheduleService.doCheck(scheduleId, checklistId, MEMBER_ID, flag);

        return ResponseEntity.ok(null);
    }

    @ApiOperation("체크리스트 삭제")
    @DeleteMapping("/{scheduleId}/checklists/{checklistId}")
    public ResponseEntity<Void> modifyChecklist(@PathVariable Long scheduleId, @PathVariable Long checklistId) {
//        Member member = getMember();
//        scheduleService.deleteChecklist(scheduleId, checklistId, member.getId());
        scheduleService.deleteChecklist(scheduleId, checklistId, MEMBER_ID);

        return ResponseEntity.ok(null);
    }

    // 투표
    @ApiOperation("투표 생성")
    @PostMapping("/{scheduleId}/votings")
    public ResponseEntity<Long> createVoting(@PathVariable Long scheduleId, @RequestBody @Valid VotingCreationRequest votingCreationRequest) {
//        Member member = getMember();
//        Long savedId = scheduleService.createVoting(scheduleId, votingCreationRequest, member.getId());
        Long savedId = scheduleService.saveVoting(scheduleId, votingCreationRequest, MEMBER_ID);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedId);
    }

    @ApiOperation("일정에 속한 투표 목록 조회")
    @GetMapping("/{scheduleId}/votings")
    public ResponseEntity<List<VotingSimpleResponse>> getVotingList(@PathVariable Long scheduleId) {
        List<VotingSimpleResponse> votingSimpleResponses = scheduleService.getVotingList(scheduleId);

        return ResponseEntity.ok(votingSimpleResponses);
    }

    @ApiOperation("투표 상세 조회")
    @GetMapping("/{scheduleId}/votings/{votingId}")
    public ResponseEntity<VotingDetailResponse> getVoting(@PathVariable Long scheduleId, @PathVariable Long votingId) {
//        Member member = getMember();
//        VotingDetailResponse votingDetailResponse = scheduleService.getVoting(scheduleId, votingId, member.getId());
        VotingDetailResponse votingDetailResponse = scheduleService.getVoting(scheduleId, votingId, MEMBER_ID);

        return ResponseEntity.ok(votingDetailResponse);
    }

    @ApiOperation("투표 행사")
    @PatchMapping("/{scheduleId}/votings/{votingId}")
    public ResponseEntity<Void> doVote(@PathVariable Long scheduleId, @PathVariable Long
            votingId, @RequestBody @Valid VotingRequest votingRequest) {
//        Member member = getMember();
//        scheduleService.doVote(scheduleId, votingId, votingRequest, member.getId());
        scheduleService.doVote(scheduleId, votingId, votingRequest, MEMBER_ID);

        return ResponseEntity.ok(null);
    }

    @ApiOperation("투표 삭제")
    @DeleteMapping("/{scheduleId}/votings/{votingId}")
    public ResponseEntity<Void> deleteVoting(@PathVariable Long scheduleId, @PathVariable Long votingId) {
//        Member member = getMember();
//        scheduleService.deleteVoting(scheduleId, votingId, member.getId());
        scheduleService.deleteVoting(scheduleId, votingId, MEMBER_ID);

        return ResponseEntity.ok(null);
    }
}
