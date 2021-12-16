package com.cocodan.triplan.schedule.dto.request;

import com.cocodan.triplan.spot.domain.Spot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DailyScheduleSpotCreationRequest {

    private long spotId;

    @NotBlank
    private String addressName;

    @NotBlank
    private String roadAddressName;

    @NotBlank
    private String placeName;

    @NotNull
    private Position position;

    @NotNull
    private LocalDate date;

    private int order;

}
