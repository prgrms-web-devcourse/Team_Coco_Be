package com.cocodan.triplan.schedule.service;

import com.cocodan.triplan.member.domain.Member;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final SpotService spotService;

    private final MemberRepository memberRepository;

    private final MemoRepository memoRepository;

    private final ChecklistRepository checklistRepository;

    private final VotingRepository votingRepository;

    @Transactional
    public Long saveSchedule(ScheduleCreationRequest scheduleCreationRequest, Long memberId) {
        Schedule schedule = convertSchedule(scheduleCreationRequest, memberId);

        scheduleCreationRequest.getDailyScheduleSpotCreationRequests().stream()
                .filter(this::isNotSavedSpot)
                .forEach(spotService::createSpot);

        return scheduleRepository.save(schedule).getId();
    }

    private Schedule convertSchedule(ScheduleCreationRequest scheduleCreationRequest, Long memberId) {
        Schedule schedule = createSchedule(scheduleCreationRequest, memberId);

        createScheduleThema(scheduleCreationRequest, schedule);

        createScheduleDailySpots(scheduleCreationRequest, schedule);

        return schedule;
    }

    private void createScheduleDailySpots(ScheduleCreationRequest scheduleCreationRequest, Schedule schedule) {
        scheduleCreationRequest.getDailyScheduleSpotCreationRequests()
                .forEach(dailyScheduleSpotCreationRequest -> createDailyScheduleSpot(schedule, dailyScheduleSpotCreationRequest));
    }

    private void createScheduleThema(ScheduleCreationRequest scheduleCreationRequest, Schedule schedule) {
        scheduleCreationRequest.getThemeList()
                .stream()
                .map(s -> Theme.valueOf(s.toUpperCase()))
                .forEach(theme -> new ScheduleTheme(schedule, theme));
    }

    private Schedule createSchedule(ScheduleCreationRequest scheduleCreationRequest, Long memberId) {
        return Schedule.builder()
                .title(scheduleCreationRequest.getTitle())
                .startDate(scheduleCreationRequest.getStartDate())
                .endDate(scheduleCreationRequest.getEndDate())
                .memberId(memberId)
                .build();
    }

    private DailyScheduleSpot createDailyScheduleSpot(Schedule schedule, DailyScheduleSpotCreationRequest dailyScheduleSpotCreationRequest) {
        return DailyScheduleSpot.builder()
                .spotId(dailyScheduleSpotCreationRequest.getSpotId())
                .date(dailyScheduleSpotCreationRequest.getDate())
                .order(dailyScheduleSpotCreationRequest.getOrder())
                .schedule(schedule)
                .build();
    }

    private boolean isNotSavedSpot(DailyScheduleSpotCreationRequest dailyScheduleSpotCreationRequest) {
        return !spotService.existsById(dailyScheduleSpotCreationRequest.getSpotId());
    }

    @Transactional(readOnly = true)
    public List<ScheduleSimpleResponse> getSchedules(Long memberId) {
        return scheduleRepository.findByMemberId(memberId)
                .stream()
                .map(ScheduleSimpleResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ScheduleDetailResponse getSchedule(Long scheduleId, Long memberId) {
        validateScheduleMember(scheduleId, memberId);

        Schedule schedule = scheduleRepository.findOneWithSpotsById(scheduleId)
                .orElseThrow(() -> new RuntimeException(""));

        List<Long> scheduleMemberIds = getMemberIds(schedule);

        List<Member> members = memberRepository.findByIdIn(scheduleMemberIds);

        List<Spot> spots = spotService.findByIdIn(getSpotIds(schedule));

        //TODO : 멤버 Repository 로 imageURl 가져오기
        return ScheduleDetailResponse.of(schedule, spots, members);
    }

    private void validateScheduleMember(Long scheduleId, Long memberId) {
        boolean notExists = !scheduleRepository.existsByIdAndMemberId(scheduleId, memberId);

        if (notExists) {
            throw new RuntimeException("");
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

    @Transactional
    public void modifySchedule(Long scheduleId, ScheduleModificationRequest scheduleModificationRequest) {
        scheduleRepository.findById(scheduleId)
                .map(schedule -> {
                    schedule.removeAllSpots();
                    convertDailyScheduleSpotList(schedule, scheduleModificationRequest);
                    return schedule;
                })
                .orElseThrow(() -> new RuntimeException(""));
    }

    private void convertDailyScheduleSpotList(Schedule schedule, ScheduleModificationRequest scheduleModificationRequest) {
        scheduleModificationRequest.getDailyScheduleSpotCreationRequests()
                .forEach(dailyScheduleSpotCreationRequest -> createDailyScheduleSpot(schedule, dailyScheduleSpotCreationRequest));
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
    public Long saveMemo(Long scheduleId, MemoRequest memoRequest, Long memberId) {
        validateScheduleMember(scheduleId, memberId);

        Memo memo = scheduleRepository.findById(scheduleId)
                .map(schedule -> createMemo(memoRequest, schedule, memberId))
                .orElseThrow(() -> new RuntimeException(""));

        return memoRepository.save(memo).getId();
    }

    private Memo createMemo(MemoRequest memoRequest, Schedule schedule, Long memberId) {
        return Memo.builder()
                .schedule(schedule)
                .title(memoRequest.getTitle())
                .content(memoRequest.getContent())
                .memberId(memberId)
                .build();
    }


    @Transactional(readOnly = true)
    public List<MemoSimpleResponse> getMemos(Long scheduleId, Long memberId) {
        validateScheduleMember(scheduleId, memberId);

        return memoRepository.findByScheduleId(scheduleId)
                .stream()
                .map(MemoSimpleResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MemoDetailResponse getMemo(Long scheduleId, Long memoId, Long memberId) {
        validateScheduleMember(scheduleId, memberId);
        validateScheduleMemo(scheduleId, memoId);

        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(() -> new RuntimeException());

        Long ownerId = memo.getMemberId();

        Member member = memberRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException());

        return MemoDetailResponse.of(memo, member);
    }

    private void validateScheduleMemo(Long scheduleId, Long memoId) {
        boolean notExists = !memoRepository.existsByIdAndScheduleId(memoId, scheduleId);

        if (notExists) {
            throw new RuntimeException("");
        }
    }


    @Transactional
    public void modifyMemo(Long scheduleId, Long memoId, MemoRequest memoRequest, Long memberId) {
        validateScheduleMember(scheduleId, memberId);
        validateScheduleMemo(scheduleId, memoId);

        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(() -> new RuntimeException(""));

        memo.modify(memoRequest.getTitle(), memoRequest.getContent());
    }

    @Transactional
    public void deleteMemo(Long scheduleId, Long memoId, Long memberId) {
        validateScheduleMember(scheduleId, memberId);
        validateScheduleMemo(scheduleId, memoId);

        memoRepository.deleteById(memoId);
    }

    // 체크리스트
    @Transactional
    public Long saveChecklist(Long scheduleId, ChecklistCreationRequest checklistCreationRequest, Long memberId) {
        validateScheduleMember(scheduleId, memberId);

        Checklist checklist = scheduleRepository.findById(scheduleId)
                .map(schedule -> createChecklist(checklistCreationRequest, schedule))
                .orElseThrow(() -> new RuntimeException(""));

        return checklistRepository.save(checklist).getId();
    }

    private Checklist createChecklist(ChecklistCreationRequest checklistCreationRequest, Schedule schedule) {
        return Checklist.builder()
                .content(checklistCreationRequest.getContent())
                .schedule(schedule)
                .date(checklistCreationRequest.getDate())
                .build();
    }

    @Transactional(readOnly = true)
    public List<ChecklistResponse> getChecklists(Long scheduleId, Long memberId) {
        validateScheduleMember(scheduleId, memberId);

        return checklistRepository.findByScheduleId(scheduleId).stream()
                .map(ChecklistResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void doCheck(Long scheduleId, Long checklistId, Long memberId, boolean flag) {
        validateScheduleMember(scheduleId, memberId);
        validateScheduleChecklist(scheduleId, checklistId);

        Checklist checklist = checklistRepository.findById(checklistId)
                .orElseThrow(() -> new RuntimeException(""));

        checklist.check(flag);
    }

    private void validateScheduleChecklist(Long scheduleId, Long checklistId) {
        boolean notExists = !checklistRepository.existsByIdAndScheduleId(checklistId, scheduleId);

        if (notExists) {
            throw new RuntimeException("");
        }
    }

    @Transactional
    public void deleteChecklist(Long scheduleId, Long checklistId, Long memberId) {
        validateScheduleMember(scheduleId, memberId);
        validateScheduleChecklist(scheduleId, checklistId);

        checklistRepository.deleteById(checklistId);
    }


    // 투표
    @Transactional
    public Long saveVoting(Long scheduleId, VotingCreationRequest votingCreationRequest, Long memberId) {
        validateScheduleMember(scheduleId, memberId);

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException(""));

        Voting voting = createVoting(votingCreationRequest, memberId, schedule);

        createVotingContents(votingCreationRequest, voting);

        return votingRepository.save(voting).getId();
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

    private VotingContent createVotingContent(Voting voting, String v) {
        return VotingContent.builder()
                .content(v)
                .voting(voting)
                .build();
    }

    @Transactional
    public void deleteVoting(Long scheduleId, Long votingId, Long memberId) {
        validateScheduleMember(scheduleId, memberId);
        validateScheduleVoting(scheduleId, votingId);

        votingRepository.deleteById(votingId);
    }

    private void validateScheduleVoting(Long scheduleId, Long votingId) {
        boolean notExists = !votingRepository.existsByIdAndScheduleId(votingId, scheduleId);

        if (notExists) {
            throw new RuntimeException("");
        }
    }

    @Transactional(readOnly = true)
    public List<VotingSimpleResponse> getVotingList(Long scheduleId, Long memberId) {
        validateScheduleMember(scheduleId,memberId);

        return votingRepository.findByScheduleId(scheduleId)
                .stream()
                .map(VotingSimpleResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VotingDetailResponse getVoting(Long scheduleId, Long votingId, Long memberId) {
        validateScheduleMember(scheduleId, memberId);
        validateScheduleVoting(scheduleId, votingId);

        Voting voting = votingRepository.findById(votingId)
                .orElseThrow(() -> new RuntimeException());

        Long ownerId = voting.getMemberId();

        Member owner = memberRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException());

        return VotingDetailResponse.of(voting, owner, memberId);
    }

    @Transactional
    public void doVote(Long scheduleId, Long votingId, VotingRequest votingRequest, Long memberId) {
        validateScheduleMember(scheduleId, memberId);
        validateScheduleVoting(scheduleId, votingId);

        Voting voting = votingRepository.findById(votingId)
                .orElseThrow(() -> new RuntimeException(""));

        voting.vote(votingRequest.getVotingMap(), memberId);
    }
}
