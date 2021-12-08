package com.cocodan.triplan.schedule.service;

import com.cocodan.triplan.schedule.domain.*;
import com.cocodan.triplan.schedule.domain.vo.Thema;
import com.cocodan.triplan.schedule.dto.request.*;
import com.cocodan.triplan.schedule.dto.response.ScheduleDetailResponse;
import com.cocodan.triplan.schedule.dto.response.ScheduleSimpleResponse;
import com.cocodan.triplan.schedule.repository.ChecklistRepository;
import com.cocodan.triplan.schedule.repository.MemoRepository;
import com.cocodan.triplan.schedule.repository.ScheduleRepository;
import com.cocodan.triplan.schedule.repository.VotingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class ScheduleServiceTest {

    private final Long MEMBER_ID = 1L;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MemoRepository memoRepository;

    @Autowired
    private ChecklistRepository checklistRepository;

    @Autowired
    private VotingRepository votingRepository;

    @Test
    @DisplayName("여행 일정을 생성한다.")
    void createSchedule() {
        ScheduleCreationRequest scheduleCreationRequest = createScheduleCreation();

        scheduleService.createSchedule(scheduleCreationRequest, MEMBER_ID);
    }

    private ScheduleCreationRequest createScheduleCreation() {
        return new ScheduleCreationRequest("title", LocalDate.of(2021, 12, 1), LocalDate.of(2021, 12, 3), List.of("activity", "food"),
                List.of(new DailyScheduleSpotCreationRequest(1L, LocalDate.of(2021, 12, 1), 1),
                        new DailyScheduleSpotCreationRequest(2L, LocalDate.of(2021, 12, 1), 2),
                        new DailyScheduleSpotCreationRequest(3L, LocalDate.of(2021, 12, 1), 3),
                        new DailyScheduleSpotCreationRequest(4L, LocalDate.of(2021, 12, 2), 1),
                        new DailyScheduleSpotCreationRequest(5L, LocalDate.of(2021, 12, 2), 2),
                        new DailyScheduleSpotCreationRequest(6L, LocalDate.of(2021, 12, 2), 3),
                        new DailyScheduleSpotCreationRequest(7L, LocalDate.of(2021, 12, 3), 1),
                        new DailyScheduleSpotCreationRequest(8L, LocalDate.of(2021, 12, 3), 2)
                ));
    }

    @Test
    @DisplayName("일정 목록을 조회한다.")
    void getSchedules() {
        //TODO: 멤버 엔티티 추가되면 test 실행
        List<ScheduleSimpleResponse> schedules = scheduleService.getSchedules(MEMBER_ID);
    }

    @Test
    @DisplayName("일정을 상세 조회한다.")
    void getSchedule() {
        ScheduleCreationRequest scheduleCreationRequest = createScheduleCreation();
        Long scheduleId = scheduleService.createSchedule(scheduleCreationRequest, MEMBER_ID);

        ScheduleDetailResponse schedule = scheduleService.getSchedule(scheduleId);

        assertThat(schedule.getStartDate()).isEqualTo(LocalDate.of(2021, 12, 1));
        assertThat(schedule.getTitle()).isEqualTo("title");
        assertThat(schedule.getThemas()).contains(Thema.valueOf("ACTIVITY"), Thema.valueOf("FOOD"));
        List<Long> ids = schedule.getSpotSimpleList().stream()
                .map(spotSimple -> spotSimple.getId())
                .collect(Collectors.toList());

        // TODO: 장소 데이터 추가되면 다시 확인
        assertThat(ids).containsExactlyInAnyOrder(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
    }

    @Test
    @DisplayName("일정 장소 리스트를 수정한다")
    void modifySpots() {
        // Given
        ScheduleCreationRequest scheduleCreationRequest = createScheduleCreation();
        Long schedule = scheduleService.createSchedule(scheduleCreationRequest, MEMBER_ID);

        ScheduleModificationRequest scheduleModificationRequest = new ScheduleModificationRequest(
                List.of(
                        new DailyScheduleSpotCreationRequest(5L, LocalDate.of(2021, 12, 22), 1),
                        new DailyScheduleSpotCreationRequest(6L, LocalDate.of(2021, 12, 22), 2),
                        new DailyScheduleSpotCreationRequest(7L, LocalDate.of(2021, 12, 23), 1),
                        new DailyScheduleSpotCreationRequest(8L, LocalDate.of(2021, 12, 23), 2),
                        new DailyScheduleSpotCreationRequest(9L, LocalDate.of(2021, 12, 24), 1),
                        new DailyScheduleSpotCreationRequest(11L, LocalDate.of(2021, 12, 24), 2),
                        new DailyScheduleSpotCreationRequest(12L, LocalDate.of(2021, 12, 24), 3)
                ));

        // When
        scheduleService.modifySchedule(schedule, scheduleModificationRequest);

        // Then
        Schedule updatedSchedule = scheduleRepository.findById(schedule).get();

        List<Long> ids = updatedSchedule.getDailyScheduleSpots().stream()
                .map(dailyScheduleSpot -> dailyScheduleSpot.getSpotId())
                .collect(Collectors.toList());

        assertThat(ids).containsExactlyInAnyOrder(5L, 6L, 7L, 8L, 9L, 11L, 12L);
    }

    @Test
    @DisplayName("일정을 삭제한다")
    void deleteSchedule() {
        // Given
        ScheduleCreationRequest scheduleCreationRequest = createScheduleCreation();
        Long schedule = scheduleService.createSchedule(scheduleCreationRequest, MEMBER_ID);

        // When
        scheduleService.deleteSchedule(schedule, MEMBER_ID);

        // Then
        assertThat(scheduleRepository.findById(schedule)).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("메모를 추가한다")
    void createMemo() {
        // Given
        Long schedule = scheduleService.createSchedule(createScheduleCreation(), MEMBER_ID);
        MemoRequest memoRequest = new MemoRequest("JIFEOgoiioghiohgieogio");

        // When
        Long memo = scheduleService.createMemo(schedule, memoRequest, MEMBER_ID);
        Memo saved = memoRepository.findById(memo).get();

        // Then
        assertThat(saved.getContent()).isEqualTo("JIFEOgoiioghiohgieogio");
    }

    @Test
    @DisplayName("메모를 수정한다")
    void modifyMemo() {
        // Given
        Long schedule = scheduleService.createSchedule(createScheduleCreation(), MEMBER_ID);
        MemoRequest memoRequest = new MemoRequest("JIFEOgoiioghiohgieogio");
        Long memo = scheduleService.createMemo(schedule, memoRequest, MEMBER_ID);

        // When
        MemoRequest updateRequest = new MemoRequest("Updated Memo Content");
        scheduleService.modifyMemo(schedule, memo, updateRequest, MEMBER_ID);

        // Then
        Memo updated = memoRepository.findById(memo).get();
        assertThat(updated.getContent()).isEqualTo(updateRequest.getContent());
    }

    @Test
    @DisplayName("메모를 삭제한다")
    void deleteMemo() {
        // Given
        Long schedule = scheduleService.createSchedule(createScheduleCreation(), MEMBER_ID);
        MemoRequest memoRequest = new MemoRequest("JIFEOgoiioghiohgieogio");
        Long memo = scheduleService.createMemo(schedule, memoRequest, MEMBER_ID);

        // When
        scheduleService.deleteMemo(schedule, memo, MEMBER_ID);

        // Then
        Optional<Memo> optionalMemo = memoRepository.findById(memo);
        assertThat(optionalMemo).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("체크리스트를 추가한다")
    void createChecklist() {
        // Given
        Long schedule = scheduleService.createSchedule(createScheduleCreation(), MEMBER_ID);
        ChecklistCreationRequest checklistCreationRequest = new ChecklistCreationRequest(LocalDate.of(2021, 12, 5), "밥 먹을 사람");

        // When
        Long checklist = scheduleService.createChecklist(schedule, checklistCreationRequest);

        // Then
        assertThat(checklist).isEqualTo(1L);
    }

    @DisplayName("체크리스트 선택 및 해제")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void doCheck(boolean flag) {
        // Given
        Long schedule = scheduleService.createSchedule(createScheduleCreation(), MEMBER_ID);
        ChecklistCreationRequest checklistCreationRequest = new ChecklistCreationRequest(LocalDate.of(2021, 12, 5), "밥 먹을 사람");
        Long checklist = scheduleService.createChecklist(schedule, checklistCreationRequest);

        // When
        scheduleService.doCheck(schedule, checklist, MEMBER_ID, flag);
        Checklist saved = checklistRepository.findById(checklist).get();

        // Then
        assertThat(saved.isChecked()).isEqualTo(flag);
    }

    @Test
    @DisplayName("체크리스트를 삭제한다")
    void deleteChecklist() {
        // Given
        Long schedule = scheduleService.createSchedule(createScheduleCreation(), MEMBER_ID);
        ChecklistCreationRequest checklistCreationRequest = new ChecklistCreationRequest(LocalDate.of(2021, 12, 5), "밥 먹을 사람");
        Long checklist = scheduleService.createChecklist(schedule, checklistCreationRequest);

        // When
        scheduleService.deleteChecklist(schedule, checklist, MEMBER_ID);
        Optional<Checklist> saved = checklistRepository.findById(checklist);

        // Then
        assertThat(saved).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("투표를 추가한다")
    void createVoting() {
        // Given
        Long schedule = scheduleService.createSchedule(createScheduleCreation(), MEMBER_ID);
        VotingCreationRequest votingCreationRequest = new VotingCreationRequest("무슨 요일날 갈까요?", List.of("월", "화", "수", "목"), false);

        // When
        Long voting = scheduleService.createVoting(schedule, votingCreationRequest, MEMBER_ID);
        Voting savedVoting = votingRepository.findById(voting).get();


        List<String> contentList = savedVoting.getVotingContents()
                .stream()
                .map(votingContent -> votingContent.getContent())
                .collect(Collectors.toList());

        // Then
        assertThat(savedVoting.getTitle()).isEqualTo("무슨 요일날 갈까요?");
        assertThat(savedVoting.isMultipleFlag()).isFalse();
        assertThat(contentList).containsExactly("월", "화", "수", "목");
    }

    @Test
    @DisplayName("투표를 행사 한다")
    void vote() {
        // Given
        Long schedule = scheduleService.createSchedule(createScheduleCreation(), MEMBER_ID);
        VotingCreationRequest votingCreationRequest = new VotingCreationRequest("무슨 요일날 갈까요?", List.of("월", "화", "수", "목"), false);

        Long voting = scheduleService.createVoting(schedule, votingCreationRequest, MEMBER_ID);
        Voting saved = votingRepository.findById(voting).get();

        List<Long> ids = saved.getVotingContents().stream()
                .map(votingContent -> votingContent.getId())
                .collect(Collectors.toList());

        Map<Long, Boolean> votingMap = Map.of(ids.get(0), true, ids.get(1), false, ids.get(2), true, ids.get(3), false);
        VotingRequest votingRequest = new VotingRequest(votingMap);

        // When
        scheduleService.doVote(schedule, voting, votingRequest, MEMBER_ID);

        List<VotingContent> votingContents = saved.getVotingContents();

        List<Integer> votingCountList = votingContents.stream()
                .map(VotingContent::getCount)
                .collect(Collectors.toList());

        // Then
        votingContents.forEach(votingContent -> {
            if (votingMap.get(votingContent.getId())) {
                assertThat(getVotingMemberIds(votingContent)).contains(MEMBER_ID);
            } else {
                assertThat(getVotingMemberIds(votingContent)).doesNotContain(MEMBER_ID);
            }
        });

        assertThat(votingCountList).containsExactly(1, 0, 1, 0);
        assertThat(saved.getVotingMemberCount()).isEqualTo(1);
    }

    private List<Long> getVotingMemberIds(VotingContent votingContent) {
        return votingContent.getVotingContentMembers()
                .stream()
                .map(VotingContentMember::getMemberId)
                .collect(Collectors.toList());
    }

    @Test
    @DisplayName("투표를 삭제한다")
    void deleteVoting() {
        // Given
        Long schedule = scheduleService.createSchedule(createScheduleCreation(), MEMBER_ID);
        VotingCreationRequest votingCreationRequest = new VotingCreationRequest("무슨 요일날 갈까요?", List.of("월", "화", "수", "목"), false);
        Long voting = scheduleService.createVoting(schedule, votingCreationRequest, MEMBER_ID);

        // When
        scheduleService.deleteVoting(schedule, voting, MEMBER_ID);
        Optional<Voting> actual = votingRepository.findById(voting);

        // Then
        assertThat(actual).isEqualTo(Optional.empty());
    }
}