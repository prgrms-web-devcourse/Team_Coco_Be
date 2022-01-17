package com.cocodan.triplan.schedule.service;

import com.cocodan.triplan.member.dto.response.MemberCreateResponse;
import com.cocodan.triplan.member.service.MemberService;
import com.cocodan.triplan.schedule.domain.Checklist;
import com.cocodan.triplan.schedule.dto.request.ChecklistCreationRequest;
import com.cocodan.triplan.schedule.repository.ChecklistRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ChecklistServiceTest {

    private Long MEMBER_ID;

    @Autowired
    private ChecklistService checklistService;

    @Autowired
    private ChecklistRepository checklistRepository;

    @Autowired
    private ScheduleService scheduleService;

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
    @DisplayName("체크리스트를 추가한다")
    void createChecklist() {
        // Given
        Long schedule = scheduleService.saveSchedule(TestDataFactory.createScheduleCreation(), MEMBER_ID);
        ChecklistCreationRequest checklistCreationRequest = new ChecklistCreationRequest(0, "밥 먹을 사람");

        // When
        Long checklist = checklistService.saveChecklist(schedule, checklistCreationRequest, MEMBER_ID);

        // Then
        assertThat(checklist).isEqualTo(1L);
    }

    @DisplayName("체크리스트 선택 및 해제")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void doCheck(boolean flag) {
        // Given
        Long schedule = scheduleService.saveSchedule(TestDataFactory.createScheduleCreation(), MEMBER_ID);
        ChecklistCreationRequest checklistCreationRequest = new ChecklistCreationRequest(1, "밥 먹을 사람");
        Long checklist = checklistService.saveChecklist(schedule, checklistCreationRequest, MEMBER_ID);

        // When
        checklistService.doCheck(schedule, checklist, MEMBER_ID, flag);
        Checklist saved = checklistRepository.findById(checklist).get();

        // Then
        assertThat(saved.isChecked()).isEqualTo(flag);
    }

    @Test
    @DisplayName("체크리스트를 삭제한다")
    void deleteChecklist() {
        // Given
        Long schedule = scheduleService.saveSchedule(TestDataFactory.createScheduleCreation(), MEMBER_ID);
        ChecklistCreationRequest checklistCreationRequest = new ChecklistCreationRequest(1, "밥 먹을 사람");
        Long checklist = checklistService.saveChecklist(schedule, checklistCreationRequest, MEMBER_ID);

        // When
        checklistService.deleteChecklist(schedule, checklist, MEMBER_ID);
        Optional<Checklist> saved = checklistRepository.findById(checklist);

        // Then
        assertThat(saved).isEqualTo(Optional.empty());
    }

}
