package com.cocodan.triplan.schedule.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ChecklistTest {

    @Mock
    private Schedule schedule;

    private final int DAY = 0;

    private final String title = "title";

    @Test
    @DisplayName("체크리스트를 생성한다")
    void createChecklistTest() {
        // When
        Checklist checklist = createChecklist(title, DAY, schedule);

        // Then
        assertThat(checklist.getTitle()).isEqualTo(title);
        assertThat(checklist.getDay()).isZero();
        assertThat(checklist.isChecked()).isFalse();
    }

    private Checklist createChecklist(String title, int day, Schedule schedule) {
        return Checklist.builder()
                .title(title)
                .day(day)
                .schedule(schedule)
                .build();
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @DisplayName("체크리스트를 체크/해제 한다")
    void modifyChecklistTest(boolean flag) {
        // Given
        Checklist checklist = createChecklist(title, DAY, schedule);

        // When
        checklist.check(flag);

        // Then
        assertThat(checklist.isChecked()).isEqualTo(flag);
    }

//    checkNotNull(schedule, "Schedule is required");
//        checkNotNull(title, "title is required");
//        checkArgument(Range.closed(MIN_LENGTH, MAX_LENGTH).contains(title.length()));
//        checkArgument(Range.closed(0, DAY_MAX).contains(day));

    @Test
    @DisplayName("일정이 null이면 생성할 수 없다")
    void ScheduleNullCheckTest() {
        assertThatNullPointerException().isThrownBy(
                () -> createChecklist(title, DAY, null)
        ).withMessageContaining("Schedule");
    }

    @Test
    @DisplayName("제목이 null이면 생성할 수 없다")
    void TitleNullCheckTest() {
        assertThatNullPointerException().isThrownBy(
                () -> createChecklist(null, DAY, schedule)
        ).withMessageContaining("Title");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, Checklist.MAX_LENGTH + 1})
    @DisplayName("제목의 길이는 제약 조건을 벗어나면 생성할 수 없다")
    void TitleLengthTest(int length) {
        String title = " ".repeat(length);

        assertThatIllegalArgumentException().isThrownBy(
                () -> createChecklist(title, DAY, schedule)
        ).withMessageContaining("Title");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, Schedule.DAY_MAX + 1})
    @DisplayName("날짜의 제약조건을 벗어나면 생성할 수 없다")
    void dayRangeTest(int day) {
        assertThatIllegalArgumentException().isThrownBy(
                () -> createChecklist(title, day, schedule)
        ).withMessageContaining("Day");
    }
}