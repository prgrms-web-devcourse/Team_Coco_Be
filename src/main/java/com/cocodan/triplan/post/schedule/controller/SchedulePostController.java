package com.cocodan.triplan.post.schedule.controller;

import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.post.schedule.dto.request.SchedulePostCreatieRequest;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostCreateResponse;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostResponse;
import com.cocodan.triplan.post.schedule.service.SchedulePostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<List<SchedulePostResponse>> scheduleList(@RequestParam(defaultValue = "0") Integer pageIndex) {
        // 1. 단순 전체조회로 시작 (최신순)
        List<SchedulePostResponse> posts = schedulePostService.getRecentSchedulePostList(pageIndex);
        return new ResponseEntity<>(posts, HttpStatus.OK);

        // 2. 검색 조건 추가
        // 3. 검색 효율성 개선
    }

    @PostMapping("/schedules")
    public ResponseEntity<SchedulePostCreateResponse> createSchedulePost(@RequestBody SchedulePostCreatieRequest request) {
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Long postId = schedulePostService.createSchedulePost(member, request);
        return new ResponseEntity<>(SchedulePostCreateResponse.from(postId), HttpStatus.OK);
    }

}
