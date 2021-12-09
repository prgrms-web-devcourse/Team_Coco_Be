package com.cocodan.triplan.schedule.dto.response;

import com.cocodan.triplan.schedule.domain.vo.Theme;
import com.cocodan.triplan.spot.dto.response.SpotSimple;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class ScheduleDetailResponse {
    private Long id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<SpotSimple> spotSimpleList;
    private List<Theme> thema;
    private List<String> memberImageUrls;

    @Builder
    public ScheduleDetailResponse(Long id, String title, LocalDate startDate, LocalDate endDate, List<SpotSimple> spotSimpleList, List<Theme> thema, List<String> memberImageUrls) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.spotSimpleList = spotSimpleList;
        this.thema = thema;
        this.memberImageUrls = memberImageUrls;
    }
}
