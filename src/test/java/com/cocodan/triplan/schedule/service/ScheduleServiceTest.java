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
    private MemoRepository memoRepository;

    @Autowired
    private ChecklistRepository checklistRepository;

    @Autowired
    private VotingRepository votingRepository;

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
    @DisplayName("?????? ????????? ????????????.")
    void createSchedule() {
        // Given
        ScheduleCreationRequest scheduleCreationRequest = createScheduleCreation();

        // When
        Long scheduleId = scheduleService.saveSchedule(scheduleCreationRequest, MEMBER_ID);

        Schedule schedule = scheduleRepository.findById(scheduleId).get();

        // Then
        assertThat(schedule.getId()).isEqualTo(scheduleId);
        assertThat(schedule.getCreatedDate()).isNotNull();
    }

    private ScheduleCreationRequest createScheduleCreation() {
        return new ScheduleCreationRequest("title", LocalDate.of(2021, 12, 1), LocalDate.of(2021, 12, 3), List.of("activity", "food"),
                List.of(new DailyScheduleSpotCreationRequest(11L, "address1", "roadAddress1", "010-1111-2222", "?????????1", new Position(37.1234, 125.3333), 1, 1),
                        new DailyScheduleSpotCreationRequest(21L, "address2", "roadAddress2", "010-1111-2223", "?????????2", new Position(37.1234, 125.3333), 1, 2),
                        new DailyScheduleSpotCreationRequest(31L, "address3", "roadAddress3", "010-1111-2224", "?????????3", new Position(37.1234, 125.3333), 1, 3),
                        new DailyScheduleSpotCreationRequest(41L, "address4", "roadAddress4", "010-1111-2225", "?????????4", new Position(37.1234, 125.3333), 2, 1),
                        new DailyScheduleSpotCreationRequest(51L, "address5", "roadAddress5", "010-1111-2226", "?????????5", new Position(37.1234, 125.3333), 2, 2),
                        new DailyScheduleSpotCreationRequest(61L, "address6", "roadAddress6", "010-1111-2227", "?????????6", new Position(37.1234, 125.3333), 2, 3),
                        new DailyScheduleSpotCreationRequest(71L, "address7", "roadAddress7", "010-1111-2228", "?????????7", new Position(37.1234, 125.3333), 3, 1),
                        new DailyScheduleSpotCreationRequest(81L, "address8", "roadAddress8", "010-1111-2229", "?????????8", new Position(37.1234, 125.3333), 3, 2)
                ),
                List.of());
    }

    @Test
    @DisplayName("?????? ????????? ????????????.")
    void getSchedules() {
        // Given
        ScheduleCreationRequest scheduleCreationRequest = createScheduleCreation();
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
    @DisplayName("????????? ?????? ????????????.")
    void getSchedule() {
        // Given
        ScheduleCreationRequest scheduleCreationRequest = createScheduleCreation();
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
    @DisplayName("?????? ?????? ???????????? ????????????")
    void modifySpots() {
        // Given
        ScheduleCreationRequest scheduleCreationRequest = createScheduleCreation();
        Long schedule = scheduleService.saveSchedule(scheduleCreationRequest, MEMBER_ID);

        ScheduleModificationRequest scheduleModificationRequest = new ScheduleModificationRequest("updated Title", List.of("NATURE"),
                List.of(
                        new DailyScheduleSpotCreationRequest(5L, "address1", "roadAddress1", "010-1111-2222", "?????????1", new Position(37.1234, 125.3333), 1, 1),
                        new DailyScheduleSpotCreationRequest(6L, "address2", "roadAddress2", "010-1111-2223", "?????????2", new Position(37.1234, 125.3333), 1, 2),
                        new DailyScheduleSpotCreationRequest(7L, "address3", "roadAddress3", "010-1111-2224", "?????????3", new Position(37.1234, 125.3333), 2, 1),
                        new DailyScheduleSpotCreationRequest(8L, "address4", "roadAddress4", "010-1111-2225", "?????????4", new Position(37.1234, 125.3333), 2, 2),
                        new DailyScheduleSpotCreationRequest(9L, "address5", "roadAddress5", "010-1111-2226", "?????????5", new Position(37.1234, 125.3333), 3, 1),
                        new DailyScheduleSpotCreationRequest(11L, "address6", "roadAddress6", "010-1111-2227", "?????????6", new Position(37.1234, 125.3333), 3, 2),
                        new DailyScheduleSpotCreationRequest(12L, "address7", "roadAddress7", "010-1111-22228", "?????????7", new Position(37.1234, 125.3333), 3, 3)
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
    @DisplayName("????????? ????????????")
    void deleteSchedule() {
        // Given
        ScheduleCreationRequest scheduleCreationRequest = createScheduleCreation();
        Long schedule = scheduleService.saveSchedule(scheduleCreationRequest, MEMBER_ID);

        // When
        scheduleService.deleteSchedule(schedule, MEMBER_ID);

        // Then
        assertThat(scheduleRepository.findById(schedule)).isNotPresent();
    }

    @Test
    @DisplayName("????????? ????????????")
    void createMemo() {
        // Given
        Long schedule = scheduleService.saveSchedule(createScheduleCreation(), MEMBER_ID);
        MemoRequest memoRequest = new MemoRequest("memotitle", "JIFEOgoiioghiohgieogio");

        // When
        Long memo = scheduleService.saveMemo(schedule, memoRequest, MEMBER_ID);

        Memo actual = memoRepository.findById(memo).get();

        // Then
        assertThat(actual.getTitle()).isEqualTo("memotitle");
        assertThat(actual.getContent()).isEqualTo("JIFEOgoiioghiohgieogio");
    }

    @Test
    @DisplayName("?????? ????????? ????????????")
    void getMemos() {
        // Given
        Long schedule = scheduleService.saveSchedule(createScheduleCreation(), MEMBER_ID);
        MemoRequest memoRequest1 = new MemoRequest("memotitle1", "JIFEOgoiioghiohgieogio1");
        MemoRequest memoRequest2 = new MemoRequest("memotitle2", "JIFEOgoiioghiohgieogio2");
        MemoRequest memoRequest3 = new MemoRequest("memotitle3", "JIFEOgoiioghiohgieogio3");

        Long memo1 = scheduleService.saveMemo(schedule, memoRequest1, MEMBER_ID);
        Long memo2 = scheduleService.saveMemo(schedule, memoRequest2, MEMBER_ID);
        Long memo3 = scheduleService.saveMemo(schedule, memoRequest3, MEMBER_ID);

        // When
        List<MemoResponse> memos = scheduleService.getMemos(schedule, MEMBER_ID);

        List<String> titles = memos.stream()
                .map(MemoResponse::getTitle)
                .collect(Collectors.toList());

        List<String> contents = memos.stream()
                .map((MemoResponse::getContent))
                .collect(Collectors.toList());

        List<Long> ids = memos.stream()
                .map((MemoResponse::getId))
                .collect(Collectors.toList());


        // Then
        assertThat(ids).containsExactly(memo1, memo2, memo3);
        assertThat(titles).containsExactlyInAnyOrder("memotitle1", "memotitle2", "memotitle3");
        assertThat(contents).containsExactlyInAnyOrder("JIFEOgoiioghiohgieogio1", "JIFEOgoiioghiohgieogio2", "JIFEOgoiioghiohgieogio3");
    }

    @Test
    @DisplayName("????????? ?????? ?????? ??????")
    void getMemo() {
        // Given
        Long schedule = scheduleService.saveSchedule(createScheduleCreation(), MEMBER_ID);
        MemoRequest memoRequest1 = new MemoRequest("memotitle1", "JIFEOgoiioghiohgieogio1");
        Long memo = scheduleService.saveMemo(schedule, memoRequest1, MEMBER_ID);

        // When
        MemoResponse actual = scheduleService.getMemo(schedule, memo, MEMBER_ID);

        // Then
        assertThat(actual.getTitle()).isEqualTo("memotitle1");
        assertThat(actual.getContent()).isEqualTo("JIFEOgoiioghiohgieogio1");
        assertThat(actual.getMemberSimpleResponse().getId()).isEqualTo(MEMBER_ID);
    }

    @Test
    @DisplayName("????????? ????????????")
    void modifyMemo() {
        // Given
        Long schedule = scheduleService.saveSchedule(createScheduleCreation(), MEMBER_ID);
        MemoRequest memoRequest = new MemoRequest("memotitle", "JIFEOgoiioghiohgieogio");
        Long memo = scheduleService.saveMemo(schedule, memoRequest, MEMBER_ID);

        // When
        MemoRequest updateRequest = new MemoRequest("????????????????????????????????????????????????", "Updated Memo Content");
        scheduleService.modifyMemo(schedule, memo, updateRequest, MEMBER_ID);

        // Then
        Memo updated = memoRepository.findById(memo).get();
        assertThat(updated.getContent()).isEqualTo(updateRequest.getContent());
        assertThat(updated.getTitle()).isEqualTo(updateRequest.getTitle());
    }

    @Test
    @DisplayName("????????? ????????????")
    void deleteMemo() {
        // Given
        Long schedule = scheduleService.saveSchedule(createScheduleCreation(), MEMBER_ID);
        MemoRequest memoRequest = new MemoRequest("memotitle", "JIFEOgoiioghiohgieogio");
        Long memo = scheduleService.saveMemo(schedule, memoRequest, MEMBER_ID);

        // When
        scheduleService.deleteMemo(schedule, memo, MEMBER_ID);

        // Then
        Optional<Memo> optionalMemo = memoRepository.findById(memo);
        assertThat(optionalMemo).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("?????????????????? ????????????")
    void createChecklist() {
        // Given
        Long schedule = scheduleService.saveSchedule(createScheduleCreation(), MEMBER_ID);
        ChecklistCreationRequest checklistCreationRequest = new ChecklistCreationRequest(0, "??? ?????? ??????");

        // When
        Long checklist = scheduleService.saveChecklist(schedule, checklistCreationRequest, MEMBER_ID);

        // Then
        assertThat(checklist).isEqualTo(1L);
    }

    @DisplayName("??????????????? ?????? ??? ??????")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void doCheck(boolean flag) {
        // Given
        Long schedule = scheduleService.saveSchedule(createScheduleCreation(), MEMBER_ID);
        ChecklistCreationRequest checklistCreationRequest = new ChecklistCreationRequest(1, "??? ?????? ??????");
        Long checklist = scheduleService.saveChecklist(schedule, checklistCreationRequest, MEMBER_ID);

        // When
        scheduleService.doCheck(schedule, checklist, MEMBER_ID, flag);
        Checklist saved = checklistRepository.findById(checklist).get();

        // Then
        assertThat(saved.isChecked()).isEqualTo(flag);
    }

    @Test
    @DisplayName("?????????????????? ????????????")
    void deleteChecklist() {
        // Given
        Long schedule = scheduleService.saveSchedule(createScheduleCreation(), MEMBER_ID);
        ChecklistCreationRequest checklistCreationRequest = new ChecklistCreationRequest(1, "??? ?????? ??????");
        Long checklist = scheduleService.saveChecklist(schedule, checklistCreationRequest, MEMBER_ID);

        // When
        scheduleService.deleteChecklist(schedule, checklist, MEMBER_ID);
        Optional<Checklist> saved = checklistRepository.findById(checklist);

        // Then
        assertThat(saved).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("????????? ????????????")
    void createVoting() {
        // Given
        Long schedule = scheduleService.saveSchedule(createScheduleCreation(), MEMBER_ID);
        VotingCreationRequest votingCreationRequest = new VotingCreationRequest("?????? ????????? ??????????", List.of("???", "???", "???", "???"), false);

        // When
        Long voting = scheduleService.saveVoting(schedule, votingCreationRequest, MEMBER_ID);
        Voting savedVoting = votingRepository.findById(voting).get();


        List<String> contentList = savedVoting.getVotingContents()
                .stream()
                .map(votingContent -> votingContent.getContent())
                .collect(Collectors.toList());

        // Then
        assertThat(savedVoting.getTitle()).isEqualTo("?????? ????????? ??????????");
        assertThat(savedVoting.isMultipleFlag()).isFalse();
        assertThat(contentList).containsExactly("???", "???", "???", "???");
    }

    @Test
    @DisplayName("?????? ????????? ????????????")
    void getVotings() {
        // Given
        Long schedule = scheduleService.saveSchedule(createScheduleCreation(), MEMBER_ID);
        VotingCreationRequest votingCreationRequest1 = new VotingCreationRequest("?????? ????????? ??????????", List.of("???", "???", "???", "???"), false);
        VotingCreationRequest votingCreationRequest2 = new VotingCreationRequest("?????? ?????? ??????", List.of("??????", "??????", "?????????", "??? ???"), true);

        Long voting1 = scheduleService.saveVoting(schedule, votingCreationRequest1, MEMBER_ID);
        Long voting2 = scheduleService.saveVoting(schedule, votingCreationRequest2, MEMBER_ID);

        // When
        List<VotingResponse> votingList = scheduleService.getVotingList(schedule, MEMBER_ID);

        List<String> titles = votingList.stream()
                .map(VotingResponse::getTitle)
                .collect(Collectors.toList());

        List<Integer> counts = votingList.stream()
                .map(VotingResponse::getNumOfTotalParticipants)
                .collect(Collectors.toList());

        // Then
        assertThat(titles).contains("?????? ????????? ??????????", "?????? ?????? ??????");
        assertThat(counts).containsExactly(0, 0);
    }

    @Test
    @DisplayName("????????? ?????? ????????? ????????????")
    void getVoting() {
        // Given
        Long schedule = scheduleService.saveSchedule(createScheduleCreation(), MEMBER_ID);
        VotingCreationRequest votingCreationRequest = new VotingCreationRequest("?????? ????????? ??????????", List.of("???", "???", "???", "???"), false);

        Long voting = scheduleService.saveVoting(schedule, votingCreationRequest, MEMBER_ID);

        // When
        VotingResponse response = scheduleService.getVoting(schedule, voting, MEMBER_ID);

        List<String> contents = response.getVotingContentResponses().stream()
                .map(VotingContentResponse::getContent)
                .collect(Collectors.toList());

        // Then
        assertThat(response.getId()).isEqualTo(voting);
        assertThat(response.getTitle()).isEqualTo("?????? ????????? ??????????");
        assertThat(response.getNumOfTotalParticipants()).isZero();
        assertThat(response.getMemberSimpleResponse().getId()).isEqualTo(MEMBER_ID);
        assertThat(contents).containsExactly("???", "???", "???", "???");
    }

    @Test
    @DisplayName("????????? ?????? ??????")
    void vote() {
        // Given
        Long schedule = scheduleService.saveSchedule(createScheduleCreation(), MEMBER_ID);
        VotingCreationRequest votingCreationRequest = new VotingCreationRequest("?????? ????????? ??????????", List.of("???", "???", "???", "???"), false);

        Long voting = scheduleService.saveVoting(schedule, votingCreationRequest, MEMBER_ID);
        Voting saved = votingRepository.findById(voting).get();

        List<Long> ids = saved.getVotingContents().stream()
                .map(VotingContent::getId)
                .collect(Collectors.toList());

        Map<Long, Boolean> votingMap = Map.of(ids.get(0), true, ids.get(1), false, ids.get(2), false, ids.get(3), false);
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

        assertThat(votingCountList).containsExactly(1, 0, 0, 0);
        assertThat(saved.getNumOfTotalParticipants()).isEqualTo(1);
    }

    private List<Long> getVotingMemberIds(VotingContent votingContent) {
        return votingContent.getVotingContentMembers()
                .stream()
                .map(VotingContentMember::getMemberId)
                .collect(Collectors.toList());
    }

    @Test
    @DisplayName("????????? ????????????")
    void deleteVoting() {
        // Given
        Long schedule = scheduleService.saveSchedule(createScheduleCreation(), MEMBER_ID);
        VotingCreationRequest votingCreationRequest = new VotingCreationRequest("?????? ????????? ??????????", List.of("???", "???", "???", "???"), false);
        Long voting = scheduleService.saveVoting(schedule, votingCreationRequest, MEMBER_ID);

        // When
        scheduleService.deleteVoting(schedule, voting, MEMBER_ID);
        Optional<Voting> actual = votingRepository.findById(voting);

        // Then
        assertThat(actual).isNotPresent();
    }
}
