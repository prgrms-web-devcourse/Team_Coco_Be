package com.cocodan.triplan.schedule.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Position {

    public static final int LAT_MIN = 33;
    public static final int LAT_MAX = 43;

    public static final int LNG_MIN = 124;
    public static final int LNG_MAX = 132;

    @Range(min = LAT_MIN, max = LAT_MAX)
    private double lat;

    @Range(min = LNG_MIN, max = LNG_MAX)
    private double lng;
}
