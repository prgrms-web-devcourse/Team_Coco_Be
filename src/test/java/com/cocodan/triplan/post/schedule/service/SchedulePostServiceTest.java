package com.cocodan.triplan.post.schedule.service;

import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.member.domain.vo.GenderType;
import com.cocodan.triplan.member.dto.response.MemberCreateResponse;
import com.cocodan.triplan.member.dto.response.MemberGetOneResponse;
import com.cocodan.triplan.member.service.MemberService;
import com.cocodan.triplan.post.schedule.domain.SchedulePost;
import com.cocodan.triplan.post.schedule.dto.request.SchedulePostCreatieRequest;
import com.cocodan.triplan.spot.domain.vo.City;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class SchedulePostServiceTest {

    @Autowired
    SchedulePostService schedulePostService;

    @Autowired
    MemberService memberService;

    private static Long testMemberId;
    private static final String EMAIL = "KimLeePark@gmail.com";
    private static final String NAME = "김이박";
    private static final String PHONE = "01077775555";
    private static final String BIRTH = "19901111";
    private static final String NICKNAME = "TestNickname";
    private static final String PROFILE_IMAGE = "https://wwww.someonesownserver.org/img/1";

    @BeforeAll
    void setup() {
        testMemberId = memberService.create(
                EMAIL,
                NAME,
                PHONE,
                BIRTH,
                GenderType.MALE.getTypeStr(),
                NICKNAME,
                PROFILE_IMAGE
                        ).getId();
    }

    @Test
    void getRecentSchedulePostList() {
        // TODO: 2012.12.07 Teru - 게시글 조회 테스트 추가
    }

    @Test
    @DisplayName("여행 공유 게시글 생성 로직이 정상적으로 동작한다.")
    @Transactional
    void createSchedulePost() {
        SchedulePostCreatieRequest request = SchedulePostCreatieRequest.builder()
                .title("1번 여행!")
                .content("어디로든 갔다옴~")
                .city("서울")
                .scheduleId(1L)
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
    }
}