package com.cocodan.triplan.schedule.service;

import com.cocodan.triplan.exception.common.ForbiddenException;
import com.cocodan.triplan.exception.common.NotFoundException;
import com.cocodan.triplan.exception.common.NotIncludeException;
import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.member.service.MemberService;
import com.cocodan.triplan.schedule.ScheduleConverter;
import com.cocodan.triplan.schedule.domain.Memo;
import com.cocodan.triplan.schedule.domain.Schedule;
import com.cocodan.triplan.schedule.dto.request.MemoRequest;
import com.cocodan.triplan.schedule.dto.response.MemoResponse;
import com.cocodan.triplan.schedule.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;

    private final ScheduleService scheduleService;

    private final MemberService memberService;

    private final ScheduleConverter converter;

    @Transactional
    public Long saveMemo(Long scheduleId, MemoRequest memoRequest, Long memberId) {
        Schedule schedule = scheduleService.findScheduleById(scheduleId);

        scheduleService.validateScheduleMember(scheduleId, memberId);

        Memo memo = converter.createMemo(memoRequest, schedule, memberId);

        return memoRepository.save(memo).getId();
    }

    @Transactional(readOnly = true)
    public List<MemoResponse> getMemos(Long scheduleId, Long memberId) {
        scheduleService.validateScheduleMember(scheduleId, memberId);

        return memoRepository.findByScheduleId(scheduleId)
                .stream()
                .map(MemoResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MemoResponse getMemo(Long scheduleId, Long memoId, Long memberId) {
        Memo memo = findMemoById(memoId);

        validateScheduleMemo(scheduleId, memoId);

        scheduleService.validateScheduleMember(scheduleId, memberId);

        Long ownerId = memo.getMemberId();

        Member member = memberService.findMemberById(ownerId);

        return MemoResponse.of(memo, member);
    }

    @Transactional
    public void modifyMemo(Long scheduleId, Long memoId, MemoRequest memoRequest, Long memberId) {
        Memo memo = findMemoById(memoId);

        validateScheduleMemo(scheduleId, memoId);

        validateMemoOwner(memo, memberId);

        memo.modify(memoRequest.getTitle(), memoRequest.getContent());
    }

    @Transactional
    public void deleteMemo(Long scheduleId, Long memoId, Long memberId) {
        Memo memo = findMemoById(memoId);

        validateScheduleMemo(scheduleId, memoId);

        validateMemoOwner(memo, memberId);

        memoRepository.delete(memo);
    }

    private void validateScheduleMemo(Long scheduleId, Long memoId) {
        boolean notExists = !memoRepository.existsByIdAndScheduleId(memoId, scheduleId);

        if (notExists) {
            throw new NotIncludeException(Schedule.class, Memo.class, scheduleId, memoId);
        }
    }

    private Memo findMemoById(Long memoId) {
        return memoRepository.findById(memoId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format(
                        "{} :: Memo Not Found. " +
                                "There does not exist a Memo with the given ID : {}",
                        LocalDateTime.now(), memoId
                )));
    }

    private void validateMemoOwner(Memo memo, Long memberId) {
        if (!memo.getMemberId().equals(memberId)) {
            throw new ForbiddenException(Memo.class, memo.getId(), memberId);
        }
    }
}
