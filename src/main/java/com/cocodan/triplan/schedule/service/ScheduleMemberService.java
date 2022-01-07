package com.cocodan.triplan.schedule.service;

import com.cocodan.triplan.exception.common.NoFriendsException;
import com.cocodan.triplan.friend.repository.FriendRepository;
import com.cocodan.triplan.member.dto.response.MemberSimpleResponse;
import com.cocodan.triplan.member.repository.MemberRepository;
import com.cocodan.triplan.schedule.domain.Schedule;
import com.cocodan.triplan.schedule.domain.ScheduleMember;
import com.cocodan.triplan.schedule.dto.request.ScheduleCreationRequest;
import com.cocodan.triplan.schedule.dto.request.ScheduleMemberRequest;
import com.cocodan.triplan.schedule.repository.ScheduleRepository;
import com.cocodan.triplan.util.ExceptionMessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleMemberService {

    private final ScheduleRepository scheduleRepository;

    private final MemberRepository memberRepository;

    private final FriendRepository friendRepository;

    private final ScheduleService scheduleService;

    // 여행 멤버
    @Transactional
    public void addScheduleMember(Long scheduleId, ScheduleMemberRequest scheduleMemberRequest, Long memberId) {
        Schedule schedule = scheduleService.findScheduleById(scheduleId);

        scheduleService.validateScheduleMember(scheduleId, memberId);

        long friendId = scheduleMemberRequest.getFriendId();

        validateFriends(friendId, memberId);

        createScheduleMember(schedule, friendId);
    }

    @Transactional(readOnly = true)
    public List<MemberSimpleResponse> getScheduleMembers(Long scheduleId, Long memberId) {
        Schedule schedule = scheduleService.findScheduleById(scheduleId);

        scheduleService.validateScheduleMember(scheduleId, memberId);

        List<Long> ids = schedule.getScheduleMembers().stream()
                .map(ScheduleMember::getMemberId)
                .collect(Collectors.toList());

        return memberRepository.findByIdIn(ids)
                .stream()
                .map(MemberSimpleResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteScheduleMember(Long scheduleId, Long deletedId, Long memberId) {
        Schedule schedule = scheduleService.findScheduleById(scheduleId);

        scheduleService.validateScheduleMember(scheduleId, memberId);

        if (scheduleService.isConstructor(schedule, deletedId)) {
            throw new IllegalArgumentException(ExceptionMessageUtils.getMessage("exception.bad_request"));
        }

        ScheduleMember deletedMember = findDeletedMember(schedule, deletedId);

        schedule.deleteScheduleMember(deletedMember);
    }

    @Transactional
    public void exitSchedule(Long scheduleId, Long memberId) {
        Schedule schedule = scheduleService.findScheduleById(scheduleId);

        scheduleService.validateScheduleMember(scheduleId, memberId);

        if (memberId.equals(schedule.getMemberId())) {
            scheduleRepository.delete(schedule);
            return;
        }

        ScheduleMember deletedMember = findDeletedMember(schedule, memberId);

        schedule.deleteScheduleMember(deletedMember);
    }

    private void createScheduleMember(Schedule schedule, long friendId) {
        ScheduleMember.builder()
                .memberId(friendId)
                .schedule(schedule)
                .build();
    }

    private void validateFriends(long friendId, Long memberId) {
        friendRepository.findByFromIdAndToId(memberId, friendId)
                .orElseThrow(() -> new NoFriendsException(friendId, memberId));
    }

    private ScheduleMember findDeletedMember(Schedule schedule, Long deletedId) {
        return schedule.getScheduleMembers().stream()
                .filter(scheduleMember -> scheduleMember.getMemberId().equals(deletedId))
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                ExceptionMessageUtils.getMessage("exception.bad_request")
                        )
                );
    }
}
