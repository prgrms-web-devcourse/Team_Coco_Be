package com.cocodan.triplan.schedule.dto.response;

import com.cocodan.triplan.schedule.domain.Checklist;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChecklistResponse {

    private Long id;

    private String content;

    private boolean checked;

    private int day;

    public static ChecklistResponse from(Checklist checklist) {
        return ChecklistResponse.builder()
                .id(checklist.getId())
                .content(checklist.getContent())
                .checked(checklist.isChecked())
                .day(checklist.getDay())
                .build();
    }
}
