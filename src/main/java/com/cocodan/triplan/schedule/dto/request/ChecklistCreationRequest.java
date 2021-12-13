package com.cocodan.triplan.schedule.dto.request;

import com.cocodan.triplan.schedule.domain.Checklist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChecklistCreationRequest {

    private LocalDate date;

    @Length(min = Checklist.MIN_LENGTH, max = Checklist.MAX_LENGTH)
    private String title;
}
