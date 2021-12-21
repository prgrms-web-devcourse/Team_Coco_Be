package com.cocodan.triplan.schedule.domain;

import com.cocodan.triplan.spot.domain.Spot;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DailyScheduleSpotTest {

    @Mock
    private Schedule schedule;

    private final Long SPOT_ID = 1L;
    private final String PLACE_NAME = "Place Name";
    private final int DATE_ORDER = 1;
    private final int SPOT_ORDER = 1;

    @Test
    @DisplayName("DailyScheduleSpot을 생성한다")
    void createDailyScheduleSpotTest() {
        DailyScheduleSpot dailyScheduleSpot = createDailyScheduleSpot(schedule, SPOT_ID, SPOT_ORDER, DATE_ORDER, PLACE_NAME);

        assertThat(dailyScheduleSpot.getDateOrder()).isEqualTo(DATE_ORDER);
        assertThat(dailyScheduleSpot.getSpotId()).isEqualTo(SPOT_ID);
        assertThat(dailyScheduleSpot.getSpotOrder()).isEqualTo(SPOT_ORDER);
        assertThat(dailyScheduleSpot.getPlaceName()).isEqualTo(PLACE_NAME);
    }

    private DailyScheduleSpot createDailyScheduleSpot(Schedule schedule, Long spotId, int spotOrder, int dateOrder, String placeName) {
        return DailyScheduleSpot.builder()
                .placeName(placeName)
                .spotOrder(spotOrder)
                .spotId(spotId)
                .schedule(schedule)
                .dateOrder(dateOrder)
                .build();
    }

    @Test
    @DisplayName("일정이 null이면 생성할 수 없다")
    void scheduleNullCheck() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> createDailyScheduleSpot(null, SPOT_ID, SPOT_ORDER, DATE_ORDER, PLACE_NAME)
        );
    }

    @Test
    @DisplayName("spotId가 null이면 생성할 수 없다")
    void spotIdNullCheck() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> createDailyScheduleSpot(schedule, null, SPOT_ORDER, DATE_ORDER, PLACE_NAME)
        );
    }

    @Test
    @DisplayName("placeName이 null이면 생성할 수 없다")
    void placeNameNullCheck() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> createDailyScheduleSpot(schedule, SPOT_ID, SPOT_ORDER, DATE_ORDER, null)
        );
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, 0L})
    @DisplayName("spotId는 0이하면 생성할 수 없다")
    void spotIdPositiveCheck(long spotId) {
        assertThatIllegalArgumentException().isThrownBy(
                () ->  createDailyScheduleSpot(schedule, spotId, SPOT_ORDER, DATE_ORDER, PLACE_NAME)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {Spot.PLACE_NAME_MIN_LENGTH -1 , Spot.PLACE_NAME_MAX_LENGTH+1})
    @DisplayName("placeName 길이를 벗어나면 생성할 수 없다")
    void placeNameLengthInvalidCheck(int length){
        String placeName = " ".repeat(length);

        assertThatIllegalArgumentException().isThrownBy(
                () ->  createDailyScheduleSpot(schedule, SPOT_ID, SPOT_ORDER, DATE_ORDER, placeName)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {Schedule.DAY_MIN -1, Schedule.DAY_MAX+1})
    @DisplayName("dateOrder의 범위를 벗어나면 생성할 수 없다")
    void dateOrderInvalidCheck(int dateOrder){
        assertThatIllegalArgumentException().isThrownBy(
                () ->  createDailyScheduleSpot(schedule, SPOT_ID, SPOT_ORDER, dateOrder, PLACE_NAME)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {Schedule.NUM_OF_SPOT_MIN-1, Schedule.NUM_OF_SPOT_MAX+1})
    @DisplayName("spot의 순서를 벗어나면 생성할 수 없다")
    void spotOrderInvalidCheck(int spotOrder){
        assertThatIllegalArgumentException().isThrownBy(
                () ->  createDailyScheduleSpot(schedule, SPOT_ID, spotOrder, DATE_ORDER, PLACE_NAME)
        );
    }

}