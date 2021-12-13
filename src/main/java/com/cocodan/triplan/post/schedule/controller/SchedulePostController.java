package com.cocodan.triplan.post.schedule.controller;

import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.member.domain.vo.GenderType;
import com.cocodan.triplan.post.schedule.dto.request.SchedulePostLikeRequest;
import com.cocodan.triplan.post.schedule.dto.request.SchedulePostRequest;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostCreateResponse;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostDetailResponse;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostLikeResponse;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostResponse;
import com.cocodan.triplan.post.schedule.service.SchedulePostService;
import com.cocodan.triplan.post.schedule.vo.SchedulePostSortingRule;
import com.cocodan.triplan.schedule.domain.vo.Theme;
import com.cocodan.triplan.spot.domain.vo.City;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.cocodan.triplan.post.schedule.controller.SchedulePostController.schedulePostBaseUri;

@Api(tags = "Schedule Post")
@RequestMapping(schedulePostBaseUri)
@RestController
public class SchedulePostController {

    public static final String schedulePostBaseUri = "/posts";

    private final SchedulePostService schedulePostService;

    public SchedulePostController(SchedulePostService schedulePostService) {
        this.schedulePostService = schedulePostService;
    }

    @ApiOperation("여행 일정 공유 게시글 (조건별)목록 조회")
    @GetMapping("/schedules")
    public ResponseEntity<List<SchedulePostResponse>> schedulePostList(
            @RequestParam(defaultValue = "0") Integer pageIndex,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "전체") String searchingCity,
            @RequestParam(defaultValue = "ALL") String searchingTheme,
            @RequestParam(defaultValue = "최신순") String sorting
    ) {
        City city = City.from(searchingCity);
        Theme theme = Theme.valueOf(searchingTheme);
        SchedulePostSortingRule sortRule = SchedulePostSortingRule.of(sorting);

        List<SchedulePostResponse> schedulePostList
                = schedulePostService.getSchedulePosts(search, city, theme, sortRule, pageIndex);

        return ResponseEntity.ok(schedulePostList);
        // TODO: 검색 효율성 개선
    }

    @ApiOperation("여행 일정 공유 게시글 작성")
    @PostMapping("/schedules")
    public ResponseEntity<SchedulePostCreateResponse> createSchedulePost(@RequestBody SchedulePostRequest request) {
        // Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // TODO: TP-68 티켓에 의한 임시 코드 -> 추후 위의 comment-out 된 것으로 다시 교체
        Member member = new Member(1L, "Temporary@temp.com", "Temporary User", "01011110000", "19000101", GenderType.MALE, "Temporary", "https://Temporary.temp.tem/img/temp-1");

        Long postId = schedulePostService.createSchedulePost(member.getId(), request);
        return ResponseEntity.ok(SchedulePostCreateResponse.from(postId));
    }

    @ApiOperation("특정 여행 일정 공유 게시글 상세조회")
    @GetMapping("/schedules/{schedulePostId}")
    public ResponseEntity<SchedulePostDetailResponse> detailSchedulePost(@PathVariable Long schedulePostId) {
        SchedulePostDetailResponse schedulePostDetail = schedulePostService.getSchedulePostDetail(schedulePostId);
        return ResponseEntity.ok(schedulePostDetail);
    }

    @ApiOperation("(자신이 작성한)특정 여행 공유 게시글 삭제")
    @DeleteMapping("/schedules/{schedulePostId}")
    public ResponseEntity<Void> deleteSchedulePost(@PathVariable Long schedulePostId) {
        // Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // TODO: TP-68 티켓에 의한 임시 코드 -> 추후 위의 comment-out 된 것으로 다시 교체
        Member member = new Member(1L, "Temporary@temp.com", "Temporary User", "01011110000", "19000101", GenderType.MALE, "Temporary", "https://Temporary.temp.tem/img/temp-1");

        schedulePostService.deleteSchedulePost(member.getId(), schedulePostId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("여행 공유 게시글 수정")
    @PutMapping("/schedules/{schedulePostId}")
    public ResponseEntity<Void> modifySchedulePost(@PathVariable Long schedulePostId, @RequestBody SchedulePostRequest request) {
        // TODO: 2021.12.10 Teru - 별도의 Util class 를 만들어 요청을 보내는 유저 정보 받아오는 메서드 작성하기
        // Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // TODO: TP-68 티켓에 의한 임시 코드 -> 추후 위의 comment-out 된 것으로 다시 교체
        Member member = new Member(1L, "Temporary@temp.com", "Temporary User", "01011110000", "19000101", GenderType.MALE, "Temporary", "https://Temporary.temp.tem/img/temp-1");

        schedulePostService.modifySchedulePost(member.getId(), request);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("여행 공유 게시글 좋아요 토글")
    @PostMapping("/schedules/{schedulePostId}/liked")
    public ResponseEntity<SchedulePostLikeResponse> changeLikeFlag(@PathVariable("schedulePostId") Long schedulePostId, @RequestBody SchedulePostLikeRequest request) {
        // Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // TODO: TP-68 티켓에 의한 임시 코드 -> 추후 위의 comment-out 된 것으로 다시 교체
        Member member = new Member(1L, "Temporary@temp.com", "Temporary User", "01011110000", "19000101", GenderType.MALE, "Temporary", "https://Temporary.temp.tem/img/temp-1");

        Long likeCount = schedulePostService.toggleSchedulePostLiked(member.getId(), request);
        return ResponseEntity.ok(new SchedulePostLikeResponse(likeCount));
    }

    @ApiOperation("좋아요 누른 게시글만 조회")
    @GetMapping("/schedules/liked")
    public ResponseEntity<List<SchedulePostResponse>> likedSchedulePostList() {
        // TODO: 2021.12.14 Teru - Pageable 적용 여부 고민... 한다면 어떻게?
        // Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // TODO: TP-68 티켓에 의한 임시 코드 -> 추후 위의 comment-out 된 것으로 다시 교체
        Member member = new Member(1L, "Temporary@temp.com", "Temporary User", "01011110000", "19000101", GenderType.MALE, "Temporary", "https://Temporary.temp.tem/img/temp-1");

        return ResponseEntity.ok(schedulePostService.getLikedSchedulePosts(member.getId()));
    }
}
