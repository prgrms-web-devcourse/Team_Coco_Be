package com.cocodan.triplan.post.schedule.dto.response;

import com.cocodan.triplan.member.domain.vo.GenderType;
import com.cocodan.triplan.member.dto.response.MemberGetOneResponse;
import com.cocodan.triplan.post.schedule.domain.SchedulePostComment;
import com.cocodan.triplan.post.schedule.vo.Ages;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class SchedulePostCommentResponse {

    private Long id;

    private String nickname;

    private Ages ages;

    private GenderType gender;

    private String content;

    private LocalDateTime createdAt;

    private List<SchedulePostNestedCommentResponse> nestedComments;

    private Long writerId; // 이 댓글을 쓴 사람의 memberId

    private boolean schedulePostWriter; // 게시글의 작성자인지 여부

    public static SchedulePostCommentResponse of(
            Long commentId,
            SchedulePostComment comment,
            MemberGetOneResponse member,
            boolean schedulePostWriter,
            List<SchedulePostNestedCommentResponse> nestedComments
    ) {
        return SchedulePostCommentResponse.builder()
                .id(commentId)
                .writerId(member.getId())
                .nickname(member.getNickname())
                .ages(Ages.from(member.getBirth()))
                .gender(member.getGender())
                .content(comment.getContent())
                .createdAt(comment.getCreatedDate())
                .nestedComments(nestedComments)
                .schedulePostWriter(schedulePostWriter)
                .build();
    }
}