package com.cocodan.triplan.schedule.dto.response;

import com.cocodan.triplan.schedule.domain.vo.Thema;
import com.cocodan.triplan.spot.dto.response.SpotSimple;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class ScheduleDetailResponse {
    private final Long id;
    private final String title;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final List<SpotSimple> spotSimpleList;
    private final List<Thema> themas;
    private final List<String> memberImageUrls;

    @Builder
    public ScheduleDetailResponse(Long id, String title, LocalDate startDate, LocalDate endDate, List<SpotSimple> spotSimpleList, List<Thema> themas, List<String> memberImageUrls) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.spotSimpleList = spotSimpleList;
        this.themas = themas;
        this.memberImageUrls = memberImageUrls;
    }
}
