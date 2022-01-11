package com.cocodan.triplan.schedule.service;

import com.cocodan.triplan.exception.common.NotFoundException;
import com.cocodan.triplan.exception.common.NotIncludeException;
import com.cocodan.triplan.schedule.ScheduleConverter;
import com.cocodan.triplan.schedule.domain.Checklist;
import com.cocodan.triplan.schedule.domain.Schedule;
import com.cocodan.triplan.schedule.dto.request.ChecklistCreationRequest;
import com.cocodan.triplan.schedule.dto.response.ChecklistResponse;
import com.cocodan.triplan.schedule.repository.ChecklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChecklistService {

    private final ChecklistRepository checklistRepository;

    private final ScheduleService scheduleService;

    private final ScheduleConverter converter;

    @Transactional
    public Long saveChecklist(Long scheduleId, ChecklistCreationRequest checklistCreationRequest, Long memberId) {
        Schedule schedule = scheduleService.findScheduleById(scheduleId);

        scheduleService.validateScheduleMember(scheduleId, memberId);

        Checklist checklist = converter.createChecklist(checklistCreationRequest, schedule);

        return checklistRepository.save(checklist).getId();
    }

    @Transactional(readOnly = true)
    public List<ChecklistResponse> getChecklists(Long scheduleId, Long memberId) {
        scheduleService.validateScheduleMember(scheduleId, memberId);

        return checklistRepository.findByScheduleId(scheduleId).stream()
                .map(ChecklistResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void doCheck(Long scheduleId, Long checklistId, Long memberId, boolean flag) {
        Checklist checklist = findChecklistById(checklistId);

        validateScheduleChecklist(scheduleId, checklistId);

        scheduleService.validateScheduleMember(scheduleId, memberId);

        checklist.check(flag);
    }

    @Transactional
    public void deleteChecklist(Long scheduleId, Long checklistId, Long memberId) {
        Checklist checklist = findChecklistById(checklistId);

        validateScheduleChecklist(scheduleId, checklistId);

        scheduleService.validateScheduleMember(scheduleId, memberId);

        checklistRepository.delete(checklist);
    }

    private Checklist findChecklistById(Long checklistId) {
        return checklistRepository.findById(checklistId)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format(
                        "{} :: Checklist Not Found. " +
                                "There does not exist a Checklist with the given ID : {}",
                        LocalDateTime.now(), checklistId
                )));
    }

    private void validateScheduleChecklist(Long scheduleId, Long checklistId) {
        boolean notExists = !checklistRepository.existsByIdAndScheduleId(checklistId, scheduleId);

        if (notExists) {
            throw new NotIncludeException(Schedule.class, Checklist.class, scheduleId, checklistId);
        }
    }

}
