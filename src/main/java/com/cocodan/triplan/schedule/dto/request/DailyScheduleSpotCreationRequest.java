package com.cocodan.triplan.schedule.dto.request;

import com.cocodan.triplan.spot.domain.Spot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DailyScheduleSpotCreationRequest {

    private Long spotId;

    private String addressName;

    private String roadAddressName;

    @Length(min = Spot.PHONE_LENGTH, max = Spot.PHONE_LENGTH)
    private String phone;

    private String placeName;

    private Position position;

    private LocalDate date;

    private int order;

}
