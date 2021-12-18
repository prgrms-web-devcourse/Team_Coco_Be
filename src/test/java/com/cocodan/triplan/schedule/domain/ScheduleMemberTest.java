package com.cocodan.triplan.schedule.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ScheduleMemberTest {

    @Mock
    private Schedule schedule;

    private final Long MEMBER_ID = 9999L;

    @Test
    @DisplayName("일정이 null이면 생성할 수 없다")
    void scheduleNullCheck() {
        assertThatNullPointerException().isThrownBy(
                () -> createScheduleMember(null, MEMBER_ID)
        );
    }

    private ScheduleMember createScheduleMember(Schedule schedule, Long memberId) {
        return ScheduleMember.builder()
                .schedule(schedule)
                .memberId(memberId)
                .build();
    }

    @Test
    @DisplayName("멤버 id가 null이면 생성할 수 없다")
    void memberIdNullCheck() {
        assertThatNullPointerException().isThrownBy(
                () -> createScheduleMember(schedule, null)
        ).withMessageContaining("Member");
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, 0L})
    @DisplayName("멤버 id가 0이하면 생성할 수 없다")
    void memberIdCheck(long memberId) {
        assertThatIllegalArgumentException().isThrownBy(
                () -> createScheduleMember(schedule, memberId)
        ).withMessageContaining("MemberId must be positive");
    }
}