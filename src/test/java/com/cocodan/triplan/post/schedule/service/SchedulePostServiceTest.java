package com.cocodan.triplan.post.schedule.service;

import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.member.domain.vo.GenderType;
import com.cocodan.triplan.member.service.MemberService;
import com.cocodan.triplan.post.schedule.domain.SchedulePost;
import com.cocodan.triplan.post.schedule.dto.request.SchedulePostCreatieRequest;
import com.cocodan.triplan.spot.domain.vo.City;
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

    @Test
    void getRecentSchedulePostList() {
        // TODO: 2012.12.07 Teru - 게시글 조회 테스트 추가
    }

    @Test
    @DisplayName("여행 공유 게시글 생성 로직이 정상적으로 동작한다.")
    @Transactional
    void createSchedulePost() {
        // TODO: 2021.12.07 Teru - 멤버 도메인 구현되면 멤버 서비스 통해서 생성 및 사용하도록 수정 + 게시글 조회 후 멥버 확인 로직 추가?
        Member memberCreation1 = Member.builder()
                .email("Kim3@gmail.com")
                .name("Kim kimkim")
                .phoneNumber("010-1111-2222")
                .birth("19901111")
                .gender(GenderType.MALE)
                .nickname("KIMKIMKIM")
                .profileImage("www.kimkim.org/img/1")
                .build();
        Long memberId = memberService.save(memberCreation1);
        Member member = memberService.findById(memberId);
        SchedulePostCreatieRequest request = SchedulePostCreatieRequest.builder()
                .title("1번 여행!")
                .content("어디로든 갔다옴~")
                .city("서울")
                .scheduleId(1L)
                .build();
        Long createdSchedulePostId = schedulePostService.createSchedulePost(member, request);
        SchedulePost post1 = schedulePostService.findById(createdSchedulePostId);

        assertThat(post1.getMemberId()).isEqualTo(memberId);
        assertThat(post1.getTitle()).isEqualTo("1번 여행!");
        assertThat(post1.getContent()).isEqualTo("어디로든 갔다옴~");
        assertThat(post1.getCity()).isEqualTo(City.SEOUL);


        Member memberCreation2 = Member.builder()
                .email("Park3@gmail.com")
                .name("Park parkpark")
                .phoneNumber("010-3333-4444")
                .birth("20001212")
                .gender(GenderType.MALE)
                .nickname("PARKPARKPARK")
                .profileImage("www.PARKPARK.org/img/2")
                .build();
        Long memberId2 = memberService.save(memberCreation2);
        Member member2 = memberService.findById(memberId2);
        SchedulePostCreatieRequest request2 = SchedulePostCreatieRequest.builder()
                .title("2번 여행!")
                .content("이세상끝까지~")
                .city("부산")
                .scheduleId(2L)
                .build();
        Long createdSchedulePostId2 = schedulePostService.createSchedulePost(member2, request2);
        SchedulePost post2 = schedulePostService.findById(createdSchedulePostId2);

        assertThat(post2.getMemberId()).isEqualTo(memberId2);
        assertThat(post2.getTitle()).isEqualTo("2번 여행!");
        assertThat(post2.getContent()).isEqualTo("이세상끝까지~");
        assertThat(post2.getCity()).isEqualTo(City.BUSAN);
    }
}