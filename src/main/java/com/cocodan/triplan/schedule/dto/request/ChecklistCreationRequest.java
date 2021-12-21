package com.cocodan.triplan.schedule.dto.request;

import com.cocodan.triplan.schedule.domain.Checklist;
import com.cocodan.triplan.schedule.domain.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChecklistCreationRequest {

    @Range(max = Schedule.DAY_MAX)
    private int day;

    @Length(min = Checklist.MIN_LENGTH, max = Checklist.MAX_LENGTH)
    private String title;
}
