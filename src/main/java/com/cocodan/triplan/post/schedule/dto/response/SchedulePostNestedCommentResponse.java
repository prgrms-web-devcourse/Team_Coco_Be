package com.cocodan.triplan.post.schedule.dto.response;

import com.cocodan.triplan.member.domain.vo.GenderType;
import com.cocodan.triplan.member.dto.response.MemberGetOneResponse;
import com.cocodan.triplan.post.schedule.domain.SchedulePostComment;
import com.cocodan.triplan.post.schedule.domain.SchedulePostNestedComment;
import com.cocodan.triplan.post.schedule.vo.Ages;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SchedulePostNestedCommentResponse {

    private Long id;

    private Long parentCommentId;

    private String nickname;

    private Ages ages;

    private GenderType gender;

    private String content;

    private LocalDateTime createdAt;

    private Long writerId; // 이 댓글을 쓴 사람의 memberId;

    private boolean schedulePostWriter; // 게시글의 작성자인지 여부

    public static SchedulePostNestedCommentResponse of(
            SchedulePostComment parentComment,
            SchedulePostNestedComment comment,
            MemberGetOneResponse member,
            boolean schedulePostWriter
    ) {
        return SchedulePostNestedCommentResponse.builder()
                .id(comment.getId())
                .parentCommentId(parentComment.getId())
                .writerId(member.getId())
                .nickname(member.getNickname())
                .ages(Ages.from(member.getBirth()))
                .gender(member.getGender())
                .content(comment.getContent())
                .createdAt(comment.getCreatedDate())
                .schedulePostWriter(schedulePostWriter)
                .build();
    }
}
