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
    public Long createMemo(Long scheduleId, MemoCreationRequest memoCreationRequest, Long memberId) {
        Memo memo = scheduleRepository.findById(scheduleId)
                .map(schedule -> getMemo(memoCreationRequest, schedule, memberId))
                .orElseThrow(() -> new RuntimeException(""));

        return memoRepository.save(memo).getId();
    }
    private Memo getMemo(MemoCreationRequest memoCreationRequest, Schedule schedule, Long memberId) {
        return Memo.builder()
                .schedule(schedule)
                .content(memoCreationRequest.getContent())
                .memberId(memberId)
                .build();
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

    // 투표

    @Transactional
    public Long createVoting(Long scheduleId, VotingCreationRequest votingCreationRequest, Long memberId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException(""));

        Voting voting = Voting.builder()
                .schedule(schedule)
                .title(votingCreationRequest.getTitle())
                .memberId(memberId)
                .build();

        votingCreationRequest.getContents().stream()
                .map(v -> getVotingContent(voting, v))
                .collect(Collectors.toList());

        return votingRepository.save(voting).getId();
    }

    private VotingContent getVotingContent(Voting voting, String v) {
        return VotingContent.builder()
                .content(v)
                .voting(voting)
                .build();
    }
}
