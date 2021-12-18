package com.cocodan.triplan.schedule.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class VotingTest {

    @Mock
    private Schedule schedule;

    private final String TITLE = "title";
    private final Long MEMBER_ID = 99999L;
    private final boolean MULTIPLE_FLAG = false;

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("복수 선택 여부 따라 투표를 생성한다")
    void createVotingTest(boolean flag) {
        Voting voting = createVoting(schedule, TITLE, MEMBER_ID, flag);

        assertThat(voting.getTitle()).isEqualTo(TITLE);
        assertThat(voting.getMemberId()).isEqualTo(MEMBER_ID);
        assertThat(voting.isMultipleFlag()).isEqualTo(flag);
    }

    private Voting createVoting(Schedule schedule, String title, Long memberId, boolean multipleFlag) {
        return Voting.builder()
                .schedule(schedule)
                .title(title)
                .memberId(memberId)
                .multipleFlag(multipleFlag)
                .build();
    }


    @Test
    @DisplayName("일정이 null이면 생성할 수 없다")
    void scheduleNullCheckTest() {
        assertThatNullPointerException().isThrownBy(
                () -> createVoting(null, TITLE, MEMBER_ID, MULTIPLE_FLAG)
        ).withMessageContaining("Schedule");
    }

    @Test
    @DisplayName("제목이 null이면 생성할 수 없다")
    void titleNullCheck() {
        assertThatNullPointerException().isThrownBy(
                () -> createVoting(schedule, null, MEMBER_ID, MULTIPLE_FLAG)
        ).withMessageContaining("Title");
    }

    @Test
    @DisplayName("멤버 id가 null이면 생성할 수 없다")
    void memberIdNullCheck() {
        assertThatNullPointerException().isThrownBy(
                () -> createVoting(schedule, TITLE, null, MULTIPLE_FLAG)
        ).withMessageContaining("Member");
    }

    @ParameterizedTest
    @ValueSource(ints = {Voting.TITLE_MIN_LENGTH - 1, Voting.TITLE_MAX_LENGTH + 1})
    @DisplayName("제목의 길이 제약조건을 벗어나면 생성할 수 없다")
    void titleLengthCheck(int length) {
        String title = " ".repeat(length);

        assertThatIllegalArgumentException().isThrownBy(
                () -> createVoting(schedule, title, MEMBER_ID, MULTIPLE_FLAG)
        );
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, 0L})
    @DisplayName("멤버 id가 0이하면 생성할 수 없다")
    void memberIdCheck(long memberId) {
        assertThatIllegalArgumentException().isThrownBy(
                () -> createVoting(schedule, TITLE, memberId, MULTIPLE_FLAG)
        ).withMessageContaining("MemberId");
    }

    @Test
    @DisplayName("투표는 최소 하나는 선택해야 한다")
    void voteMinimumOne() {
        Voting voting = createVoting(schedule, TITLE, MEMBER_ID, MULTIPLE_FLAG);

        assertThatIllegalArgumentException().isThrownBy(
                () -> voting.vote(Map.of(), MEMBER_ID)
        );
    }

    @Test
    @DisplayName("단일 투표 모드에선 복수 선택이 불가능하다")
    void multipleIsImpossibleWithSingleVotingMode() {
        Voting voting = createVoting(schedule, TITLE, MEMBER_ID, MULTIPLE_FLAG);

        assertThatIllegalArgumentException().isThrownBy(
                () -> voting.vote(Map.of(1L,true, 2L,true), MEMBER_ID)
        );
    }
}