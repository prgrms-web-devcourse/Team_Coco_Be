package com.cocodan.triplan.schedule.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemoSimpleResponse {

    private final Long id;

    private final String title;

    private final String content;

    @Builder
    public MemoSimpleResponse(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
