package com.cocodan.triplan.schedule.service;

import com.cocodan.triplan.schedule.domain.Schedule;
import com.cocodan.triplan.schedule.domain.vo.Thema;
import com.cocodan.triplan.schedule.dto.request.*;
import com.cocodan.triplan.schedule.dto.response.ScheduleDetailResponse;
import com.cocodan.triplan.schedule.dto.response.ScheduleSimpleResponse;
import com.cocodan.triplan.schedule.repository.ScheduleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
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
    public void modifySpots() {
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
    public void deleteSchedule() {
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
    public void createMemo() {
        // Given
        Long schedule = scheduleService.createSchedule(createScheduleCreation(), MEMBER_ID);
        MemoCreationRequest memoCreationRequest = new MemoCreationRequest("JIFEOgoiioghiohgieogio");

        // When
        Long memo = scheduleService.createMemo(schedule, memoCreationRequest, MEMBER_ID);


        // Then
        assertThat(memo).isEqualTo(1L);
    }

    @Test
    @DisplayName("체크리스트를 추가한다")
    public void createChecklist() {
        // Given
        Long schedule = scheduleService.createSchedule(createScheduleCreation(), MEMBER_ID);
        ChecklistCreationRequest checklistCreationRequest = new ChecklistCreationRequest(LocalDate.of(2021, 12, 5), "밥 먹을 사람");

        // When
        Long checklist = scheduleService.createChecklist(schedule, checklistCreationRequest);

        // Then
        assertThat(checklist).isEqualTo(1L);
    }

    @Test
    @DisplayName("투표를 추가한다")
    public void createVoting() {
        // Given
        Long schedule = scheduleService.createSchedule(createScheduleCreation(), MEMBER_ID);
        VotingCreationRequest votingCreationRequest = new VotingCreationRequest("무슨 요일날 갈까요?", List.of("월", "화", "수", "목"), false);

        // When
        Long voting = scheduleService.createVoting(schedule, votingCreationRequest, MEMBER_ID);

        // Then
        assertThat(voting).isEqualTo(1L);
    }
}