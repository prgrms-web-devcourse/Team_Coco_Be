package com.cocodan.triplan.schedule.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemoResponse {

    private final String title;
    private final String content;

    @Builder
    public MemoResponse(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
