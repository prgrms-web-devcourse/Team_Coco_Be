package com.cocodan.triplan.schedule.dto.request;

import com.cocodan.triplan.schedule.domain.Schedule;
import com.cocodan.triplan.spot.domain.Spot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DailyScheduleSpotCreationRequest {

    @Positive
    private long spotId;

    @Length(min = Spot.ADDRESS_MIN_LENGTH, max = Spot.ADDRESS_MAX_LENGTH)
    private String addressName;

    @Length(min = Spot.ADDRESS_MIN_LENGTH, max = Spot.ADDRESS_MAX_LENGTH)
    private String roadAddressName;

    @Length(min = Spot.PHONE_MIN_LENGTH, max = Spot.PHONE_MAX_LENGTH)
    private String phone;

    @Length(min = Spot.PLACE_NAME_MIN_LENGTH, max = Spot.PLACE_NAME_MAX_LENGTH)
    private String placeName;

    @NotNull
    private Position position;

    @Range(min = Schedule.DAY_MIN, max = Schedule.DAY_MAX)
    private int dateOrder;

    @Range(min = Schedule.NUM_OF_SPOT_MIN, max = Schedule.NUM_OF_SPOT_MAX)
    private int spotOrder;
}
