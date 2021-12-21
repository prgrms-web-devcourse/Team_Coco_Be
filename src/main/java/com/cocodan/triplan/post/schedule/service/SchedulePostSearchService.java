package com.cocodan.triplan.post.schedule.service;

import com.cocodan.triplan.post.schedule.domain.SchedulePost;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostResponse;
import com.cocodan.triplan.post.schedule.repository.SchedulePostRepository;
import com.cocodan.triplan.post.schedule.vo.SchedulePostSortingRule;
import com.cocodan.triplan.schedule.domain.vo.Theme;
import com.cocodan.triplan.spot.domain.vo.City;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchedulePostSearchService {

    private final SchedulePostRepository schedulePostRepository;

    private final SchedulePostService schedulePostService;

    @Transactional
    public List<SchedulePostResponse> getSchedulePosts(
            String search,
            City city,
            Theme theme,
            SchedulePostSortingRule sortRule
    ) {
        List<SchedulePost> result = schedulePostRepository.search(search, city, theme, sortRule);
        return schedulePostService.convertToSchedulePostResponseList(result);
    }
}
