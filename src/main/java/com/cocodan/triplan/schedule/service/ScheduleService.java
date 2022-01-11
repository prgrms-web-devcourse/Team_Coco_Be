package com.cocodan.triplan.schedule.service;

import com.cocodan.triplan.exception.common.ForbiddenException;
import com.cocodan.triplan.exception.common.NotFoundException;
import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.member.repository.MemberRepository;
import com.cocodan.triplan.schedule.ScheduleConverter;
import com.cocodan.triplan.schedule.domain.*;
import com.cocodan.triplan.schedule.dto.request.*;
import com.cocodan.triplan.schedule.dto.response.*;
import com.cocodan.triplan.schedule.repository.ScheduleRepository;
import com.cocodan.triplan.spot.domain.Spot;
import com.cocodan.triplan.spot.service.SpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final MemberRepository memberRepository;

    private final SpotService spotService;

    private final ScheduleConverter converter;

    @Transactional
    public Long saveSchedule(ScheduleCreationRequest scheduleCreationRequest, Long memberId) {
        Schedule schedule = converter.createSchedule(scheduleCreationRequest, memberId);

        scheduleCreationRequest.getDailyScheduleSpotCreationRequests()
                .forEach(this::saveSpot);

        converter.createScheduleMember(schedule, memberId);
        converter.createScheduleThemes(scheduleCreationRequest.getThemes(), schedule);
        converter.createScheduleDailySpots(scheduleCreationRequest.getDailyScheduleSpotCreationRequests(), schedule);
        converter.addAllScheduleMembers(scheduleCreationRequest, schedule);

        Schedule save = scheduleRepository.save(schedule);
        return save.getId();
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponse> getSchedules(Long memberId) {
        return scheduleRepository.findByMemberId(memberId)
                .stream()
                .map(ScheduleResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ScheduleResponse getSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findOneWithSpotsById(scheduleId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format(
                        "{} :: Schedule Not Found. " +
                                "There does not exist a Schedule with the given ID : {}",
                        LocalDateTime.now(), scheduleId
                )));

        List<Long> scheduleMemberIds = getMemberIds(schedule);

        List<Member> members = memberRepository.findByIdIn(scheduleMemberIds);

        List<Spot> spots = spotService.findByIdIn(getSpotIds(schedule));

        return ScheduleResponse.of(schedule, spots, members);
    }

    @Transactional
    public void modifySchedule(Long scheduleId, ScheduleModificationRequest scheduleModificationRequest, Long memberId) {
        Schedule schedule = findScheduleById(scheduleId);

        validateScheduleMember(scheduleId, memberId);

        scheduleModificationRequest.getDailyScheduleSpotCreationRequests()
                .forEach(this::saveSpot);

        schedule.updateTitle(scheduleModificationRequest.getTitle());

        schedule.clearThemes();
        converter.createScheduleThemes(scheduleModificationRequest.getThemes(), schedule);

        schedule.removeAllSpots();
        converter.createScheduleDailySpots(scheduleModificationRequest.getDailyScheduleSpotCreationRequests(), schedule);
    }

    @Transactional
    public void deleteSchedule(Long scheduleId, Long memberId) {
        Schedule schedule = findScheduleById(scheduleId);

        if (!isConstructor(schedule, memberId)) {
            throw new ForbiddenException(Schedule.class, schedule.getId(), memberId);
        }

        scheduleRepository.delete(schedule);
    }

    public Schedule findScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format(
                        "{} :: Schedule Not Found. " +
                                "There does not exist a Schedule with the given ID : {}",
                        LocalDateTime.now(), scheduleId
                )));
    }

    public void validateScheduleMember(Long scheduleId, Long memberId) {
        if (scheduleRepository.countByScheduleIdAndMemberId(scheduleId, memberId) == 0) {
            throw new ForbiddenException(Schedule.class, scheduleId, memberId);
        }
    }

    public boolean isConstructor(Schedule schedule, Long memberId) {
        return schedule.getMemberId().equals(memberId);
    }

    public Member findMemberById(Long ownerId) {
        return memberRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format(
                        "{} :: Member Not Found. " +
                                "There does not exist a Member with the given ID : {}",
                        LocalDateTime.now(), ownerId
                )));
    }


    private boolean isNotSavedSpot(DailyScheduleSpotCreationRequest dailyScheduleSpotCreationRequest) {
        return !spotService.existsById(dailyScheduleSpotCreationRequest.getSpotId());
    }

    private void saveSpot(DailyScheduleSpotCreationRequest dailyScheduleSpotCreationRequest) {
        if (isNotSavedSpot(dailyScheduleSpotCreationRequest)) {
            spotService.createSpot(dailyScheduleSpotCreationRequest);
        }
    }

    private List<Long> getMemberIds(Schedule schedule) {
        return schedule.getScheduleMembers().stream()
                .map(ScheduleMember::getMemberId)
                .collect(Collectors.toList());
    }

    private List<Long> getSpotIds(Schedule dailyScheduleSpots) {
        return dailyScheduleSpots.getDailyScheduleSpots().stream()
                .map(DailyScheduleSpot::getSpotId)
                .collect(Collectors.toList());
    }
}
