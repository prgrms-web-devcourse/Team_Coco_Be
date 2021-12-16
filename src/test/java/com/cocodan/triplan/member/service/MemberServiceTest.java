package com.cocodan.triplan.member.service;

import com.cocodan.triplan.member.domain.Group;
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

import java.util.List;
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
    private static final String PASSWORD = "taid123";
    private static final String PASSWORD_ENCODE = "$2b$10$lphz9oOi2pcyg3AAj/boz..IkwiUhlQw4FLaVs.t24sifJkawzpYq";

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    MemberConverter converter;

    @Mock
    Pageable pageable;

    @Mock
    List<Member> members;

    Group group = new Group(1L, "USER_GROUP");

    Member member = new Member(EMAIL, NAME, BIRTH, GenderType.of(GENDER), NICKNAME, PROFILE_IMAGE, PASSWORD_ENCODE, group);

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
//        when(converter.toMemberEntity(EMAIL, NAME, BIRTH, GENDER, NICKNAME, PROFILE_IMAGE, memberService.getPasswordEncoder().encode(PASSWORD), 1L)).thenReturn(member);
//        when(memberRepository.save(member)).thenReturn(member);
//
//        // when
//        memberService.create(EMAIL, NAME, BIRTH, GENDER, NICKNAME, PROFILE_IMAGE, PASSWORD, 1L);
//
//        // then
//        verify(converter).toMemberEntity(EMAIL, NAME, BIRTH, GENDER, NICKNAME, PROFILE_IMAGE, memberService.getPasswordEncoder().encode(PASSWORD), 1L);
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
        when(memberRepository.findAllByNickname(NICKNAME)).thenReturn(members);

        // when
        memberService.findMemberByNickname(NICKNAME);

        // then
        verify(memberRepository).findAllByNickname(NICKNAME);
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