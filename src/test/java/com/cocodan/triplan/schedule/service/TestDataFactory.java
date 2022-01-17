package com.cocodan.triplan.schedule.service;

import com.cocodan.triplan.schedule.dto.request.DailyScheduleSpotCreationRequest;
import com.cocodan.triplan.schedule.dto.request.Position;
import com.cocodan.triplan.schedule.dto.request.ScheduleCreationRequest;

import java.time.LocalDate;
import java.util.List;

public class TestDataFactory {

    public static ScheduleCreationRequest createScheduleCreation() {
        return new ScheduleCreationRequest("title", LocalDate.of(2021, 12, 1), LocalDate.of(2021, 12, 3), List.of("activity", "food"),
                List.of(new DailyScheduleSpotCreationRequest(11L, "address1", "roadAddress1", "010-1111-2222", "불국사1", new Position(37.1234, 125.3333), 1, 1),
                        new DailyScheduleSpotCreationRequest(21L, "address2", "roadAddress2", "010-1111-2223", "불국사2", new Position(37.1234, 125.3333), 1, 2),
                        new DailyScheduleSpotCreationRequest(31L, "address3", "roadAddress3", "010-1111-2224", "불국사3", new Position(37.1234, 125.3333), 1, 3),
                        new DailyScheduleSpotCreationRequest(41L, "address4", "roadAddress4", "010-1111-2225", "불국사4", new Position(37.1234, 125.3333), 2, 1),
                        new DailyScheduleSpotCreationRequest(51L, "address5", "roadAddress5", "010-1111-2226", "불국사5", new Position(37.1234, 125.3333), 2, 2),
                        new DailyScheduleSpotCreationRequest(61L, "address6", "roadAddress6", "010-1111-2227", "불국사6", new Position(37.1234, 125.3333), 2, 3),
                        new DailyScheduleSpotCreationRequest(71L, "address7", "roadAddress7", "010-1111-2228", "불국사7", new Position(37.1234, 125.3333), 3, 1),
                        new DailyScheduleSpotCreationRequest(81L, "address8", "roadAddress8", "010-1111-2229", "불국사8", new Position(37.1234, 125.3333), 3, 2)
                ),
                List.of());
    }


}
