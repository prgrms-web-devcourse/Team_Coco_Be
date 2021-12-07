package com.cocodan.triplan.schedule.service;

import com.cocodan.triplan.schedule.domain.vo.Thema;
import com.cocodan.triplan.schedule.dto.request.DailyScheduleSpotCreation;
import com.cocodan.triplan.schedule.dto.request.ScheduleCreation;
import com.cocodan.triplan.schedule.dto.response.ScheduleDetail;
import com.cocodan.triplan.schedule.dto.response.ScheduleSimple;
import com.cocodan.triplan.spot.dto.response.SpotSimple;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class ScheduleServiceTest {

    @Autowired
    private ScheduleService scheduleService;

    @Test
    @DisplayName("여행 일정을 생성한다.")
    void createSchedule() {
        ScheduleCreation scheduleCreation = createScheduleCreation();

        scheduleService.createSchedule(scheduleCreation);
    }

    private ScheduleCreation createScheduleCreation() {
        return new ScheduleCreation("title", LocalDate.of(2021,12, 1), LocalDate.of(2021,12,3), List.of("activity", "food"),
                List.of(new DailyScheduleSpotCreation(1L, LocalDate.of(2021,12, 1), 1),
                        new DailyScheduleSpotCreation(2L, LocalDate.of(2021,12,1), 2),
                        new DailyScheduleSpotCreation(3L, LocalDate.of(2021,12,1), 3),
                        new DailyScheduleSpotCreation(4L, LocalDate.of(2021,12,2), 1),
                        new DailyScheduleSpotCreation(5L, LocalDate.of(2021,12,2), 2),
                        new DailyScheduleSpotCreation(6L, LocalDate.of(2021,12,2), 3),
                        new DailyScheduleSpotCreation(7L, LocalDate.of(2021,12,3), 1),
                        new DailyScheduleSpotCreation(8L, LocalDate.of(2021,12,3), 2)
                ));
    }

    @Test
    @DisplayName("일정 목록을 조회한다.")
    void getSchedules() {
        //TODO: 멤버 엔티티 추가되면 test 실행
        List<ScheduleSimple> schedules = scheduleService.getSchedules(1L);
    }

    @Test
    @DisplayName("일정을 상세 조회한다.")
    void getSchedule() {
        ScheduleCreation scheduleCreation = createScheduleCreation();
        scheduleService.createSchedule(scheduleCreation);

        ScheduleDetail schedule = scheduleService.getSchedule(1L);

        assertThat(schedule.getStartDate()).isEqualTo(LocalDate.of(2021,12, 1));
        assertThat(schedule.getTitle()).isEqualTo("title");
        assertThat(schedule.getThemas()).contains(Thema.valueOf("ACTIVITY"),Thema.valueOf("FOOD"));
        List<Long> ids = schedule.getSpotSimpleList().stream()
                .map(spotSimple -> spotSimple.getId())
                .collect(Collectors.toList());

        // TODO: 장소 데이터 추가되면 다시 확인
        assertThat(ids).containsExactlyInAnyOrder(1L,2L,3L,4L,5L,6L,7L,8L);
    }
}