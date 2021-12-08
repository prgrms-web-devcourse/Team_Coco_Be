package com.cocodan.triplan.schedule.service;

import com.cocodan.triplan.converter.ScheduleConverter;
import com.cocodan.triplan.schedule.domain.*;
import com.cocodan.triplan.schedule.dto.request.*;
import com.cocodan.triplan.schedule.dto.response.ScheduleDetail;
import com.cocodan.triplan.schedule.dto.response.ScheduleSimple;
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
    public Long createSchedule(ScheduleCreation scheduleCreation, Long memberId) {
        Schedule schedule = scheduleConverter.convertSchedule(scheduleCreation, memberId);

        return scheduleRepository.save(schedule).getId();
    }

    @Transactional(readOnly = true)
    public List<ScheduleSimple> getSchedules(Long memberId) {
        return scheduleRepository.findByMemberId(memberId)
                .stream()
                .map(scheduleConverter::convertScheduleSimple)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ScheduleDetail getSchedule(Long scheduleId) {
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
    public void modifySchedule(Long scheduleId, ScheduleModification scheduleModification) {
        Schedule updated = scheduleRepository.findById(scheduleId)
                .map(schedule -> {
                    schedule.removeAllSpots();
                    scheduleConverter.convertDailyScheduleSpots(schedule, scheduleModification);
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
    public Long createMemo(Long scheduleId, MemoCreation memoCreation, Long memberId) {
        Memo memo = scheduleRepository.findById(scheduleId)
                .map(schedule -> getMemo(memoCreation, schedule, memberId))
                .orElseThrow(() -> new RuntimeException(""));

        return memoRepository.save(memo).getId();
    }
    private Memo getMemo(MemoCreation memoCreation, Schedule schedule, Long memberId) {
        return Memo.builder()
                .schedule(schedule)
                .content(memoCreation.getContent())
                .memberId(memberId)
                .build();
    }

    // 체크리스트

    @Transactional
    public Long createChecklist(Long scheduleId, ChecklistCreation checklistCreation) {
        Checklist checklist = scheduleRepository.findById(scheduleId)
                .map(schedule -> getChecklist(checklistCreation, schedule))
                .orElseThrow(() -> new RuntimeException(""));

        return checklistRepository.save(checklist).getId();
    }
    private Checklist getChecklist(ChecklistCreation checklistCreation, Schedule schedule) {
        return Checklist.builder()
                .content(checklistCreation.getContent())
                .schedule(schedule)
                .date(checklistCreation.getDate())
                .build();
    }

    // 투표

    @Transactional
    public Long createVoting(Long scheduleId, VotingCreation votingCreation, Long memberId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException(""));

        Voting voting = Voting.builder()
                .schedule(schedule)
                .title(votingCreation.getTitle())
                .memberId(memberId)
                .build();

        votingCreation.getContents().stream()
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
