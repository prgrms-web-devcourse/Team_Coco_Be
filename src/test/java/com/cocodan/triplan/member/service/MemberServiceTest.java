package com.cocodan.triplan.member.service;

import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.member.domain.vo.GenderType;
import com.cocodan.triplan.member.dto.response.MemberGetOneResponse;
import com.cocodan.triplan.member.repository.MemberRepository;
import com.cocodan.triplan.util.MemberConverter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@Slf4j
class MemberServiceTest {
    private static final Long MEMBER_ID = 1L;
    private static final String EMAIL = "ddkk94@naver.com";
    private static final String NAME =  "Taid";
    private static final String BIRTH = "20211015";
    private static final String GENDER = GenderType.MALE.getTypeStr();
    private static final String NICKNAME = "TTTaid";
    private static final String PROFILE_IMAGE = "/images/test";

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    MemberConverter converter;

    @Mock
    Pageable pageable;

    @Mock
    Page<Member> members;

    Member member = new Member(MEMBER_ID, EMAIL, NAME, BIRTH, GenderType.of(GENDER), NICKNAME, PROFILE_IMAGE);

    MemberGetOneResponse findResponse = MemberGetOneResponse.builder()
            .id(MEMBER_ID)
            .email(EMAIL)
            .name(NAME)
            .birth(BIRTH)
            .gender(GenderType.of(GENDER))
            .nickname(NICKNAME)
            .profileImage(PROFILE_IMAGE)
            .build();

//    @Test
//    void create() {
//        // given
//        when(converter.toMemberEntity(EMAIL, NAME, PHONE_NUMBER, BIRTH, GENDER, NICKNAME, PROFILE_IMAGE, PASSWORD, 1L)).thenReturn(member);
//        when(memberRepository.save(member)).thenReturn(member);
//
//        // when
//        memberService.create(EMAIL, NAME, PHONE_NUMBER, BIRTH, GENDER, NICKNAME, PROFILE_IMAGE, PASSWORD, 1L);
//
//        // then
//        verify(converter).toMemberEntity(EMAIL, NAME, PHONE_NUMBER, BIRTH, GENDER, NICKNAME, PROFILE_IMAGE, PASSWORD, 1L);
//        verify(memberRepository).save(member);
//        verify(converter).toMemberCreateResponse(member);
//    }

    @Test
    void getOne() {
        // given
        when(memberRepository.findById(MEMBER_ID)).thenReturn(Optional.of(member));
        when(converter.toMemberFindResponse(member)).thenReturn(findResponse);

        // when
        memberService.getOne(MEMBER_ID);

        // then
        verify(memberRepository).findById(MEMBER_ID);
    }

    @Test
    void getAll() {
        // given
        when(memberRepository.findAll(pageable)).thenReturn(members);

        // when
        memberService.getAll(pageable);

        // then
        verify(memberRepository).findAll(pageable);
    }

    @Test
    void update() {
        // given
        when(memberRepository.findById(MEMBER_ID)).thenReturn(Optional.of(member));

        // when
        memberService.update(MEMBER_ID, NAME, NICKNAME, PROFILE_IMAGE);

        // then
        verify(memberRepository).findById(MEMBER_ID);
        verify(converter).toMemberUpdateResponse(member);
    }

    @Test
    void delete() {
        // given
        when(memberRepository.findById(MEMBER_ID)).thenReturn(Optional.of(member));

        // when
        memberService.delete(MEMBER_ID);

        // then
        verify(converter).toMemberDeleteResponse(member);
        verify(memberRepository).delete(member);
    }
}