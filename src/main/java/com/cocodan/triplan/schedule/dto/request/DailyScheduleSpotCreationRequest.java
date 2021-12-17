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

    @Length(min = Spot.PHONE_MIN_LENGTH, max = Spot.PHONE_MAX_LENGTH)
    private String phone;

    @NotBlank
    private String placeName;

    @NotNull
    private Position position;

    private int dateOrder;

    private int spotOrder;

}
