package com.cocodan.triplan.schedule.service;

import com.cocodan.triplan.exception.common.ForbiddenException;
import com.cocodan.triplan.exception.common.NoFriendsException;
import com.cocodan.triplan.exception.common.NotFoundException;
import com.cocodan.triplan.exception.common.NotIncludeException;
import com.cocodan.triplan.friend.repository.FriendRepository;
import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.member.dto.response.MemberSimpleResponse;
import com.cocodan.triplan.member.repository.MemberRepository;
import com.cocodan.triplan.schedule.domain.*;
import com.cocodan.triplan.schedule.domain.vo.Theme;
import com.cocodan.triplan.schedule.dto.request.*;
import com.cocodan.triplan.schedule.dto.response.*;
import com.cocodan.triplan.schedule.repository.ChecklistRepository;
import com.cocodan.triplan.schedule.repository.MemoRepository;
import com.cocodan.triplan.schedule.repository.ScheduleRepository;
import com.cocodan.triplan.schedule.repository.VotingRepository;
import com.cocodan.triplan.spot.domain.Spot;
import com.cocodan.triplan.spot.service.SpotService;
import com.cocodan.triplan.util.ExceptionMessageUtils;
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

    @Transactional
    public Long saveSchedule(ScheduleCreationRequest scheduleCreationRequest, Long memberId) {
        Schedule schedule = convertSchedule(scheduleCreationRequest, memberId);

        createOwner(schedule, memberId);

        scheduleCreationRequest.getDailyScheduleSpotCreationRequests()
                .forEach(this::saveSpot);

        Schedule save = scheduleRepository.save(schedule);

        addScheduleMember(scheduleCreationRequest, save);

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
        createScheduleThemes(scheduleModificationRequest.getThemes(), schedule);

        schedule.removeAllSpots();
        convertDailyScheduleSpotList(schedule, scheduleModificationRequest);
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

    private Schedule convertSchedule(ScheduleCreationRequest scheduleCreationRequest, Long memberId) {
        Schedule schedule = createSchedule(scheduleCreationRequest, memberId);

        createScheduleThemes(scheduleCreationRequest.getThemes(), schedule);

        createScheduleDailySpots(scheduleCreationRequest, schedule);

        return schedule;
    }

    private void addScheduleMember(ScheduleCreationRequest scheduleCreationRequest, Schedule save) {
        for (Long id : scheduleCreationRequest.getIdsOfFriends()) {
            createScheduleMember(save, id);
        }
    }

    public void createScheduleMember(Schedule schedule, long friendId) {
        ScheduleMember.builder()
                .memberId(friendId)
                .schedule(schedule)
                .build();
    }

    private void createOwner(Schedule schedule, Long memberId) {
        ScheduleMember.builder()
                .schedule(schedule)
                .memberId(memberId)
                .build();
    }

    private Schedule createSchedule(ScheduleCreationRequest scheduleCreationRequest, Long memberId) {
        return Schedule.builder()
                .title(scheduleCreationRequest.getTitle())
                .startDate(scheduleCreationRequest.getStartDate())
                .endDate(scheduleCreationRequest.getEndDate())
                .memberId(memberId)
                .build();
    }

    private void createScheduleDailySpots(ScheduleCreationRequest scheduleCreationRequest, Schedule schedule) {
        scheduleCreationRequest.getDailyScheduleSpotCreationRequests()
                .forEach(request -> createDailyScheduleSpot(schedule, request));
    }

    private void createDailyScheduleSpot(
            Schedule schedule,
            DailyScheduleSpotCreationRequest dailyScheduleSpotCreationRequest
    ) {
        DailyScheduleSpot.builder()
                .spotId(dailyScheduleSpotCreationRequest.getSpotId())
                .placeName(dailyScheduleSpotCreationRequest.getPlaceName())
                .dateOrder(dailyScheduleSpotCreationRequest.getDateOrder())
                .spotOrder(dailyScheduleSpotCreationRequest.getSpotOrder())
                .schedule(schedule)
                .build();
    }

    private boolean isNotSavedSpot(DailyScheduleSpotCreationRequest dailyScheduleSpotCreationRequest) {
        return !spotService.existsById(dailyScheduleSpotCreationRequest.getSpotId());
    }

    private void saveSpot(DailyScheduleSpotCreationRequest dailyScheduleSpotCreationRequest) {
        if (isNotSavedSpot(dailyScheduleSpotCreationRequest)) {
            spotService.createSpot(dailyScheduleSpotCreationRequest);
        }
    }

    private void createScheduleThemes(List<String> themes, Schedule schedule) {
        themes.stream()
                .map(s -> Theme.from(s.toUpperCase()))
                .forEach(theme -> new ScheduleTheme(schedule, theme));
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

    private void convertDailyScheduleSpotList(
            Schedule schedule,
            ScheduleModificationRequest scheduleModificationRequest
    ) {
        scheduleModificationRequest.getDailyScheduleSpotCreationRequests()
                .forEach(request -> createDailyScheduleSpot(schedule, request));
    }
}
