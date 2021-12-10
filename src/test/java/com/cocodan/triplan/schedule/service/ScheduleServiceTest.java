package com.cocodan.triplan.schedule.service;

import com.cocodan.triplan.member.dto.response.MemberCreateResponse;
import com.cocodan.triplan.member.service.MemberService;
import com.cocodan.triplan.schedule.domain.*;
import com.cocodan.triplan.schedule.domain.vo.Theme;
import com.cocodan.triplan.schedule.dto.request.*;
import com.cocodan.triplan.schedule.dto.response.*;
import com.cocodan.triplan.schedule.repository.ChecklistRepository;
import com.cocodan.triplan.schedule.repository.MemoRepository;
import com.cocodan.triplan.schedule.repository.ScheduleRepository;
import com.cocodan.triplan.schedule.repository.VotingRepository;
import com.cocodan.triplan.spot.dto.response.SpotSimple;
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
    private MemoRepository memoRepository;

    @Autowired
    private ChecklistRepository checklistRepository;

    @Autowired
    private VotingRepository votingRepository;

    @Autowired
    private MemberService memberService;

    @PostConstruct
    void postConstruct() {
        MemberCreateResponse memberCreateResponse = memberService.create("ffff@naver.com", "taehyun", "01011111111", "1994-12-16", "남성", "henry", "");
        MEMBER_ID = memberCreateResponse.getId();
    }

    @Test
    @DisplayName("여행 일정을 생성한다.")
    void createSchedule() {
        ScheduleCreationRequest scheduleCreationRequest = createScheduleCreation();

        scheduleService.createSchedule(scheduleCreationRequest, MEMBER_ID);
    }

    private ScheduleCreationRequest createScheduleCreation() {
        return new ScheduleCreationRequest("title", LocalDate.of(2021, 12, 1), LocalDate.of(2021, 12, 3), List.of("activity", "food"),
                List.of(new DailyScheduleSpotCreationRequest(1L, "address1", "roadAddress1", "010-1111-2222", "불국사1", new Position(37.1234, 125.3333), LocalDate.of(2021, 12, 1), 1),
                        new DailyScheduleSpotCreationRequest(2L, "address2", "roadAddress2", "010-1111-2223", "불국사2", new Position(37.1234, 125.3333), LocalDate.of(2021, 12, 1), 2),
                        new DailyScheduleSpotCreationRequest(3L, "address3", "roadAddress3", "010-1111-2224", "불국사3", new Position(37.1234, 125.3333), LocalDate.of(2021, 12, 1), 3),
                        new DailyScheduleSpotCreationRequest(4L, "address4", "roadAddress4", "010-1111-2225", "불국사4", new Position(37.1234, 125.3333), LocalDate.of(2021, 12, 2), 1),
                        new DailyScheduleSpotCreationRequest(5L, "address5", "roadAddress5", "010-1111-2226", "불국사5", new Position(37.1234, 125.3333), LocalDate.of(2021, 12, 2), 2),
                        new DailyScheduleSpotCreationRequest(6L, "address6", "roadAddress6", "010-1111-2227", "불국사6", new Position(37.1234, 125.3333), LocalDate.of(2021, 12, 2), 3),
                        new DailyScheduleSpotCreationRequest(7L, "address7", "roadAddress7", "010-1111-2228", "불국사7", new Position(37.1234, 125.3333), LocalDate.of(2021, 12, 3), 1),
                        new DailyScheduleSpotCreationRequest(8L, "address8", "roadAddress8", "010-1111-2229", "불국사8", new Position(37.1234, 125.3333), LocalDate.of(2021, 12, 3), 2)
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
        assertThat(schedule.getThema()).contains(Theme.valueOf("ACTIVITY"), Theme.valueOf("FOOD"));
        List<Long> ids = schedule.getSpotSimpleList().stream()
                .map(SpotSimple::getId)
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
                List.of(new DailyScheduleSpotCreationRequest(1L, "address1", "roadAddress1", "010-1111-2222", "불국사1", new Position(37.1234, 125.3333), LocalDate.of(2021, 12, 1), 1),
                        new DailyScheduleSpotCreationRequest(2L, "address2", "roadAddress2", "010-1111-2223", "불국사2", new Position(37.1234, 125.3333), LocalDate.of(2021, 12, 1), 2),
                        new DailyScheduleSpotCreationRequest(3L, "address3", "roadAddress3", "010-1111-2224", "불국사3", new Position(37.1234, 125.3333), LocalDate.of(2021, 12, 1), 3),
                        new DailyScheduleSpotCreationRequest(4L, "address4", "roadAddress4", "010-1111-2225", "불국사4", new Position(37.1234, 125.3333), LocalDate.of(2021, 12, 2), 1),
                        new DailyScheduleSpotCreationRequest(5L, "address5", "roadAddress5", "010-1111-2226", "불국사5", new Position(37.1234, 125.3333), LocalDate.of(2021, 12, 2), 2),
                        new DailyScheduleSpotCreationRequest(6L, "address6", "roadAddress6", "010-1111-2227", "불국사6", new Position(37.1234, 125.3333), LocalDate.of(2021, 12, 2), 3),
                        new DailyScheduleSpotCreationRequest(7L, "address7", "roadAddress7", "010-1111-2228", "불국사7", new Position(37.1234, 125.3333), LocalDate.of(2021, 12, 3), 1),
                        new DailyScheduleSpotCreationRequest(8L, "address8", "roadAddress8", "010-1111-2229", "불국사8", new Position(37.1234, 125.3333), LocalDate.of(2021, 12, 3), 2)
                ));

        // When
        scheduleService.modifySchedule(schedule, scheduleModificationRequest);

        // Then
        Schedule updatedSchedule = scheduleRepository.findById(schedule).get();

        List<Long> ids = updatedSchedule.getDailyScheduleSpots().stream()
                .map(DailyScheduleSpot::getSpotId)
                .collect(Collectors.toList());

//        assertThat(ids).containsExactlyInAnyOrder(5L, 6L, 7L, 8L, 9L, 11L, 12L);
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
        MemoRequest memoRequest = new MemoRequest("memotitle", "JIFEOgoiioghiohgieogio");

        // When
        Long memo = scheduleService.createMemo(schedule, memoRequest, MEMBER_ID);

        Memo actual = memoRepository.findById(memo).get();

        // Then
        assertThat(actual.getTitle()).isEqualTo("memotitle");
        assertThat(actual.getContent()).isEqualTo("JIFEOgoiioghiohgieogio");
    }

    @Test
    @DisplayName("메모 목록을 조회한다")
    void getMemos() {
        // Given
        Long schedule = scheduleService.createSchedule(createScheduleCreation(), MEMBER_ID);
        MemoRequest memoRequest1 = new MemoRequest("memotitle1", "JIFEOgoiioghiohgieogio1");
        MemoRequest memoRequest2 = new MemoRequest("memotitle2", "JIFEOgoiioghiohgieogio2");
        MemoRequest memoRequest3 = new MemoRequest("memotitle3", "JIFEOgoiioghiohgieogio3");

        Long memo1 = scheduleService.createMemo(schedule, memoRequest1, MEMBER_ID);
        Long memo2 = scheduleService.createMemo(schedule, memoRequest2, MEMBER_ID);
        Long memo3 = scheduleService.createMemo(schedule, memoRequest3, MEMBER_ID);

        // When
        List<MemoSimpleResponse> memos = scheduleService.getMemos(schedule, MEMBER_ID);

        List<String> titles = memos.stream()
                .map(MemoSimpleResponse::getTitle)
                .collect(Collectors.toList());

        List<String> contents = memos.stream()
                .map((MemoSimpleResponse::getContent))
                .collect(Collectors.toList());

        // Then
        assertThat(titles).containsExactlyInAnyOrder("memotitle1", "memotitle2", "memotitle3");
        assertThat(contents).containsExactlyInAnyOrder("JIFEOgoiioghiohgieogio1", "JIFEOgoiioghiohgieogio2", "JIFEOgoiioghiohgieogio3");
    }

    @Test
    @DisplayName("메모를 상세 조회 한다")
    void getMemo() {
        // Given
        Long schedule = scheduleService.createSchedule(createScheduleCreation(), MEMBER_ID);
        MemoRequest memoRequest1 = new MemoRequest("memotitle1", "JIFEOgoiioghiohgieogio1");
        Long memo = scheduleService.createMemo(schedule, memoRequest1, MEMBER_ID);

        // When
        MemoDetailResponse actual = scheduleService.getMemo(schedule, memo, MEMBER_ID);

        // Then
        assertThat(actual.getTitle()).isEqualTo("memotitle1");
        assertThat(actual.getContent()).isEqualTo("JIFEOgoiioghiohgieogio1");
        assertThat(actual.getOwnerId()).isEqualTo(MEMBER_ID);
    }

    @Test
    @DisplayName("메모를 수정한다")
    void modifyMemo() {
        // Given
        Long schedule = scheduleService.createSchedule(createScheduleCreation(), MEMBER_ID);
        MemoRequest memoRequest = new MemoRequest("memotitle", "JIFEOgoiioghiohgieogio");
        Long memo = scheduleService.createMemo(schedule, memoRequest, MEMBER_ID);

        // When
        MemoRequest updateRequest = new MemoRequest("Updated Memo Title", "Updated Memo Content");
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
        MemoRequest memoRequest = new MemoRequest("memotitle", "JIFEOgoiioghiohgieogio");
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
                .map(VotingContent::getContent)
                .collect(Collectors.toList());

        // Then
        assertThat(savedVoting.getTitle()).isEqualTo("무슨 요일날 갈까요?");
        assertThat(savedVoting.isMultipleFlag()).isFalse();
        assertThat(contentList).containsExactly("월", "화", "수", "목");
    }

    @Test
    @DisplayName("투표 목록을 조회한다")
    void getVotings() {
        // Given
        Long schedule = scheduleService.createSchedule(createScheduleCreation(), MEMBER_ID);
        VotingCreationRequest votingCreationRequest1 = new VotingCreationRequest("무슨 요일날 갈까요?", List.of("월", "화", "수", "목"), false);
        VotingCreationRequest votingCreationRequest2 = new VotingCreationRequest("어디 여행 갈래", List.of("서울", "부산", "제주도", "안 가"), true);

        Long voting1 = scheduleService.createVoting(schedule, votingCreationRequest1, MEMBER_ID);
        Long voting2 = scheduleService.createVoting(schedule, votingCreationRequest2, MEMBER_ID);

        // When
        List<VotingSimpleResponse> votingList = scheduleService.getVotingList(schedule);

        List<String> titles = votingList.stream()
                .map(VotingSimpleResponse::getTitle)
                .collect(Collectors.toList());

        List<Integer> counts = votingList.stream()
                .map(VotingSimpleResponse::getMemberCount)
                .collect(Collectors.toList());

        // Then
        assertThat(titles).contains("무슨 요일날 갈까요?", "어디 여행 갈래");
        assertThat(counts).containsExactly(0, 0);
    }

    @Test
    @DisplayName("투표의 상세 정보를 조회한다")
    void getVoting() {
        // Given
        Long schedule = scheduleService.createSchedule(createScheduleCreation(), MEMBER_ID);
        VotingCreationRequest votingCreationRequest = new VotingCreationRequest("무슨 요일날 갈까요?", List.of("월", "화", "수", "목"), false);

        Long voting = scheduleService.createVoting(schedule, votingCreationRequest, MEMBER_ID);

        // When
        VotingDetailResponse response = scheduleService.getVoting(schedule, voting, MEMBER_ID);

        List<String> contents = response.getVotingContentResponses().stream()
                .map(VotingContentResponse::getContent)
                .collect(Collectors.toList());

        // Then
        assertThat(response.getId()).isEqualTo(voting);
        assertThat(response.getTitle()).isEqualTo("무슨 요일날 갈까요?");
        assertThat(response.getNumOfTotalParticipants()).isZero();
        assertThat(response.getOwnerId()).isEqualTo(MEMBER_ID);
        assertThat(contents).containsExactly("월", "화", "수", "목");
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
                .map(VotingContent::getId)
                .collect(Collectors.toList());

        Map<Long, Boolean> votingMap = Map.of(ids.get(0), true, ids.get(1), false, ids.get(2), true, ids.get(3), false);
        VotingRequest votingRequest = new VotingRequest(votingMap);

        // When
        scheduleService.doVote(schedule, voting, votingRequest, MEMBER_ID);

        List<VotingContent> votingContents = saved.getVotingContents();

        List<Integer> votingCountList = votingContents.stream()
                .map(VotingContent::getNumOfParticipants)
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
        assertThat(saved.getNumOfTotalParticipants()).isEqualTo(1);
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
