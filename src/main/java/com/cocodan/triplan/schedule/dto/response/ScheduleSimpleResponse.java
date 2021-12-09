package com.cocodan.triplan.schedule.dto.response;

import com.cocodan.triplan.schedule.domain.vo.Thema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class ScheduleSimpleResponse {

    private final Long id;
    private final String title;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final List<Thema> themas;

    @Builder
    public ScheduleSimpleResponse(Long id, String title, LocalDate startDate, LocalDate endDate, List<Thema> themas) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.themas = themas;
    }
}
