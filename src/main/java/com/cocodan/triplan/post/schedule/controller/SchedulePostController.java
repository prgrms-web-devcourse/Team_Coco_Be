package com.cocodan.triplan.post.schedule.controller;

import com.cocodan.triplan.jwt.JwtAuthentication;
import com.cocodan.triplan.post.schedule.dto.request.SchedulePostCommentRequest;
import com.cocodan.triplan.post.schedule.dto.request.SchedulePostLikeRequest;
import com.cocodan.triplan.post.schedule.dto.request.SchedulePostRequest;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostCommentResponse;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostCreateResponse;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostDetailResponse;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostLikeResponse;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostNestedCommentResponse;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostResponse;
import com.cocodan.triplan.post.schedule.service.SchedulePostSearchService;
import com.cocodan.triplan.post.schedule.service.SchedulePostService;
import com.cocodan.triplan.post.schedule.vo.SchedulePostSortingRule;
import com.cocodan.triplan.schedule.domain.vo.Theme;
import com.cocodan.triplan.spot.domain.vo.City;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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
    public ResponseEntity<List<SchedulePostResponse>> schedulePostList(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "전체") String searchingCity,
            @RequestParam(defaultValue = "ALL") String searchingTheme,
            @RequestParam(defaultValue = "최신순") String sorting
    ) {
        City city = City.from(searchingCity);
        Theme theme = Theme.from(searchingTheme);
        SchedulePostSortingRule sortRule = SchedulePostSortingRule.of(sorting);
        List<SchedulePostResponse> schedulePosts = schedulePostSearchService.getSchedulePosts(search, city, theme, sortRule);

        return ResponseEntity.ok(schedulePosts);
    }

    @ApiOperation("여행 일정 공유 게시글 작성")
    @PostMapping("/schedules")
    public ResponseEntity<SchedulePostCreateResponse> createSchedulePost(
            @Valid @RequestBody SchedulePostRequest request,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        Long postId = schedulePostService.createSchedulePost(authentication.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(SchedulePostCreateResponse.from(postId));
    }

    @ApiOperation("자신이 작성한 여행 공유 게시글 모아보기")
    @GetMapping("/schedules/me")
    public ResponseEntity<List<SchedulePostResponse>> getMySchedulePostList(@AuthenticationPrincipal JwtAuthentication authentication) {
        Long memberId = authentication.getId();
        List<SchedulePostResponse> schedulePosts = schedulePostService.getCertainMemberSchedulePostList(memberId);
        return ResponseEntity.ok(schedulePosts);
    }

    @ApiOperation("특정 멤버가 작성한 여행 공유 게시글 모아보기")
    @GetMapping("/schedules/writers/{writerId}")
    public ResponseEntity<List<SchedulePostResponse>> getCertainMembersSchedulePostList(@PathVariable("writerId") Long writerId) {
        List<SchedulePostResponse> schedulePosts = schedulePostService.getCertainMemberSchedulePostList(writerId);
        return ResponseEntity.ok(schedulePosts);
    }

    @ApiOperation("특정 여행 일정 공유 게시글 상세조회")
    @GetMapping("/schedules/{schedulePostId}")
    public ResponseEntity<SchedulePostDetailResponse> getDetailSchedulePost(
            @PathVariable("schedulePostId") Long schedulePostId,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        SchedulePostDetailResponse schedulePostDetail = schedulePostService.getSchedulePostDetail(schedulePostId, authentication.getId());
        return ResponseEntity.ok(schedulePostDetail);
    }

    @ApiOperation("(자신이 작성한)특정 여행 공유 게시글 삭제")
    @DeleteMapping("/schedules/{schedulePostId}")
    public ResponseEntity<Void> deleteSchedulePost(
            @PathVariable("schedulePostId") Long schedulePostId,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        schedulePostService.deleteSchedulePost(authentication.getId(), schedulePostId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("여행 공유 게시글 수정")
    @PutMapping("/schedules/{schedulePostId}")
    public ResponseEntity<Void> modifySchedulePost(
            @PathVariable("schedulePostId") Long schedulePostId,
            @Valid @RequestBody SchedulePostRequest request,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        schedulePostService.modifySchedulePost(authentication.getId(), schedulePostId, request);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("여행 공유 게시글 좋아요 토글")
    @PostMapping("/schedules/{schedulePostId}/liked")
    public ResponseEntity<SchedulePostLikeResponse> changeLikeFlag(
            @PathVariable("schedulePostId") Long schedulePostId,
            @Valid @RequestBody SchedulePostLikeRequest request,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        Long likeCount = schedulePostService.toggleSchedulePostLiked(authentication.getId(), request);
        return ResponseEntity.ok(new SchedulePostLikeResponse(likeCount));
    }

    @ApiOperation("좋아요 누른 게시글만 조회")
    @GetMapping("/schedules/liked")
    public ResponseEntity<List<SchedulePostResponse>> likedSchedulePostList(@AuthenticationPrincipal JwtAuthentication authentication) {
        return ResponseEntity.ok(schedulePostService.getLikedSchedulePosts(authentication.getId()));
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
            @Valid @RequestBody SchedulePostCommentRequest request,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        List<SchedulePostCommentResponse> schedulePostComments = schedulePostService.writeSchedulePostComment(authentication.getId(), schedulePostId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(schedulePostComments);
    }

    @ApiOperation("여행 공유 게시글에 작성된 댓글 삭제하기")
    @DeleteMapping("/schedules/{schedulePostId}/comments/{commentId}")
    public ResponseEntity<Void> deleteSchedulePostComment(
            @PathVariable("schedulePostId") Long schedulePostId,
            @PathVariable("commentId") Long commentId,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        // TODO: 2021.12.15 Teru - 댓글에 대댓글이 작성되어 있는 상태에서 댓글이 삭제되면 어떻게 할 것인지 고민
        // 방법 1. 삭제된 댓글은 공란(삭제됨 표시)으로 두고, 아래 대댓글은 표시한다.
        // 방법 2. 삭제된 댓글에 있던 대댓글도 모두 삭제한다.
        schedulePostService.deleteSchedulePostComment(schedulePostId, commentId, authentication.getId());
        return ResponseEntity.ok().build();
    }

    @ApiOperation("여행 공유 게시글에 작성한 댓글 수정하기")
    @PutMapping("/schedules/{schedulePostId}/comments/{commentId}")
    public ResponseEntity<Void> modifySchedulePostComment(
            @PathVariable("schedulePostId") Long schedulePostId,
            @PathVariable("commentId") Long commentId,
            @Valid @RequestBody SchedulePostCommentRequest request,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        schedulePostService.modifySchedulePostComment(schedulePostId, commentId, authentication.getId(), request);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("여행 공유 게시글에 작성된 댓글의 대댓글 조회하기")
    @GetMapping("/schedules/{schedulePostId}/comments/{commentId}/nestedComments")
    public ResponseEntity<List<SchedulePostNestedCommentResponse>> getSchedulePostNestedComments(
            @PathVariable("schedulePostId") Long schedulePostId,
            @PathVariable("commentId") Long commentId,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        List<SchedulePostNestedCommentResponse> schedulePostNestedComments = schedulePostService.getSchedulePostNestedComments(schedulePostId, commentId, authentication.getId());
        return ResponseEntity.ok(schedulePostNestedComments);
    }

    @ApiOperation("여행 공유 게시글에 작성된 댓글에 대댓글 작성하기 (전체 댓글 반환)")
    @PostMapping("/schedules/{schedulePostId}/comments/{commentId}/nestedComments")
    public ResponseEntity<List<SchedulePostCommentResponse>> writeSchedulePostNestedComment(
            @PathVariable("schedulePostId") Long schedulePostId,
            @PathVariable("commentId") Long commentId,
            @Valid @RequestBody SchedulePostCommentRequest request,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        List<SchedulePostCommentResponse> schedulePostCommentResponses = schedulePostService.writeNestedCommentToSchedulePostComment(
                authentication.getId(),
                schedulePostId,
                commentId,
                request
        );
        return ResponseEntity.ok(schedulePostCommentResponses);
    }

    @ApiOperation("대댓글 수정하기")
    @PutMapping("/schedules/{schedulePostId}/comments/{commentId}/nestedComments/{nestedCommentId}")
    public ResponseEntity<Void> modifySchedulePostNestedComment(
            @PathVariable("schedulePostId") Long schedulePostId,
            @PathVariable("commentId") Long commentId,
            @PathVariable("nestedCommentId") Long nestedCommentId,
            @Valid @RequestBody SchedulePostCommentRequest request,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        schedulePostService.modifySchedulePostNestedComment(
                authentication.getId(),
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
            @PathVariable("nestedCommentId") Long nestedCommentId,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        schedulePostService.deleteSchedulePostNestedComment(
                authentication.getId(),
                schedulePostId,
                commentId,
                nestedCommentId
        );
        return ResponseEntity.ok().build();
    }
}
