package com.cocodan.triplan.schedule.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@ExtendWith(MockitoExtension.class)
class VotingContentMemberTest {

    @Mock
    private VotingContent votingContent;

    private final Long MEMBER_ID = 44444L;

    @Test
    @DisplayName("votingContent가 null이면 생성할 수 없다")
    void votingContentNullCheck() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> createVotingContentMember(null, MEMBER_ID)
        ).withMessageContaining("VotingContent");
    }

    private VotingContentMember createVotingContentMember(VotingContent votingContent, Long memberId) {
        return VotingContentMember.builder()
                .votingContent(votingContent)
                .memberId(memberId)
                .build();
    }

    @Test
    @DisplayName("멤버 id가 null이면 생성할 수 없다")
    void memberIdNullCheck() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> createVotingContentMember(votingContent, null)
        ).withMessageContaining("Member");
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, 0L})
    @DisplayName("멤버 id가 0이하면 생성할 수 없다")
    void memberIdCheck(long memberId) {
        assertThatIllegalArgumentException().isThrownBy(
                () -> createVotingContentMember(votingContent, memberId)
        ).withMessageContaining("MemberId must be positive");
    }
}