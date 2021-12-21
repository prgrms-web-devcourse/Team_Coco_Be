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
class MemoTest {

    @Mock
    private Schedule schedule;

    private final Long MEMBER_ID = 1000L;
    private final String TITLE = "title";
    private final String CONTENT = "content";

    @Test
    @DisplayName("메모를 생성한다")
    void createMemoTest() {
        Memo memo = createMemo(TITLE, CONTENT, schedule, MEMBER_ID);

        assertThat(memo.getTitle()).isEqualTo(TITLE);
        assertThat(memo.getContent()).isEqualTo(CONTENT);
        assertThat(memo.getMemberId()).isEqualTo(MEMBER_ID);
    }

    private Memo createMemo(String title, String content, Schedule schedule, Long memberId) {
        return Memo.builder()
                .memberId(memberId)
                .title(title)
                .content(content)
                .schedule(schedule)
                .build();
    }

    @Test
    @DisplayName("메모를 수정한다")
    void modifyMemoTest() {
        // Given
        Memo memo = createMemo(TITLE, CONTENT, schedule, MEMBER_ID);

        // When
        memo.modify("updated title", "updated content");

        // Then
        assertThat(memo.getTitle()).isEqualTo("updated title");
        assertThat(memo.getContent()).isEqualTo("updated content");
    }

    @Test
    @DisplayName("일정이 null이면 생성할 수 없다")
    void scheduleNullCheckTest() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> createMemo(TITLE, CONTENT, null, MEMBER_ID)
        ).withMessageContaining("Schedule");
    }

    @Test
    @DisplayName("제목이 null이면 생성할 수 없다")
    void titleNullCheck() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> createMemo(null, CONTENT, schedule, MEMBER_ID)
        ).withMessageContaining("Title");
    }

    @Test
    @DisplayName("내용이 null이면 생성할 수 없다")
    void contentNullCheck() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> createMemo(TITLE, null, schedule, MEMBER_ID)
        ).withMessageContaining("Content");
    }

    @Test
    @DisplayName("멤버 id가 null이면 생성할 수 없다")
    void memberIdNullCheck() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> createMemo(TITLE, CONTENT, schedule, null)
        ).withMessageContaining("Member");
    }

    @ParameterizedTest
    @ValueSource(ints = {Memo.TITLE_MAX_LENGTH + 1, Memo.TITLE_MIN_LENGTH - 1})
    @DisplayName("제목의 길이 제약조건을 벗어나면 생성할 수 없다")
    void titleLengthCheck(int length) {
        String title = " ".repeat(length);

        assertThatIllegalArgumentException().isThrownBy(
                () -> createMemo(title, CONTENT, schedule, MEMBER_ID)
        ).withMessageContaining("Title length");
    }

    @ParameterizedTest
    @ValueSource(ints = {Memo.CONTENT_MAX_LENGTH + 1, Memo.CONTENT_MIN_LENGTH - 1})
    @DisplayName("본문의 길이 제약조건을 벗어나면 생성할 수 없다")
    void contentLengthCheck(int length) {
        String content = " ".repeat(length);

        assertThatIllegalArgumentException().isThrownBy(
                () -> createMemo(TITLE, content, schedule, MEMBER_ID)
        ).withMessageContaining("Content length");
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, 0L})
    @DisplayName("멤버 id가 0이하면 생성할 수 없다")
    void memberIdCheck(long memberId) {
        assertThatIllegalArgumentException().isThrownBy(
                () -> createMemo(TITLE, CONTENT, schedule, memberId)
        ).withMessageContaining("MemberId");
    }
}