package com.cocodan.triplan.post.schedule.dto.response;

import com.cocodan.triplan.member.domain.vo.GenderType;
import com.cocodan.triplan.post.schedule.domain.SchedulePostNestedComment;
import com.cocodan.triplan.post.schedule.vo.Ages;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class NestedCommentReadResponse {

    private Long nestedCommentId;

    private Long parentCommentId;

    private String nickname;

    private Ages ages;

    private GenderType gender;

    private String content;

    private String createdAt;

    private Long writerId; // 이 댓글을 쓴 사람의 memberId;

    private boolean schedulePostWriter; // 게시글의 작성자인지 여부

    public static NestedCommentReadResponse from(SchedulePostNestedComment nestedComment) {
        return NestedCommentReadResponse.builder()
                .nestedCommentId(nestedComment.getId())
                .parentCommentId(nestedComment.getParentComment().getId())
                .writerId(nestedComment.getMember().getId())
                .nickname(nestedComment.getMember().getNickname())
                .ages(Ages.from(nestedComment.getMember().getBirth()))
                .gender(nestedComment.getMember().getGender())
                .content(nestedComment.getContent())
                .createdAt(nestedComment.getCreatedDate().toString())
                .schedulePostWriter(
                        nestedComment.getParentComment()
                                .getSchedulePost()
                                .getMember()
                                .equals(nestedComment.getMember())
                )
                .build();
    }
}
