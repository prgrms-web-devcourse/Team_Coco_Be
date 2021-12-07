package com.cocodan.triplan.post.schedule.controller;

import com.cocodan.triplan.post.schedule.dto.SchedulePostResponse;
import com.cocodan.triplan.post.schedule.service.SchedulePostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.cocodan.triplan.post.schedule.controller.SchedulePostController.schedulePostBaseUri;

@RequestMapping(schedulePostBaseUri)
@RestController
public class SchedulePostController {

    public static final String schedulePostBaseUri = "/posts";

    private final SchedulePostService schedulePostService;

    public SchedulePostController(SchedulePostService schedulePostService) {
        this.schedulePostService = schedulePostService;
    }

    @GetMapping("/schedules")
    public List<SchedulePostResponse> scheduleList(@RequestParam(defaultValue = "0") Integer pageIndex) {
        // 1. 단순 전체조회로 시작 (최신순)
        List<SchedulePostResponse> posts = schedulePostService.getRecentSchedulePostList(pageIndex);
        return posts;

        // 2. 검색 조건 추가
        // 3. 검색 효율성 개선
    }
}
