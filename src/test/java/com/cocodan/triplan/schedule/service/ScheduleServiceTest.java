package com.cocodan.triplan.schedule.service;

import com.cocodan.triplan.member.dto.response.MemberCreateResponse;
import com.cocodan.triplan.member.dto.response.MemberSimpleResponse;
import com.cocodan.triplan.member.service.MemberService;
import com.cocodan.triplan.schedule.domain.*;
import com.cocodan.triplan.schedule.domain.vo.Theme;
import com.cocodan.triplan.schedule.dto.request.*;
import com.cocodan.triplan.schedule.dto.response.*;
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

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class ScheduleServiceTest {

    private Long MEMBER_ID;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MemberService memberService;

    @PostConstruct
    void postConstruct() {
        MemberCreateResponse memberCreateResponse = memberService.create(
                "ffff@naver.com",
                "taehyun",
                "1994-12-16",
                "MALE",
                "henry",
                "",
                "asdf123",
                1L);
        MEMBER_ID = memberCreateResponse.getId();
    }

    @Test
    @DisplayName("여행 일정을 생성한다.")
    void createSchedule() {
        // Given
        ScheduleCreationRequest scheduleCreationRequest = TestDataFactory.createScheduleCreation();

        // When
        Long scheduleId = scheduleService.saveSchedule(scheduleCreationRequest, MEMBER_ID);

        Schedule schedule = scheduleRepository.findById(scheduleId).get();

        // Then
        assertThat(schedule.getId()).isEqualTo(scheduleId);
        assertThat(schedule.getCreatedDate()).isNotNull();
    }

    @Test
    @DisplayName("일정 목록을 조회한다.")
    void getSchedules() {
        // Given
        ScheduleCreationRequest scheduleCreationRequest = TestDataFactory.createScheduleCreation();
        Long scheduleId = scheduleService.saveSchedule(scheduleCreationRequest, MEMBER_ID);

        // When
        List<ScheduleResponse> schedules = scheduleService.getSchedules(MEMBER_ID);
        ScheduleResponse scheduleResponse = schedules.get(0);

        // Then
        assertThat(scheduleResponse.getId()).isEqualTo(scheduleId);
        assertThat(scheduleResponse.getTitle()).isEqualTo("title");
        assertThat(scheduleResponse.getStartDate()).isEqualTo("2021-12-01");
        assertThat(scheduleResponse.getEndDate()).isEqualTo("2021-12-03");
        assertThat(scheduleResponse.getThemes()).containsExactlyInAnyOrder(Theme.ACTIVITY, Theme.FOOD);
    }

    @Test
    @DisplayName("일정을 상세 조회한다.")
    void getSchedule() {
        // Given
        ScheduleCreationRequest scheduleCreationRequest = TestDataFactory.createScheduleCreation();
        Long scheduleId = scheduleService.saveSchedule(scheduleCreationRequest, MEMBER_ID);

        // When
        ScheduleResponse response = scheduleService.getSchedule(scheduleId);

        // Then
        assertThat(response.getStartDate()).isEqualTo("2021-12-01");
        assertThat(response.getEndDate()).isEqualTo("2021-12-03");
        assertThat(response.getTitle()).isEqualTo("title");
        assertThat(response.getThemes()).contains(Theme.ACTIVITY, Theme.FOOD);

        List<Long> memberIds = response.getMemberSimpleResponses().stream()
                .map(MemberSimpleResponse::getId)
                .collect(Collectors.toList());

        assertThat(memberIds).containsExactly(MEMBER_ID);

        List<String> memberNicknames = response.getMemberSimpleResponses().stream()
                .map(MemberSimpleResponse::getNickname)
                .collect(Collectors.toList());

        assertThat(memberNicknames).containsExactly("henry");

        List<Long> spotIds = response.getSpotResponseList().stream()
                .map(ScheduleSpotResponse::getSpotId)
                .collect(Collectors.toList());

        assertThat(spotIds).containsExactly(11L, 21L, 31L, 41L, 51L, 61L, 71L, 81L);
    }

    @Test
    @DisplayName("일정 장소 리스트를 수정한다")
    void modifySpots() {
        // Given
        ScheduleCreationRequest scheduleCreationRequest = TestDataFactory.createScheduleCreation();
        Long schedule = scheduleService.saveSchedule(scheduleCreationRequest, MEMBER_ID);

        ScheduleModificationRequest scheduleModificationRequest = new ScheduleModificationRequest("updated Title", List.of("NATURE"),
                List.of(
                        new DailyScheduleSpotCreationRequest(5L, "address1", "roadAddress1", "010-1111-2222", "불국사1", new Position(37.1234, 125.3333), 1, 1),
                        new DailyScheduleSpotCreationRequest(6L, "address2", "roadAddress2", "010-1111-2223", "불국사2", new Position(37.1234, 125.3333), 1, 2),
                        new DailyScheduleSpotCreationRequest(7L, "address3", "roadAddress3", "010-1111-2224", "불국사3", new Position(37.1234, 125.3333), 2, 1),
                        new DailyScheduleSpotCreationRequest(8L, "address4", "roadAddress4", "010-1111-2225", "불국사4", new Position(37.1234, 125.3333), 2, 2),
                        new DailyScheduleSpotCreationRequest(9L, "address5", "roadAddress5", "010-1111-2226", "불국사5", new Position(37.1234, 125.3333), 3, 1),
                        new DailyScheduleSpotCreationRequest(11L, "address6", "roadAddress6", "010-1111-2227", "불국사6", new Position(37.1234, 125.3333), 3, 2),
                        new DailyScheduleSpotCreationRequest(12L, "address7", "roadAddress7", "010-1111-22228", "불국사7", new Position(37.1234, 125.3333), 3, 3)
                ));

        // When
        scheduleService.modifySchedule(schedule, scheduleModificationRequest, MEMBER_ID);

        // Then
        Schedule updatedSchedule = scheduleRepository.findById(schedule).get();

        List<Long> ids = updatedSchedule.getDailyScheduleSpots().stream()
                .map(DailyScheduleSpot::getSpotId)
                .collect(Collectors.toList());

        assertThat(ids).containsExactlyInAnyOrder(5L, 6L, 7L, 8L, 9L, 11L, 12L);
    }

    @Test
    @DisplayName("일정을 삭제한다")
    void deleteSchedule() {
        // Given
        ScheduleCreationRequest scheduleCreationRequest = TestDataFactory.createScheduleCreation();
        Long schedule = scheduleService.saveSchedule(scheduleCreationRequest, MEMBER_ID);

        // When
        scheduleService.deleteSchedule(schedule, MEMBER_ID);

        // Then
        assertThat(scheduleRepository.findById(schedule)).isNotPresent();
    }
}
