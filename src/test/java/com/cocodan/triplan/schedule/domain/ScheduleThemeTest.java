package com.cocodan.triplan.schedule.domain;

import com.cocodan.triplan.schedule.domain.vo.Theme;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ScheduleThemeTest {

    @Mock
    private Schedule schedule;

    private final Theme theme = Theme.ALL;

    @Test
    @DisplayName("일정이 null이면 생성할 수 없다")
    void scheduleNullCheck() {
        assertThatNullPointerException().isThrownBy(
                () -> createScheduleTheme(null, theme)
        );
    }

    private ScheduleTheme createScheduleTheme(Schedule schedule, Theme theme) {
        return new ScheduleTheme(schedule, theme);
    }

    @Test
    @DisplayName("테마가 null이면 생성할 수 없다")
    void themeNullCheck() {
        assertThatNullPointerException().isThrownBy(
                () -> createScheduleTheme(schedule, null)
        );
    }
}