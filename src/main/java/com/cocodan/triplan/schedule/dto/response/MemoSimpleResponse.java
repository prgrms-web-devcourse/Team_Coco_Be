package com.cocodan.triplan.schedule.dto.response;

import com.cocodan.triplan.schedule.domain.Memo;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemoSimpleResponse {

    private final Long id;

    private final String title;

    private final String content;

    @Builder
    private MemoSimpleResponse(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public static MemoSimpleResponse from(Memo memo) {
        return MemoSimpleResponse.builder()
                .id(memo.getId())
                .title(memo.getTitle())
                .content(memo.getContent())
                .build();
    }
}
