package com.cocodan.triplan.schedule.service;

import com.cocodan.triplan.member.dto.response.MemberCreateResponse;
import com.cocodan.triplan.member.service.MemberService;
import com.cocodan.triplan.schedule.domain.Memo;
import com.cocodan.triplan.schedule.dto.request.MemoRequest;
import com.cocodan.triplan.schedule.dto.response.MemoResponse;
import com.cocodan.triplan.schedule.repository.MemoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MemoServiceTest {

    private Long MEMBER_ID;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private MemoService memoService;

    @Autowired
    private MemoRepository memoRepository;

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
    @DisplayName("메모를 추가한다")
    void createMemo() {
        // Given
        Long schedule = scheduleService.saveSchedule(TestDataFactory.createScheduleCreation(), MEMBER_ID);
        MemoRequest memoRequest = new MemoRequest("memotitle", "JIFEOgoiioghiohgieogio");

        // When
        Long memo = memoService.saveMemo(schedule, memoRequest, MEMBER_ID);

        Memo actual = memoRepository.findById(memo).get();

        // Then
        assertThat(actual.getTitle()).isEqualTo("memotitle");
        assertThat(actual.getContent()).isEqualTo("JIFEOgoiioghiohgieogio");
    }

    @Test
    @DisplayName("메모 목록을 조회한다")
    void getMemos() {
        // Given
        Long schedule = scheduleService.saveSchedule(TestDataFactory.createScheduleCreation(), MEMBER_ID);
        MemoRequest memoRequest1 = new MemoRequest("memotitle1", "JIFEOgoiioghiohgieogio1");
        MemoRequest memoRequest2 = new MemoRequest("memotitle2", "JIFEOgoiioghiohgieogio2");
        MemoRequest memoRequest3 = new MemoRequest("memotitle3", "JIFEOgoiioghiohgieogio3");

        Long memo1 = memoService.saveMemo(schedule, memoRequest1, MEMBER_ID);
        Long memo2 = memoService.saveMemo(schedule, memoRequest2, MEMBER_ID);
        Long memo3 = memoService.saveMemo(schedule, memoRequest3, MEMBER_ID);

        // When
        List<MemoResponse> memos = memoService.getMemos(schedule, MEMBER_ID);

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
    @DisplayName("메모를 상세 조회 한다")
    void getMemo() {
        // Given
        Long schedule = scheduleService.saveSchedule(TestDataFactory.createScheduleCreation(), MEMBER_ID);
        MemoRequest memoRequest1 = new MemoRequest("memotitle1", "JIFEOgoiioghiohgieogio1");
        Long memo = memoService.saveMemo(schedule, memoRequest1, MEMBER_ID);

        // When
        MemoResponse actual = memoService.getMemo(schedule, memo, MEMBER_ID);

        // Then
        assertThat(actual.getTitle()).isEqualTo("memotitle1");
        assertThat(actual.getContent()).isEqualTo("JIFEOgoiioghiohgieogio1");
        assertThat(actual.getMemberSimpleResponse().getId()).isEqualTo(MEMBER_ID);
    }

    @Test
    @DisplayName("메모를 수정한다")
    void modifyMemo() {
        // Given
        Long schedule = scheduleService.saveSchedule(TestDataFactory.createScheduleCreation(), MEMBER_ID);
        MemoRequest memoRequest = new MemoRequest("memotitle", "JIFEOgoiioghiohgieogio");
        Long memo = memoService.saveMemo(schedule, memoRequest, MEMBER_ID);

        // When
        MemoRequest updateRequest = new MemoRequest("핫둘셋넷다여일여아열핫둘셋넷닷엿", "Updated Memo Content");
        memoService.modifyMemo(schedule, memo, updateRequest, MEMBER_ID);

        // Then
        Memo updated = memoRepository.findById(memo).get();
        assertThat(updated.getContent()).isEqualTo(updateRequest.getContent());
        assertThat(updated.getTitle()).isEqualTo(updateRequest.getTitle());
    }

    @Test
    @DisplayName("메모를 삭제한다")
    void deleteMemo() {
        // Given
        Long schedule = scheduleService.saveSchedule(TestDataFactory.createScheduleCreation(), MEMBER_ID);
        MemoRequest memoRequest = new MemoRequest("memotitle", "JIFEOgoiioghiohgieogio");
        Long memo = memoService.saveMemo(schedule, memoRequest, MEMBER_ID);

        // When
        memoService.deleteMemo(schedule, memo, MEMBER_ID);

        // Then
        Optional<Memo> optionalMemo = memoRepository.findById(memo);
        assertThat(optionalMemo).isEqualTo(Optional.empty());
    }

}
