package com.cocodan.triplan.schedule;

import com.cocodan.triplan.schedule.domain.*;
import com.cocodan.triplan.schedule.domain.vo.Theme;
import com.cocodan.triplan.schedule.dto.request.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduleConverter {

    public Checklist createChecklist(ChecklistCreationRequest request, Schedule schedule) {
        return Checklist.builder()
                .title(request.getTitle())
                .schedule(schedule)
                .day(request.getDay())
                .build();
    }

    public Memo createMemo(MemoRequest request, Schedule schedule, Long memberId) {
        return Memo.builder()
                .schedule(schedule)
                .title(request.getTitle())
                .content(request.getContent())
                .memberId(memberId)
                .build();
    }

    public Voting createVoting(VotingCreationRequest request, Long memberId, Schedule schedule) {
        return Voting.builder()
                .schedule(schedule)
                .title(request.getTitle())
                .multipleFlag(request.isMultipleFlag())
                .memberId(memberId)
                .build();
    }

    public void createVotingContents(VotingCreationRequest request, Voting voting) {
        request.getContents()
                .forEach(content -> createVotingContent(voting, content));
    }

    public Schedule createSchedule(ScheduleCreationRequest request, Long memberId) {
        return Schedule.builder()
                .title(request.getTitle())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .memberId(memberId)
                .build();
    }

    public void addAllScheduleMembers(ScheduleCreationRequest request, Schedule save) {
        for (Long id : request.getIdsOfFriends()) {
            createScheduleMember(save, id);
        }
    }

    public void createScheduleMember(Schedule schedule, long memberId) {
        ScheduleMember.builder()
                .memberId(memberId)
                .schedule(schedule)
                .build();
    }

    public void createScheduleDailySpots(List<DailyScheduleSpotCreationRequest> requests, Schedule schedule) {
        requests.forEach(spotRequest -> createDailyScheduleSpot(spotRequest, schedule));
    }

    public void createScheduleThemes(List<String> themes, Schedule schedule) {
        themes.stream()
                .map(s -> Theme.from(s.toUpperCase()))
                .forEach(theme -> new ScheduleTheme(schedule, theme));
    }

    private void createVotingContent(Voting voting, String votingContentName) {
        VotingContent.builder()
                .voting(voting)
                .content(votingContentName)
                .build();
    }

    private void createDailyScheduleSpot(DailyScheduleSpotCreationRequest request, Schedule schedule) {
        DailyScheduleSpot.builder()
                .spotId(request.getSpotId())
                .placeName(request.getPlaceName())
                .dateOrder(request.getDateOrder())
                .spotOrder(request.getSpotOrder())
                .schedule(schedule)
                .build();
    }
}
