package com.cocodan.triplan.schedule.controller;

import com.cocodan.triplan.common.ApiResponse;
import com.cocodan.triplan.jwt.JwtAuthentication;
import com.cocodan.triplan.member.dto.response.MemberSimpleResponse;
import com.cocodan.triplan.schedule.dto.request.*;
import com.cocodan.triplan.schedule.dto.response.*;
import com.cocodan.triplan.schedule.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    private final MemoService memoService;

    private final VotingService votingService;

    private final ChecklistService checklistService;

    private final ScheduleMemberService scheduleMemberService;

    // 일정
    @ApiOperation("일정 생성")
    @PostMapping()
    public ApiResponse<IdResponse> createSchedule(
            @Valid @RequestBody ScheduleCreationRequest scheduleCreationRequest,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        Long savedId = scheduleService.saveSchedule(scheduleCreationRequest, authentication.getId());

        return ApiResponse.created(new IdResponse(savedId));
    }

    @ApiOperation("회원이 속한 일정 목록 조회")
    @GetMapping()
    public ApiResponse<List<ScheduleResponse>> getSchedules(
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        List<ScheduleResponse> schedules = scheduleService.getSchedules(authentication.getId());

        return ApiResponse.ok(schedules);
    }

    @ApiOperation("일정 상세 조회")
    @GetMapping("/{scheduleId}")
    public ApiResponse<ScheduleResponse> getSchedule(@PathVariable Long scheduleId) {
        ScheduleResponse schedule = scheduleService.getSchedule(scheduleId);

        return ApiResponse.ok(schedule);
    }

    @ApiOperation("일정 수정")
    @PutMapping("/{scheduleId}")
    public ApiResponse<Void> modifySchedule(
            @PathVariable Long scheduleId,
            @RequestBody @Valid ScheduleModificationRequest scheduleModificationRequest,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        scheduleService.modifySchedule(scheduleId, scheduleModificationRequest, authentication.getId());

        return ApiResponse.ok();
    }

    @ApiOperation("일정 삭제")
    @DeleteMapping("/{scheduleId}")
    public ApiResponse<Void> deleteSchedule(
            @PathVariable Long scheduleId,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        scheduleService.deleteSchedule(scheduleId, authentication.getId());

        return ApiResponse.ok();
    }

    // 메모
    @ApiOperation("메모 생성")
    @PostMapping("/{scheduleId}/memos")
    public ApiResponse<IdResponse> createMemo(
            @PathVariable Long scheduleId,
            @RequestBody @Valid MemoRequest memoRequest,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        Long savedId = memoService.saveMemo(scheduleId, memoRequest, authentication.getId());

        return ApiResponse.created(new IdResponse(savedId));
    }

    @ApiOperation("일정에 속한 메모 목록 조회")
    @GetMapping("/{scheduleId}/memos")
    public ApiResponse<List<MemoResponse>> getMemos(
            @PathVariable Long scheduleId,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        List<MemoResponse> memos = memoService.getMemos(scheduleId, authentication.getId());

        return ApiResponse.ok(memos);
    }

    @ApiOperation("메모 상세 조회")
    @GetMapping("/{scheduleId}/memos/{memoId}")
    public ApiResponse<MemoResponse> getMemo(
            @PathVariable Long scheduleId,
            @PathVariable Long memoId,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        MemoResponse memoResponse = memoService.getMemo(scheduleId, memoId, authentication.getId());

        return ApiResponse.ok(memoResponse);
    }

    @ApiOperation("메모 수정")
    @PutMapping("/{scheduleId}/memos/{memoId}")
    public ApiResponse<Void> modifyMemo(
            @PathVariable Long scheduleId,
            @PathVariable Long memoId,
            @RequestBody @Valid MemoRequest memoRequest,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        memoService.modifyMemo(scheduleId, memoId, memoRequest, authentication.getId());

        return ApiResponse.ok();
    }

    @ApiOperation("메모 삭제")
    @DeleteMapping("/{scheduleId}/memos/{memoId}")
    public ApiResponse<Void> deleteMemo(
            @PathVariable Long scheduleId,
            @PathVariable Long memoId,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        memoService.deleteMemo(scheduleId, memoId, authentication.getId());

        return ApiResponse.ok();
    }

    // 체크리스트
    @ApiOperation("체크리스트 생성")
    @PostMapping("/{scheduleId}/checklists")
    public ApiResponse<IdResponse> saveChecklist(
            @PathVariable Long scheduleId,
            @RequestBody @Valid ChecklistCreationRequest checklistCreationRequest,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        Long savedId = checklistService.saveChecklist(scheduleId, checklistCreationRequest, authentication.getId());

        return ApiResponse.created(new IdResponse(savedId));
    }

    @ApiOperation("일정에 속한 체크리스트 목록 조회")
    @GetMapping("/{scheduleId}/checklists")
    public ApiResponse<List<ChecklistResponse>> getChecklists(
            @PathVariable Long scheduleId,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        List<ChecklistResponse> checklistResponses = checklistService.getChecklists(scheduleId, authentication.getId());

        return ApiResponse.ok(checklistResponses);
    }

    @ApiOperation("체크리스트 선택 및 해제")
    @PatchMapping("/{scheduleId}/checklists/{checklistId}")
    public ApiResponse<Void> doCheck(
            @PathVariable Long scheduleId,
            @PathVariable Long checklistId,
            @RequestParam boolean flag,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        checklistService.doCheck(scheduleId, checklistId, authentication.getId(), flag);

        return ApiResponse.ok();
    }

    @ApiOperation("체크리스트 삭제")
    @DeleteMapping("/{scheduleId}/checklists/{checklistId}")
    public ApiResponse<Void> modifyChecklist(
            @PathVariable Long scheduleId,
            @PathVariable Long checklistId,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        checklistService.deleteChecklist(scheduleId, checklistId, authentication.getId());

        return ApiResponse.ok();
    }

    // 투표
    @ApiOperation("투표 생성")
    @PostMapping("/{scheduleId}/votings")
    public ApiResponse<IdResponse> createVoting(
            @PathVariable Long scheduleId,
            @RequestBody @Valid VotingCreationRequest votingCreationRequest,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        Long savedId = votingService.saveVoting(scheduleId, votingCreationRequest, authentication.getId());

        return ApiResponse.created(new IdResponse(savedId));
    }

    @ApiOperation("일정에 속한 투표 목록 조회")
    @GetMapping("/{scheduleId}/votings")
    public ApiResponse<List<VotingResponse>> getVotingList(
            @PathVariable Long scheduleId,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        List<VotingResponse> votingSimpleResponses =
                votingService.getVotingList(scheduleId, authentication.getId());

        return ApiResponse.ok(votingSimpleResponses);
    }

    @ApiOperation("투표 상세 조회")
    @GetMapping("/{scheduleId}/votings/{votingId}")
    public ApiResponse<VotingResponse> getVoting(
            @PathVariable Long scheduleId,
            @PathVariable Long votingId,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        VotingResponse votingResponse =
                votingService.getVoting(scheduleId, votingId, authentication.getId());

        return ApiResponse.ok(votingResponse);
    }

    @ApiOperation("투표 행사")
    @PatchMapping("/{scheduleId}/votings/{votingId}")
    public ApiResponse<Void> doVote(
            @PathVariable Long scheduleId,
            @PathVariable Long votingId,
            @RequestBody @Valid VotingRequest votingRequest,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        votingService.doVote(scheduleId, votingId, votingRequest, authentication.getId());

        return ApiResponse.ok();
    }

    @ApiOperation("투표 삭제")
    @DeleteMapping("/{scheduleId}/votings/{votingId}")
    public ApiResponse<Void> deleteVoting(
            @PathVariable Long scheduleId,
            @PathVariable Long votingId,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        votingService.deleteVoting(scheduleId, votingId, authentication.getId());

        return ApiResponse.ok();
    }

    // 여행 멤버
    @ApiOperation("여행 멤버 추가")
    @PostMapping("/{scheduleId}/members")
    public ApiResponse<Void> addScheduleMember(
            @PathVariable Long scheduleId,
            @Valid @RequestBody ScheduleMemberRequest scheduleMemberRequest,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        scheduleMemberService.addScheduleMember(scheduleId, scheduleMemberRequest, authentication.getId());

        return ApiResponse.ok();
    }

    @ApiOperation("여행 멤버 목록 조회")
    @GetMapping("/{scheduleId}/members")
    public ApiResponse<List<MemberSimpleResponse>> getScheduleMembers(
            @PathVariable Long scheduleId,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        List<MemberSimpleResponse> memberSimpleResponses =
                scheduleMemberService.getScheduleMembers(scheduleId, authentication.getId());

        return ApiResponse.ok(memberSimpleResponses);
    }

    @ApiOperation("여행 멤버 제외")
    @DeleteMapping("/{scheduleId}/members/{memberId}")
    public ApiResponse<Void> deleteScheduleMember(
            @PathVariable Long scheduleId,
            @PathVariable(name = "memberId") Long deletedId,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        scheduleMemberService.deleteScheduleMember(scheduleId, deletedId, authentication.getId());

        return ApiResponse.ok();
    }

    @ApiOperation("여행에서 나가기")
    @DeleteMapping("/{scheduleId}/members/exit")
    public ApiResponse<Void> exitSchedule(
            @PathVariable Long scheduleId,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        scheduleMemberService.exitSchedule(scheduleId, authentication.getId());

        return ApiResponse.ok();
    }
}
