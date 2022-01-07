package com.cocodan.triplan.schedule.service;

import com.cocodan.triplan.member.dto.response.MemberCreateResponse;
import com.cocodan.triplan.member.service.MemberService;
import com.cocodan.triplan.schedule.domain.Voting;
import com.cocodan.triplan.schedule.domain.VotingContent;
import com.cocodan.triplan.schedule.domain.VotingContentMember;
import com.cocodan.triplan.schedule.dto.request.VotingCreationRequest;
import com.cocodan.triplan.schedule.dto.request.VotingRequest;
import com.cocodan.triplan.schedule.dto.response.VotingContentResponse;
import com.cocodan.triplan.schedule.dto.response.VotingResponse;
import com.cocodan.triplan.schedule.repository.VotingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class VotingServiceTest {

    private Long MEMBER_ID;

    @Autowired
    private VotingService votingService;

    @Autowired
    private VotingRepository votingRepository;

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
    @DisplayName("투표를 추가한다")
    void createVoting() {
        // Given
        Long schedule = scheduleService.saveSchedule(TestDataFactory.createScheduleCreation(), MEMBER_ID);
        VotingCreationRequest votingCreationRequest = new VotingCreationRequest("무슨 요일날 갈까요?", List.of("월", "화", "수", "목"), false);

        // When
        Long voting = votingService.saveVoting(schedule, votingCreationRequest, MEMBER_ID);
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
    @DisplayName("투표 목록을 조회한다")
    void getVotings() {
        // Given
        Long schedule = scheduleService.saveSchedule(TestDataFactory.createScheduleCreation(), MEMBER_ID);
        VotingCreationRequest votingCreationRequest1 = new VotingCreationRequest("무슨 요일날 갈까요?", List.of("월", "화", "수", "목"), false);
        VotingCreationRequest votingCreationRequest2 = new VotingCreationRequest("어디 여행 갈래", List.of("서울", "부산", "제주도", "안 가"), true);

        Long voting1 = votingService.saveVoting(schedule, votingCreationRequest1, MEMBER_ID);
        Long voting2 = votingService.saveVoting(schedule, votingCreationRequest2, MEMBER_ID);

        // When
        List<VotingResponse> votingList = votingService.getVotingList(schedule, MEMBER_ID);

        List<String> titles = votingList.stream()
                .map(VotingResponse::getTitle)
                .collect(Collectors.toList());

        List<Integer> counts = votingList.stream()
                .map(VotingResponse::getNumOfTotalParticipants)
                .collect(Collectors.toList());

        // Then
        assertThat(titles).contains("무슨 요일날 갈까요?", "어디 여행 갈래");
        assertThat(counts).containsExactly(0, 0);
    }

    @Test
    @DisplayName("투표의 상세 정보를 조회한다")
    void getVoting() {
        // Given
        Long schedule = scheduleService.saveSchedule(TestDataFactory.createScheduleCreation(), MEMBER_ID);
        VotingCreationRequest votingCreationRequest = new VotingCreationRequest("무슨 요일날 갈까요?", List.of("월", "화", "수", "목"), false);

        Long voting = votingService.saveVoting(schedule, votingCreationRequest, MEMBER_ID);

        // When
        VotingResponse response = votingService.getVoting(schedule, voting, MEMBER_ID);

        List<String> contents = response.getVotingContentResponses().stream()
                .map(VotingContentResponse::getContent)
                .collect(Collectors.toList());

        // Then
        assertThat(response.getId()).isEqualTo(voting);
        assertThat(response.getTitle()).isEqualTo("무슨 요일날 갈까요?");
        assertThat(response.getNumOfTotalParticipants()).isZero();
        assertThat(response.getMemberSimpleResponse().getId()).isEqualTo(MEMBER_ID);
        assertThat(contents).containsExactly("월", "화", "수", "목");
    }

    @Test
    @DisplayName("투표를 행사 한다")
    void vote() {
        // Given
        Long schedule = scheduleService.saveSchedule(TestDataFactory.createScheduleCreation(), MEMBER_ID);
        VotingCreationRequest votingCreationRequest = new VotingCreationRequest("무슨 요일날 갈까요?", List.of("월", "화", "수", "목"), false);

        Long voting = votingService.saveVoting(schedule, votingCreationRequest, MEMBER_ID);
        Voting saved = votingRepository.findById(voting).get();

        List<Long> ids = saved.getVotingContents().stream()
                .map(VotingContent::getId)
                .collect(Collectors.toList());

        Map<Long, Boolean> votingMap = Map.of(ids.get(0), true, ids.get(1), false, ids.get(2), false, ids.get(3), false);
        VotingRequest votingRequest = new VotingRequest(votingMap);

        // When
        votingService.doVote(schedule, voting, votingRequest, MEMBER_ID);

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
    @DisplayName("투표를 삭제한다")
    void deleteVoting() {
        // Given
        Long schedule = scheduleService.saveSchedule(TestDataFactory.createScheduleCreation(), MEMBER_ID);
        VotingCreationRequest votingCreationRequest = new VotingCreationRequest("무슨 요일날 갈까요?", List.of("월", "화", "수", "목"), false);
        Long voting = votingService.saveVoting(schedule, votingCreationRequest, MEMBER_ID);

        // When
        votingService.deleteVoting(schedule, voting, MEMBER_ID);
        Optional<Voting> actual = votingRepository.findById(voting);

        // Then
        assertThat(actual).isNotPresent();
    }
}
