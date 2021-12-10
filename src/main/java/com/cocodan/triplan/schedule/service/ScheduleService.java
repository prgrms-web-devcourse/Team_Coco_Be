package com.cocodan.triplan.schedule.service;

import com.cocodan.triplan.converter.ScheduleConverter;
import com.cocodan.triplan.schedule.domain.*;
import com.cocodan.triplan.schedule.dto.request.*;
import com.cocodan.triplan.schedule.dto.response.ScheduleDetailResponse;
import com.cocodan.triplan.schedule.dto.response.ScheduleSimpleResponse;
import com.cocodan.triplan.schedule.repository.ChecklistRepository;
import com.cocodan.triplan.schedule.repository.MemoRepository;
import com.cocodan.triplan.schedule.repository.ScheduleRepository;
import com.cocodan.triplan.schedule.repository.VotingRepository;
import com.cocodan.triplan.spot.domain.Spot;
import com.cocodan.triplan.spot.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final SpotRepository spotRepository;
    private final ScheduleConverter scheduleConverter;

    private final MemoRepository memoRepository;
    private final ChecklistRepository checklistRepository;
    private final VotingRepository votingRepository;

    @Transactional
    public Long createSchedule(ScheduleCreationRequest scheduleCreationRequest, Long memberId) {
        Schedule schedule = scheduleConverter.convertSchedule(scheduleCreationRequest, memberId);

        return scheduleRepository.save(schedule).getId();
    }

    @Transactional(readOnly = true)
    public List<ScheduleSimpleResponse> getSchedules(Long memberId) {
        return scheduleRepository.findByMemberId(memberId)
                .stream()
                .map(scheduleConverter::convertScheduleSimple)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ScheduleDetailResponse getSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findOneWithSpotsById(scheduleId)
                .orElseThrow(() -> new RuntimeException(""));

        List<Spot> spots = spotRepository.findByIdIn(getSpotIds(schedule));

        //TODO : 멤버 Repository 로 imageURl 가져오기
        return scheduleConverter.convertScheduleDetail(schedule, spots, null);
    }

    private List<Long> getSpotIds(Schedule schedule) {
        return schedule.getDailyScheduleSpots().stream()
                .map(DailyScheduleSpot::getSpotId)
                .collect(Collectors.toList());
    }

    @Transactional
    public void modifySchedule(Long scheduleId, ScheduleModificationRequest scheduleModificationRequest) {
        Schedule updated = scheduleRepository.findById(scheduleId)
                .map(schedule -> {
                    schedule.removeAllSpots();
                    scheduleConverter.convertDailyScheduleSpotList(schedule, scheduleModificationRequest);
                    return schedule;
                })
                .orElseThrow(() -> new RuntimeException(""));

        scheduleRepository.save(updated);
    }

    @Transactional
    public void deleteSchedule(Long scheduleId, Long memberId) {
        scheduleRepository.findById(scheduleId)
                .filter(schedule -> isOwner(schedule, memberId))
                .ifPresent(scheduleRepository::delete);
    }

    private boolean isOwner(Schedule schedule, Long memberId) {
        return schedule.getMemberId().equals(memberId);
    }

    // 메모
    @Transactional
    public Long createMemo(Long scheduleId, MemoRequest memoRequest, Long memberId) {
        Memo memo = scheduleRepository.findById(scheduleId)
                .map(schedule -> getMemo(memoRequest, schedule, memberId))
                .orElseThrow(() -> new RuntimeException(""));

        return memoRepository.save(memo).getId();
    }

    private Memo getMemo(MemoRequest memoRequest, Schedule schedule, Long memberId) {
        return Memo.builder()
                .schedule(schedule)
                .content(memoRequest.getContent())
                .memberId(memberId)
                .build();
    }

    @Transactional
    public void modifyMemo(Long scheduleId, Long memoId, MemoRequest memoRequest, Long memberId) {
        validateScheduleMember(scheduleId, memberId);

        memoRepository.findById(memoId)
                .ifPresentOrElse(
                        memo -> memo.modifyContent(memoRequest.getContent()),
                        () -> {
                            throw new RuntimeException("");
                        }
                );
    }

    private void validateScheduleMember(Long scheduleId, Long memberId) {
        scheduleRepository.findById(scheduleId)
                .map(Schedule::getScheduleMembers)
                .map(this::getMemberIds)
                .filter(longs -> longs.contains(memberId))
                .orElseThrow(() -> new RuntimeException(""));
    }

    @Transactional
    public void deleteMemo(Long scheduleId, Long memoId, Long memberId) {
        validateScheduleMember(scheduleId, memberId);

        memoRepository.deleteById(memoId);
    }

    private List<Long> getMemberIds(List<ScheduleMember> scheduleMembers) {
        return scheduleMembers.stream()
                .map(ScheduleMember::getMemberId)
                .collect(Collectors.toList());
    }

    // 체크리스트
    @Transactional
    public Long createChecklist(Long scheduleId, ChecklistCreationRequest checklistCreationRequest) {
        Checklist checklist = scheduleRepository.findById(scheduleId)
                .map(schedule -> getChecklist(checklistCreationRequest, schedule))
                .orElseThrow(() -> new RuntimeException(""));

        return checklistRepository.save(checklist).getId();
    }

    private Checklist getChecklist(ChecklistCreationRequest checklistCreationRequest, Schedule schedule) {
        return Checklist.builder()
                .content(checklistCreationRequest.getContent())
                .schedule(schedule)
                .date(checklistCreationRequest.getDate())
                .build();
    }

    @Transactional
    public void doCheck(Long scheduleId, Long checklistId, Long memberId, boolean flag) {
        validateScheduleMember(scheduleId, memberId);

        Checklist checklist = checklistRepository.findById(checklistId)
                .orElseThrow(() -> new RuntimeException(""));

        checklist.check(flag);
    }

    @Transactional
    public void deleteChecklist(Long scheduleId, Long checklistId, Long memberId) {
        validateScheduleMember(scheduleId, memberId);

        checklistRepository.deleteById(checklistId);
    }


    // 투표
    @Transactional
    public Long createVoting(Long scheduleId, VotingCreationRequest votingCreationRequest, Long memberId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException(""));

        Voting voting = createVoting(votingCreationRequest, memberId, schedule);

        createVotingContents(votingCreationRequest, voting);

        return votingRepository.save(voting).getId();
    }

    private void createVotingContents(VotingCreationRequest votingCreationRequest, Voting voting) {
        votingCreationRequest.getContents().stream()
                .map(v -> getVotingContent(voting, v))
                .collect(Collectors.toList());
    }

    private Voting createVoting(VotingCreationRequest votingCreationRequest, Long memberId, Schedule schedule) {
        return Voting.builder()
                .schedule(schedule)
                .title(votingCreationRequest.getTitle())
                .memberId(memberId)
                .build();
    }

    private VotingContent getVotingContent(Voting voting, String v) {
        return VotingContent.builder()
                .content(v)
                .voting(voting)
                .build();
    }

    @Transactional
    public void deleteVoting(Long scheduleId, Long votingId, Long memberId) {
        validateScheduleMember(scheduleId, memberId);

        votingRepository.deleteById(votingId);
    }

    public void doVote(Long scheduleId, Long votingId, VotingRequest votingRequest, Long memberId) {
        validateScheduleMember(scheduleId, memberId);

        Voting voting = votingRepository.findById(votingId)
                .orElseThrow(() -> new RuntimeException(""));

        voting.vote(votingRequest.getVotingMap(), memberId);
    }
}
