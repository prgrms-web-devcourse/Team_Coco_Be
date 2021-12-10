package com.cocodan.triplan.schedule.dto.response;

import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.schedule.domain.Memo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemoDetailResponse {

    private final Long id;

    private final String title;

    private final String content;

    private final Long ownerId;

    private final String ownerNickname;

    private final int ownerAge;

    private final String ownerGender;

    public static MemoDetailResponse of(Memo memo, Member member) {
        return MemoDetailResponse.builder()
                .id(memo.getId())
                .title(memo.getTitle())
                .content(memo.getContent())
                .ownerId(member.getId())
                .ownerNickname(member.getNickname())
                .ownerGender(member.getGender().getTypeStr())
                .ownerAge(member.getAge())
                .build();
    }
}
