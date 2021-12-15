package com.cocodan.triplan.post.schedule.controller;

import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.member.domain.vo.GenderType;
import com.cocodan.triplan.post.schedule.dto.request.SchedulePostCommentRequest;
import com.cocodan.triplan.post.schedule.dto.request.SchedulePostLikeRequest;
import com.cocodan.triplan.post.schedule.domain.SchedulePost;
import com.cocodan.triplan.post.schedule.dto.request.SchedulePostRequest;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostCommentResponse;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostCreateResponse;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostDetailResponse;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostLikeResponse;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostResponse;
import com.cocodan.triplan.post.schedule.service.SchedulePostSearchService;
import com.cocodan.triplan.post.schedule.service.SchedulePostService;
import com.cocodan.triplan.post.schedule.vo.SchedulePostSortingRule;
import com.cocodan.triplan.schedule.domain.vo.Theme;
import com.cocodan.triplan.spot.domain.vo.City;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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

    private final SchedulePostSearchService schedulePostSearchService;

    public SchedulePostController(SchedulePostService schedulePostService, SchedulePostSearchService schedulePostSearchService) {
        this.schedulePostService = schedulePostService;
        this.schedulePostSearchService = schedulePostSearchService;
    }

    @ApiOperation("여행 일정 공유 게시글 (조건별)목록 조회")
    @GetMapping("/schedules")
    public ResponseEntity<Page<SchedulePostResponse>> schedulePostList(
            Pageable pageable,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "전체") String searchingCity,
            @RequestParam(defaultValue = "ALL") String searchingTheme,
            @RequestParam(defaultValue = "최신순") String sorting
    ) {
        City city = City.from(searchingCity);
        Theme theme = Theme.valueOf(searchingTheme);
        SchedulePostSortingRule sortRule = SchedulePostSortingRule.of(sorting);
        Page<SchedulePostResponse> schedulePosts = schedulePostSearchService.getSchedulePosts(search, city, theme, sortRule, pageable);

        return ResponseEntity.ok(schedulePosts);
    }

    @ApiOperation("여행 일정 공유 게시글 작성")
    @PostMapping("/schedules")
    public ResponseEntity<SchedulePostCreateResponse> createSchedulePost(@RequestBody SchedulePostRequest request) {
        // Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // TODO: TP-68 티켓에 의한 임시 코드 -> 추후 위의 comment-out 된 것으로 다시 교체
        Member member = new Member(1L, "Temporary@temp.com", "Temporary User", "01011110000", "1900-01-01", GenderType.MALE, "Temporary", "https://Temporary.temp.tem/img/temp-1");

        Long postId = schedulePostService.createSchedulePost(member.getId(), request);
        return ResponseEntity.ok(SchedulePostCreateResponse.from(postId));
    }

    @ApiOperation("특정 여행 일정 공유 게시글 상세조회")
    @GetMapping("/schedules/{schedulePostId}")
    public ResponseEntity<SchedulePostDetailResponse> detailSchedulePost(@PathVariable("schedulePostId") Long schedulePostId) {
        SchedulePostDetailResponse schedulePostDetail = schedulePostService.getSchedulePostDetail(schedulePostId);
        return ResponseEntity.ok(schedulePostDetail);
    }

    @ApiOperation("(자신이 작성한)특정 여행 공유 게시글 삭제")
    @DeleteMapping("/schedules/{schedulePostId}")
    public ResponseEntity<Void> deleteSchedulePost(@PathVariable("schedulePostId") Long schedulePostId) {
        // Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // TODO: TP-68 티켓에 의한 임시 코드 -> 추후 위의 comment-out 된 것으로 다시 교체
        Member member = new Member(1L, "Temporary@temp.com", "Temporary User", "01011110000", "19000101", GenderType.MALE, "Temporary", "https://Temporary.temp.tem/img/temp-1");

        schedulePostService.deleteSchedulePost(member.getId(), schedulePostId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("여행 공유 게시글 수정")
    @PutMapping("/schedules/{schedulePostId}")
    public ResponseEntity<Void> modifySchedulePost(
            @PathVariable("schedulePostId") Long schedulePostId,
            @RequestBody SchedulePostRequest request
    ) {
        // TODO: 2021.12.10 Teru - 별도의 Util class 를 만들어 요청을 보내는 유저 정보 받아오는 메서드 작성하기
        // Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // TODO: TP-68 티켓에 의한 임시 코드 -> 추후 위의 comment-out 된 것으로 다시 교체
        Member member = new Member(1L, "Temporary@temp.com", "Temporary User", "01011110000", "19000101", GenderType.MALE, "Temporary", "https://Temporary.temp.tem/img/temp-1");

        schedulePostService.modifySchedulePost(member.getId(), schedulePostId, request);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("여행 공유 게시글 좋아요 토글")
    @PostMapping("/schedules/{schedulePostId}/liked")
    public ResponseEntity<SchedulePostLikeResponse> changeLikeFlag(
            @PathVariable("schedulePostId") Long schedulePostId,
            @RequestBody SchedulePostLikeRequest request
    ) {
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

    @ApiOperation("여행 공유 게시글에 작성된 댓글 조회하기")
    @GetMapping("/schedules/{schedulePostId}/comments")
    public ResponseEntity<List<SchedulePostCommentResponse>> getSchedulePostComments(@PathVariable("schedulePostId") Long schedulePostId) {
        List<SchedulePostCommentResponse> schedulePostComments = schedulePostService.getSchedulePostComments(schedulePostId);
        return ResponseEntity.ok(schedulePostComments);
    }

    @ApiOperation("여행 공유 게시글에 댓글 작성하기")
    @PostMapping("/schedules/{schedulePostId}/comments")
    public ResponseEntity<List<SchedulePostCommentResponse>> writeSchedulePostComment(
            @PathVariable("schedulePostId") Long schedulePostId,
            @RequestBody SchedulePostCommentRequest request
    ) {
        // Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // TODO: TP-68 티켓에 의한 임시 코드 -> 추후 위의 comment-out 된 것으로 다시 교체
        Member member = new Member(1L, "Temporary@temp.com", "Temporary User", "01011110000", "19000101", GenderType.MALE, "Temporary", "https://Temporary.temp.tem/img/temp-1");

        List<SchedulePostCommentResponse> schedulePostComments = schedulePostService.writeSchedulePostComment(member.getId(), schedulePostId, request);
        return ResponseEntity.ok(schedulePostComments);
    }

    @ApiOperation("여행 공유 게시글에 작성된 댓글 삭제하기")
    @DeleteMapping("/schedules/{schedulePostId}/comments/{commentId}")
    public ResponseEntity<Void> deleteSchedulePostComment(
            @PathVariable("schedulePostId") Long schedulePostId,
            @PathVariable("commentId") Long commentId
    ) {
        // Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // TODO: TP-68 티켓에 의한 임시 코드 -> 추후 위의 comment-out 된 것으로 다시 교체
        Member member = new Member(1L, "Temporary@temp.com", "Temporary User", "01011110000", "19000101", GenderType.MALE, "Temporary", "https://Temporary.temp.tem/img/temp-1");

        // TODO: 2021.12.15 Teru - 댓글에 대댓글이 작성되어 있는 상태에서 댓글이 삭제되면 어떻게 할 것인지 고민
        // 방법 1. 삭제된 댓글은 공란(삭제됨 표시)으로 두고, 아래 대댓글은 표시한다.
        // 방법 2. 삭제된 댓글에 있던 대댓글도 모두 삭제한다.
        schedulePostService.deleteSchedulePostComment(schedulePostId, commentId, member.getId());
        return ResponseEntity.ok().build();
    }

    @ApiOperation("여행 공유 게시글에 작성한 댓글 수정하기")
    @PutMapping("/schedules/{schedulePostId}/comments/{commentId}")
    public ResponseEntity<Void> modifySchedulePostComment(
            @PathVariable("schedulePostId") Long schedulePostId,
            @PathVariable("commentId") Long commentId,
            @RequestBody SchedulePostCommentRequest request
    ) {
        // Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // TODO: TP-68 티켓에 의한 임시 코드 -> 추후 위의 comment-out 된 것으로 다시 교체
        Member member = new Member(1L, "Temporary@temp.com", "Temporary User", "01011110000", "19000101", GenderType.MALE, "Temporary", "https://Temporary.temp.tem/img/temp-1");

        schedulePostService.modifySchedulePostComment(schedulePostId, commentId, member.getId(), request);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("여행 공유 게시글에 작성된 댓글의 대댓글 조회하기")
    @GetMapping("/schedules/{schedulePostId}/comments/{commentId}/nestedComments")
    public ResponseEntity<List<SchedulePostCommentResponse>> getSchedulePostNestedComments(
            @PathVariable("schedulePostId") Long schedulePostId,
            @PathVariable("commentId") Long commentId,
            @RequestBody SchedulePostCommentRequest request
    ) {
        // Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // TODO: TP-68 티켓에 의한 임시 코드 -> 추후 위의 comment-out 된 것으로 다시 교체
        Member member = new Member(1L, "Temporary@temp.com", "Temporary User", "01011110000", "19000101", GenderType.MALE, "Temporary", "https://Temporary.temp.tem/img/temp-1");

        List<SchedulePostCommentResponse> schedulePostCommentResponses = schedulePostService.writeNestedCommentToSchedulePostComment(
                member.getId(),
                schedulePostId,
                commentId,
                request
        );

        // 전체 댓글 및 대댓글 반환
        return ResponseEntity.ok(schedulePostCommentResponses);
    }

    @ApiOperation("대댓글 수정하기")
    @PutMapping("/schedules/{schedulePostId}/comments/{commentId}/nestedComments/{nestedCommentId}")
    public ResponseEntity<Void> modifySchedulePostNestedComment(
            @PathVariable("schedulePostId") Long schedulePostId,
            @PathVariable("commentId") Long commentId,
            @PathVariable("nestedCommentId") Long nestedCommentId,
            SchedulePostCommentRequest request
    ) {
        // Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // TODO: TP-68 티켓에 의한 임시 코드 -> 추후 위의 comment-out 된 것으로 다시 교체
        Member member = new Member(1L, "Temporary@temp.com", "Temporary User", "01011110000", "19000101", GenderType.MALE, "Temporary", "https://Temporary.temp.tem/img/temp-1");

        schedulePostService.modifySchedulePostNestedComment(
                member.getId(),
                schedulePostId,
                commentId,
                nestedCommentId,
                request
        );
        return ResponseEntity.ok().build();
    }

    @ApiOperation("대댓글 삭제하기")
    @DeleteMapping("/schedules/{schedulePostId}/comments/{commentId}/nestedComments/{nestedCommentId}")
    public ResponseEntity<Void> deleteSchedulePostNestedComment(
            @PathVariable("schedulePostId") Long schedulePostId,
            @PathVariable("commentId") Long commentId,
            @PathVariable("nestedCommentId") Long nestedCommentId
    ) {
        // Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // TODO: TP-68 티켓에 의한 임시 코드 -> 추후 위의 comment-out 된 것으로 다시 교체
        Member member = new Member(1L, "Temporary@temp.com", "Temporary User", "01011110000", "19000101", GenderType.MALE, "Temporary", "https://Temporary.temp.tem/img/temp-1");

        schedulePostService.deleteSchedulePostNestedComment(
                member.getId(),
                schedulePostId,
                commentId,
                nestedCommentId
        );
        return ResponseEntity.ok().build();
    }
}
