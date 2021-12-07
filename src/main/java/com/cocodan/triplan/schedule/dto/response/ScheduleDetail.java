package com.cocodan.triplan.schedule.dto.response;

import com.cocodan.triplan.schedule.domain.vo.Thema;
import com.cocodan.triplan.spot.dto.response.SpotSimple;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class ScheduleDetail {
    private Long id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<SpotSimple> spotSimpleList;
    private List<Thema> themas;
    private List<String> memberImageUrls;

    @Builder
    public ScheduleDetail(Long id, String title, LocalDate startDate, LocalDate endDate, List<SpotSimple> spotSimpleList, List<Thema> themas, List<String> memberImageUrls) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.spotSimpleList = spotSimpleList;
        this.themas = themas;
        this.memberImageUrls = memberImageUrls;
    }
}
