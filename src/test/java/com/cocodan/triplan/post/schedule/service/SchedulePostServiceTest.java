package com.cocodan.triplan.post.schedule.service;

import com.cocodan.triplan.member.domain.vo.GenderType;
import com.cocodan.triplan.member.service.MemberService;
import com.cocodan.triplan.post.schedule.domain.SchedulePost;
import com.cocodan.triplan.post.schedule.dto.request.SchedulePostCreateRequest;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostResponse;
import com.cocodan.triplan.post.schedule.vo.SchedulePostSortingRule;
import com.cocodan.triplan.schedule.domain.vo.Thema;
import com.cocodan.triplan.schedule.dto.request.DailyScheduleSpotCreationRequest;
import com.cocodan.triplan.schedule.dto.request.ScheduleCreationRequest;
import com.cocodan.triplan.schedule.service.ScheduleService;
import com.cocodan.triplan.spot.domain.vo.City;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SchedulePostServiceTest {

    @Autowired
    SchedulePostService schedulePostService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    MemberService memberService;

    private static Long testMemberId;
    private static final String EMAIL = "KimLeePark@gmail.com";
    private static final String NAME = "김이박";
    private static final String PHONE = "01077775555";
    private static final String BIRTH = "19901111";
    private static final String GENDER = GenderType.MALE.getTypeStr();
    private static final String NICKNAME = "TestNickname";
    private static final String PROFILE_IMAGE = "https://wwww.someonesownserver.org/img/1";

    @BeforeAll
    void setup() {
        testMemberId = memberService.create(
                EMAIL,
                NAME,
                PHONE,
                BIRTH,
                GENDER,
                NICKNAME,
                PROFILE_IMAGE
                        ).getId();
    }

    @Test
    @DisplayName("생성된 여행 공유 게시글 리스트를 정상적으로 조회 할 수 있다.")
    @Transactional
    void getRecentSchedulePostList() {
        // TODO: 2012.12.07 Teru - 게시글 조회 테스트 추가
        // 여행 생성
        ScheduleCreationRequest scheduleCreationRequest = createScheduleCreation();
        Long createdScheduleId = scheduleService.createSchedule(scheduleCreationRequest, testMemberId);

        // 여행 공유 게시글 생성
        SchedulePostCreateRequest request = SchedulePostCreateRequest.builder()
                .title("1번 여행 게시글")
                .content("1번 여행 게시글 본문")
                .city("서울")
                .scheduleId(createdScheduleId)
                .build();
        Long createdSchedulePostId = schedulePostService.createSchedulePost(testMemberId, request);

        // 여행 공유 게시글 조회
        List<SchedulePostResponse> posts = schedulePostService.getSchedulePostList("", City.ALL, Thema.ALL, SchedulePostSortingRule.RECENT, 0);
        assertThat(posts.size()).isEqualTo(1);
        assertThat(posts.get(0).getProfileImageUrl()).isEqualTo(PROFILE_IMAGE);
        assertThat(posts.get(0).getNickname()).isEqualTo(NICKNAME);
        assertThat(posts.get(0).getTitle()).isEqualTo("1번 여행 게시글");
        assertThat(posts.get(0).getGenderType().getTypeStr()).isEqualTo(GENDER);
        assertThat(posts.get(0).getCity()).isEqualTo(City.SEOUL);
        assertThat(posts.get(0).getStartDate()).isEqualTo(LocalDate.of(2021, 12, 1));
        assertThat(posts.get(0).getEndDate()).isEqualTo(LocalDate.of(2021, 12, 3));
        assertThat(posts.get(0).getThemes()).contains(Thema.ACTIVITY, Thema.FOOD);
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
    @DisplayName("여행 공유 게시글 생성 로직이 정상적으로 동작한다.")
    @Transactional
    void createSchedulePost() {
        ScheduleCreationRequest scheduleCreationRequest = createScheduleCreation();
        Long createdScheduleId = scheduleService.createSchedule(scheduleCreationRequest, testMemberId);

        SchedulePostCreateRequest request = SchedulePostCreateRequest.builder()
                .title("1번 여행!")
                .content("어디로든 갔다옴~")
                .city("서울")
                .scheduleId(createdScheduleId)
                .build();
        Long createdSchedulePostId = schedulePostService.createSchedulePost(testMemberId, request);
        SchedulePost post1 = schedulePostService.findById(createdSchedulePostId);

        assertThat(post1.getMemberId()).isEqualTo(testMemberId);
        assertThat(memberService.getOne(post1.getMemberId()).getEmail()).isEqualTo(EMAIL);
        assertThat(memberService.getOne(post1.getMemberId()).getName()).isEqualTo(NAME);
        assertThat(memberService.getOne(post1.getMemberId()).getPhoneNumber()).isEqualTo(PHONE);
        assertThat(memberService.getOne(post1.getMemberId()).getBirth()).isEqualTo(BIRTH);
        assertThat(memberService.getOne(post1.getMemberId()).getNickname()).isEqualTo(NICKNAME);
        assertThat(memberService.getOne(post1.getMemberId()).getProfileImage()).isEqualTo(PROFILE_IMAGE);
        assertThat(post1.getTitle()).isEqualTo("1번 여행!");
        assertThat(post1.getContent()).isEqualTo("어디로든 갔다옴~");
        assertThat(post1.getCity()).isEqualTo(City.SEOUL);
        assertThat(post1.getSchedule().getId()).isEqualTo(createdScheduleId);
    }
}