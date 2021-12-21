package com.cocodan.triplan.schedule.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@ExtendWith(MockitoExtension.class)
class ScheduleTest {

    private final String TITLE = "title";
    private final Long MEMBER_ID = 1L;
    private final LocalDate START_DATE = LocalDate.of(2021, 12, 16);
    private final LocalDate END_DATE = LocalDate.of(2021, 12, 18);

    private Schedule createSchedule(String title, Long memberId, LocalDate startDate, LocalDate endDate) {
        return Schedule.builder()
                .memberId(memberId)
                .title(title)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    @Test
    @DisplayName("title이 null이면 생성할 수 없다")
    void titleNullCheck() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> createSchedule(null, MEMBER_ID, START_DATE, END_DATE)
        );
    }

    @Test
    @DisplayName("startDate가 null이면 생성할 수 없다")
    void startDatedNullCheck() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> createSchedule(TITLE, MEMBER_ID, null, END_DATE)
        );
    }

    @Test
    @DisplayName("endDate가 null이면 생성할 수 없다")
    void endDateNullCheck() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> createSchedule(TITLE, MEMBER_ID, START_DATE, null)
        );
    }

    @Test
    @DisplayName("memberId이 null이면 생성할 수 없다")
    void memberIdNullCheck() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> createSchedule(TITLE, null, START_DATE, END_DATE)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {Schedule.TITLE_MIN_LENGTH - 1, Schedule.TITLE_MAX_LENGTH + 1})
    @DisplayName("일정의 제목 제약조건을 벗어나면 생성할 수 없다")
    void titleLengthCheck(int length) {
        String title = " ".repeat(length);

        assertThatIllegalArgumentException().isThrownBy(
                () -> createSchedule(title, MEMBER_ID, START_DATE, END_DATE)
        );
    }
}