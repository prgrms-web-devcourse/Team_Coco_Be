package com.cocodan.triplan.schedule.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemoResponse {

    private final Long id;
    private final String title;
    private final String content;

    private final Long ownerId;
    private final String owenrNickname;
    private final int age;
    private final String gender;

    @Builder
    public MemoResponse(Long id, String title, String content, Long ownerId, String owenrNickname, int age, String gender) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.ownerId = ownerId;
        this.owenrNickname = owenrNickname;
        this.age = age;
        this.gender = gender;
    }
}
