package com.cocodan.triplan.schedule.dto.response;

import com.cocodan.triplan.schedule.domain.Checklist;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class ChecklistResponse {

    private final Long id;

    private final String content;

    private final boolean checked;

    private final int day;

    public static ChecklistResponse from(Checklist checklist) {
        return ChecklistResponse.builder()
                .id(checklist.getId())
                .content(checklist.getContent())
                .checked(checklist.isChecked())
                .day(checklist.getDay())
                .build();
    }
}
