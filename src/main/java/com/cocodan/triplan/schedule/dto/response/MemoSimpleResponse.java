package com.cocodan.triplan.schedule.dto.response;

import com.cocodan.triplan.schedule.domain.Memo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class MemoSimpleResponse {

    private final Long id;

    private final String title;

    private final String content;

    public static MemoSimpleResponse from(Memo memo) {
        return MemoSimpleResponse.builder()
                .id(memo.getId())
                .title(memo.getTitle())
                .content(memo.getContent())
                .build();
    }
}
