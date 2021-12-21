package com.cocodan.triplan.post.schedule.repository;

import com.cocodan.triplan.post.schedule.domain.SchedulePost;
import com.cocodan.triplan.post.schedule.vo.SchedulePostSortingRule;
import com.cocodan.triplan.schedule.domain.vo.Theme;
import com.cocodan.triplan.spot.domain.vo.City;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomSchedulePostRepository {

    List<SchedulePost> search(String search,
                              City city,
                              Theme theme,
                              SchedulePostSortingRule sortRule
    );
}
