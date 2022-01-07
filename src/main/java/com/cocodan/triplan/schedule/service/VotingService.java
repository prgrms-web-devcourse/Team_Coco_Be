package com.cocodan.triplan.schedule.service;

import com.cocodan.triplan.exception.common.ForbiddenException;
import com.cocodan.triplan.exception.common.NotFoundException;
import com.cocodan.triplan.exception.common.NotIncludeException;
import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.member.service.MemberService;
import com.cocodan.triplan.schedule.domain.Schedule;
import com.cocodan.triplan.schedule.domain.Voting;
import com.cocodan.triplan.schedule.domain.VotingContent;
import com.cocodan.triplan.schedule.dto.request.VotingCreationRequest;
import com.cocodan.triplan.schedule.dto.request.VotingRequest;
import com.cocodan.triplan.schedule.dto.response.VotingResponse;
import com.cocodan.triplan.schedule.repository.VotingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VotingService {

    private final VotingRepository votingRepository;

    private final ScheduleService scheduleService;

    private final MemberService memberService;

    @Transactional
    public Long saveVoting(Long scheduleId, VotingCreationRequest votingCreationRequest, Long memberId) {
        Schedule schedule = scheduleService.findScheduleById(scheduleId);

        scheduleService.validateScheduleMember(scheduleId, memberId);

        Voting voting = createVoting(votingCreationRequest, memberId, schedule);

        createVotingContents(votingCreationRequest, voting);

        return votingRepository.save(voting).getId();
    }

    @Transactional(readOnly = true)
    public List<VotingResponse> getVotingList(Long scheduleId, Long memberId) {
        scheduleService.validateScheduleMember(scheduleId, memberId);

        return votingRepository.findByScheduleId(scheduleId)
                .stream()
                .map(VotingResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VotingResponse getVoting(Long scheduleId, Long votingId, Long memberId) {
        Voting voting = findVotingById(votingId);

        validateScheduleVoting(scheduleId, votingId);

        scheduleService.validateScheduleMember(scheduleId, memberId);

        Long ownerId = voting.getMemberId();

        Member owner = memberService.findMemberById(ownerId);

        return VotingResponse.of(voting, owner, memberId);
    }

    @Transactional
    public void doVote(Long scheduleId, Long votingId, VotingRequest votingRequest, Long memberId) {
        Voting voting = findVotingById(votingId);

        validateScheduleVoting(scheduleId, votingId);

        scheduleService.validateScheduleMember(scheduleId, memberId);

        voting.vote(votingRequest.getVotingMap(), memberId);
    }

    @Transactional
    public void deleteVoting(Long scheduleId, Long votingId, Long memberId) {
        Voting voting = findVotingById(votingId);

        validateScheduleVoting(scheduleId, votingId);

        validateVotingOwner(voting, memberId);

        votingRepository.delete(voting);
    }

    private Voting createVoting(VotingCreationRequest votingCreationRequest, Long memberId, Schedule schedule) {
        return Voting.builder()
                .schedule(schedule)
                .title(votingCreationRequest.getTitle())
                .multipleFlag(votingCreationRequest.isMultipleFlag())
                .memberId(memberId)
                .build();
    }

    private void createVotingContents(VotingCreationRequest votingCreationRequest, Voting voting) {
        votingCreationRequest.getContents()
                .forEach(content -> createVotingContent(voting, content));
    }

    private void createVotingContent(Voting voting, String votingContentName) {
        VotingContent.builder()
                .voting(voting)
                .content(votingContentName)
                .build();
    }

    private void validateScheduleVoting(Long scheduleId, Long votingId) {
        boolean notExists = !votingRepository.existsByIdAndScheduleId(votingId, scheduleId);

        if (notExists) {
            throw new NotIncludeException(Schedule.class, Voting.class, scheduleId, votingId);
        }
    }

    private Voting findVotingById(Long votingId) {
        return votingRepository.findById(votingId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format(
                        "{} :: Voting Not Found. " +
                                "There does not exist a Voting with the given ID : {}",
                        LocalDateTime.now(), votingId
                )));
    }

    private void validateVotingOwner(Voting voting, Long memberId) {
        if (!voting.getMemberId().equals(memberId)) {
            throw new ForbiddenException(Voting.class, voting.getId(), memberId);
        }
    }
}
