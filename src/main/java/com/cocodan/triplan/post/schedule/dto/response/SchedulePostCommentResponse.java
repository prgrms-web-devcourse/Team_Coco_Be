package com.cocodan.triplan.post.schedule.dto.response;

import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.member.domain.vo.GenderType;
import com.cocodan.triplan.post.schedule.domain.SchedulePostComment;
import com.cocodan.triplan.post.schedule.vo.Ages;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class SchedulePostCommentResponse {

    private Long commentId;

    private String nickname;

    private Ages ages;

    private GenderType gender;

    private String content;

    private String createdAt;

    private List<SchedulePostNestedCommentResponse> nestedComments;

    private Long writerId; // 이 댓글을 쓴 사람의 memberId

    private boolean schedulePostWriter; // 게시글의 작성자인지 여부

    public static SchedulePostCommentResponse of(
            SchedulePostComment comment,
            List<SchedulePostNestedCommentResponse> nestedComments
    ) {
        Member member = comment.getMember();
        return SchedulePostCommentResponse.builder()
                .commentId(comment.getId())
                .writerId(member.getId())
                .nickname(member.getNickname())
                .ages(Ages.from(member.getBirth()))
                .gender(member.getGender())
                .content(comment.getContent())
                .createdAt(comment.getCreatedDate().toString())
                .nestedComments(nestedComments)
                .schedulePostWriter(member.equals(comment.getSchedulePost().getMember()))
                .build();
    }
}
