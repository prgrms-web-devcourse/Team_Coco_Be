package com.cocodan.triplan.schedule.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemoResponse {

    private final Long id;
    private final String title;
    private final String content;

    @Builder
    public MemoResponse(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
