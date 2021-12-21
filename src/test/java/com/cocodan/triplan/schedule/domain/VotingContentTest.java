package com.cocodan.triplan.schedule.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class VotingContentTest {

    @Mock
    private Voting voting;

    private final String CONTENT = "Content";

    private final Long MEMBER_ID = 111L;

    @Test
    @DisplayName("voting가 null이면 생성할 수 없다")
    void votingNullCheck() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> createVotingContent(null, CONTENT)
        ).withMessageContaining("Voting");
    }

    private VotingContent createVotingContent(Voting voting, String content) {
        return VotingContent.builder()
                .voting(voting)
                .content(content)
                .build();
    }

    @Test
    @DisplayName("내용이 null이면 생성할 수 없다")
    void contentNullCheck() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> createVotingContent(voting, null)
        ).withMessageContaining("Content");
    }

    @ParameterizedTest
    @ValueSource(ints = {VotingContent.MIN_LENGTH - 1, VotingContent.MAX_LENGTH + 1})
    @DisplayName("내용의 길이 제약조건을 벗어나면 생성할 수 없다")
    void contentLengthCheck(int length) {
        String content = " ".repeat(length);

        assertThatIllegalArgumentException().isThrownBy(
                () -> createVotingContent(voting, content)
        );
    }

    @Test
    @DisplayName("투표를 하면 투표한 멤버에 추가된다")
    void voteTest() {
        // Given
        VotingContent votingContent = createVotingContent(voting, "할 사람?");

        // When
        vote(votingContent, MEMBER_ID);

        List<Long> ids = votingContent.getVotingContentMembers().stream()
                .map(VotingContentMember::getMemberId)
                .collect(Collectors.toList());

        // Then
        assertThat(ids).contains(MEMBER_ID);
    }

    @Test
    @DisplayName("이미 투표를 한 사람일 경우 투표 멤버는 변함이 없다")
    void voteByAlreadyVotingMember() {
        // Given
        VotingContent votingContent = createVotingContent(voting, "할 사람?");

        // When
        vote(votingContent, MEMBER_ID);
        vote(votingContent, MEMBER_ID);
        vote(votingContent, MEMBER_ID);

        List<Long> ids = votingContent.getVotingContentMembers().stream()
                .map(VotingContentMember::getMemberId)
                .collect(Collectors.toList());

        // Then
        assertThat(ids.size()).isEqualTo(1);
        assertThat(ids).contains(MEMBER_ID);
    }

    private void vote(VotingContent votingContent, Long memberId) {
        votingContent.vote(memberId);
    }

    @Test
    @DisplayName("투표를 취소하면 투표한 사람 목록에서 삭제된다")
    void cancelTest() {
        // Given
        VotingContent votingContent = createVotingContent(voting, "할 사람?");

        // When
        vote(votingContent, MEMBER_ID);

        votingContent.cancel(MEMBER_ID);

        List<Long> ids = votingContent.getVotingContentMembers().stream()
                .map(VotingContentMember::getMemberId)
                .collect(Collectors.toList());

        // Then
        assertThat(ids.size()).isZero();
    }

    @ParameterizedTest
    @ValueSource(longs = {10L, 11L, 12L, 13L, 14L, 15L})
    @DisplayName("투표한 사람 수를 기록한다")
    void numOfParticipantsCheck(long id) {
        // Given
        VotingContent votingContent = createVotingContent(voting, CONTENT);

        // When
        for (long i = id; i < 16L; i++) {
            vote(votingContent, i);
        }

        // Then
        assertThat(votingContent.getNumOfParticipants() + id).isEqualTo(16);
    }
}