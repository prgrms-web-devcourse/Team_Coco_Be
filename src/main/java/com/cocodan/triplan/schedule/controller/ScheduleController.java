package com.cocodan.triplan.schedule.controller;

import com.cocodan.triplan.jwt.JwtAuthentication;
import com.cocodan.triplan.member.dto.response.MemberSimpleResponse;
import com.cocodan.triplan.schedule.dto.request.*;
import com.cocodan.triplan.schedule.dto.response.*;
import com.cocodan.triplan.schedule.service.ScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "Schedule")
@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    // 일정
    @ApiOperation("일정 생성")
    @PostMapping()
    public ResponseEntity<IdResponse> createSchedule(@Valid @RequestBody ScheduleCreationRequest scheduleCreationRequest, @AuthenticationPrincipal JwtAuthentication authentication) {
        Long savedId = scheduleService.saveSchedule(scheduleCreationRequest, authentication.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(new IdResponse(savedId));
    }

    @ApiOperation("회원이 속한 일정 목록 조회")
    @GetMapping()
    public ResponseEntity<List<ScheduleSimpleResponse>> getSchedules(@AuthenticationPrincipal JwtAuthentication authentication) {
        List<ScheduleSimpleResponse> schedules = scheduleService.getSchedules(authentication.getId());

        return ResponseEntity.ok(schedules);
    }

    @ApiOperation("일정 상세 조회")
    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleDetailResponse> getSchedule(@PathVariable Long scheduleId) {
        ScheduleDetailResponse schedule = scheduleService.getSchedule(scheduleId);

        return ResponseEntity.ok(schedule);
    }

    @ApiOperation("일정 수정")
    @PutMapping("/{scheduleId}")
    public ResponseEntity<Void> modifySchedule(
            @PathVariable Long scheduleId,
            @RequestBody @Valid ScheduleModificationRequest scheduleModificationRequest,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        scheduleService.modifySchedule(scheduleId, scheduleModificationRequest, authentication.getId());

        return ResponseEntity.ok().build();
    }

    @ApiOperation("일정 삭제")
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId, @AuthenticationPrincipal JwtAuthentication authentication) {
        scheduleService.deleteSchedule(scheduleId, authentication.getId());

        return ResponseEntity.ok().build();
    }

    // 메모
    @ApiOperation("메모 생성")
    @PostMapping("/{scheduleId}/memos")
    public ResponseEntity<IdResponse> createMemo(
            @PathVariable Long scheduleId,
            @RequestBody @Valid MemoRequest memoRequest,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        Long savedId = scheduleService.saveMemo(scheduleId, memoRequest, authentication.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(new IdResponse(savedId));
    }

    @ApiOperation("일정에 속한 메모 목록 조회")
    @GetMapping("/{scheduleId}/memos")
    public ResponseEntity<List<MemoSimpleResponse>> getMemos(@PathVariable Long scheduleId, @AuthenticationPrincipal JwtAuthentication authentication) {
        List<MemoSimpleResponse> memos = scheduleService.getMemos(scheduleId, authentication.getId());

        return ResponseEntity.ok(memos);
    }

    @ApiOperation("메모 상세 조회")
    @GetMapping("/{scheduleId}/memos/{memoId}")
    public ResponseEntity<MemoDetailResponse> getMemo(
            @PathVariable Long scheduleId,
            @PathVariable Long memoId,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        MemoDetailResponse memoDetailResponse = scheduleService.getMemo(scheduleId, memoId, authentication.getId());

        return ResponseEntity.ok(memoDetailResponse);
    }

    @ApiOperation("메모 수정")
    @PutMapping("/{scheduleId}/memos/{memoId}")
    public ResponseEntity<Void> modifyMemo(
            @PathVariable Long scheduleId,
            @PathVariable Long memoId,
            @RequestBody @Valid MemoRequest memoRequest,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        scheduleService.modifyMemo(scheduleId, memoId, memoRequest, authentication.getId());

        return ResponseEntity.ok().build();
    }

    @ApiOperation("메모 삭제")
    @DeleteMapping("/{scheduleId}/memos/{memoId}")
    public ResponseEntity<Void> deleteMemo(
            @PathVariable Long scheduleId,
            @PathVariable Long memoId,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        scheduleService.deleteMemo(scheduleId, memoId, authentication.getId());

        return ResponseEntity.ok().build();
    }

    // 체크리스트
    @ApiOperation("체크리스트 생성")
    @PostMapping("/{scheduleId}/checklists")
    public ResponseEntity<IdResponse> saveChecklist(
            @PathVariable Long scheduleId,
            @RequestBody @Valid ChecklistCreationRequest checklistCreationRequest,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        Long savedId = scheduleService.saveChecklist(scheduleId, checklistCreationRequest, authentication.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(new IdResponse(savedId));
    }

    @ApiOperation("일정에 속한 체크리스트 목록 조회")
    @GetMapping("/{scheduleId}/checklists")
    public ResponseEntity<List<ChecklistResponse>> getChecklists(@PathVariable Long scheduleId, @AuthenticationPrincipal JwtAuthentication authentication) {
        List<ChecklistResponse> checklistResponses = scheduleService.getChecklists(scheduleId, authentication.getId());

        return ResponseEntity.ok(checklistResponses);
    }

    @ApiOperation("체크리스트 선택 및 해제")
    @PatchMapping("/{scheduleId}/checklists/{checklistId}")
    public ResponseEntity<Void> doCheck(
            @PathVariable Long scheduleId,
            @PathVariable Long checklistId,
            @RequestParam boolean flag,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        scheduleService.doCheck(scheduleId, checklistId, authentication.getId(), flag);

        return ResponseEntity.ok().build();
    }

    @ApiOperation("체크리스트 삭제")
    @DeleteMapping("/{scheduleId}/checklists/{checklistId}")
    public ResponseEntity<Void> modifyChecklist(
            @PathVariable Long scheduleId,
            @PathVariable Long checklistId,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        scheduleService.deleteChecklist(scheduleId, checklistId, authentication.getId());

        return ResponseEntity.ok().build();
    }

    // 투표
    @ApiOperation("투표 생성")
    @PostMapping("/{scheduleId}/votings")
    public ResponseEntity<IdResponse> createVoting(
            @PathVariable Long scheduleId,
            @RequestBody @Valid VotingCreationRequest votingCreationRequest,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        Long savedId = scheduleService.saveVoting(scheduleId, votingCreationRequest, authentication.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(new IdResponse(savedId));
    }

    @ApiOperation("일정에 속한 투표 목록 조회")
    @GetMapping("/{scheduleId}/votings")
    public ResponseEntity<List<VotingSimpleResponse>> getVotingList(
            @PathVariable Long scheduleId,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        List<VotingSimpleResponse> votingSimpleResponses = scheduleService.getVotingList(scheduleId, authentication.getId());

        return ResponseEntity.ok(votingSimpleResponses);
    }

    @ApiOperation("투표 상세 조회")
    @GetMapping("/{scheduleId}/votings/{votingId}")
    public ResponseEntity<VotingDetailResponse> getVoting(
            @PathVariable Long scheduleId,
            @PathVariable Long votingId,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        VotingDetailResponse votingDetailResponse = scheduleService.getVoting(scheduleId, votingId, authentication.getId());

        return ResponseEntity.ok(votingDetailResponse);
    }

    @ApiOperation("투표 행사")
    @PatchMapping("/{scheduleId}/votings/{votingId}")
    public ResponseEntity<Void> doVote(
            @PathVariable Long scheduleId,
            @PathVariable Long votingId,
            @RequestBody @Valid VotingRequest votingRequest,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        scheduleService.doVote(scheduleId, votingId, votingRequest, authentication.getId());

        return ResponseEntity.ok().build();
    }

    @ApiOperation("투표 삭제")
    @DeleteMapping("/{scheduleId}/votings/{votingId}")
    public ResponseEntity<Void> deleteVoting(
            @PathVariable Long scheduleId,
            @PathVariable Long votingId,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        scheduleService.deleteVoting(scheduleId, votingId, authentication.getId());

        return ResponseEntity.ok().build();
    }

    // 여행 멤버
    @ApiOperation("여행 멤버 추가")
    @PostMapping("/{scheduleId}/members")
    public ResponseEntity<Void> addScheduleMember(
            @PathVariable Long scheduleId,
            @Valid @RequestBody ScheduleMemberRequest scheduleMemberRequest,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        scheduleService.addScheduleMember(scheduleId, scheduleMemberRequest, authentication.getId());

        return ResponseEntity.ok().build();
    }

    @ApiOperation("여행 멤버 목록 조회")
    @GetMapping("/{scheduleId}/members")
    public ResponseEntity<List<MemberSimpleResponse>> getScheduleMembers(
            @PathVariable Long scheduleId,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        List<MemberSimpleResponse> memberSimpleResponses = scheduleService.getScheduleMembers(scheduleId, authentication.getId());

        return ResponseEntity.ok(memberSimpleResponses);
    }

    @ApiOperation("여행 멤버 제외")
    @DeleteMapping("/{scheduleId}/members/{memberId}")
    public ResponseEntity<Void> deleteScheduleMember(
            @PathVariable Long scheduleId,
            @PathVariable(name = "memberId") Long deletedId,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        scheduleService.deleteScheduleMember(scheduleId, deletedId, authentication.getId());

        return ResponseEntity.ok().build();
    }

    @ApiOperation("여행에서 나가기")
    @DeleteMapping("/{scheduleId}/members/exit")
    public ResponseEntity<Void> exitSchedule(@PathVariable Long scheduleId, @AuthenticationPrincipal JwtAuthentication authentication) {
        scheduleService.exitSchedule(scheduleId, authentication.getId());

        return ResponseEntity.ok().build();
    }

}
