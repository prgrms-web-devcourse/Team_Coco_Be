package com.cocodan.triplan.post.schedule.controller;

import com.cocodan.triplan.common.ApiResponse;
import com.cocodan.triplan.jwt.JwtAuthentication;
import com.cocodan.triplan.post.schedule.dto.request.CommentCreationRequest;
import com.cocodan.triplan.post.schedule.dto.request.LikeToggleRequest;
import com.cocodan.triplan.post.schedule.dto.request.SchedulePostCreationRequest;
import com.cocodan.triplan.post.schedule.dto.response.CommentReadResponse;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostCreationResponse;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostDetailResponse;
import com.cocodan.triplan.post.schedule.dto.response.LikeToggleResponse;
import com.cocodan.triplan.post.schedule.dto.response.NestedCommentReadResponse;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostListViewResponse;
import com.cocodan.triplan.post.schedule.service.SchedulePostSearchService;
import com.cocodan.triplan.post.schedule.service.SchedulePostService;
import com.cocodan.triplan.post.schedule.vo.SchedulePostSortingRule;
import com.cocodan.triplan.schedule.domain.vo.Theme;
import com.cocodan.triplan.spot.domain.vo.City;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    public SchedulePostController(
            SchedulePostService schedulePostService,
            SchedulePostSearchService schedulePostSearchService
    ) {
        this.schedulePostService = schedulePostService;
        this.schedulePostSearchService = schedulePostSearchService;
    }

    @ApiOperation("여행 일정 공유 게시글 (조건별)목록 조회")
    @GetMapping("/schedules")
    public ApiResponse<List<SchedulePostListViewResponse>> schedulePostList(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "전체") String searchingCity,
            @RequestParam(defaultValue = "ALL") String searchingTheme,
            @RequestParam(defaultValue = "최신순") String sorting
    ) {
        City city = City.from(searchingCity);
        Theme theme = Theme.from(searchingTheme);
        SchedulePostSortingRule sortRule = SchedulePostSortingRule.of(sorting);
        List<SchedulePostListViewResponse> schedulePosts =
                schedulePostSearchService.getSchedulePosts(search, city, theme, sortRule);

        return ApiResponse.ok(schedulePosts);
    }

    @ApiOperation("여행 일정 공유 게시글 작성")
    @PostMapping("/schedules")
    public ApiResponse<SchedulePostCreationResponse> createSchedulePost(
            @Valid @RequestBody SchedulePostCreationRequest request,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        Long postId = schedulePostService.createSchedulePost(authentication.getId(), request);
        return ApiResponse.ok(SchedulePostCreationResponse.from(postId));
    }

    @ApiOperation("선택 가능한 도시 목록 보내주기")
    @GetMapping("/schedules/cities")
    public ApiResponse<List<String>> getAvailableCities() {
        List<String> cities = schedulePostService.getAvailableCities();
        return ApiResponse.ok(cities);
    }

    @ApiOperation("자신이 작성한 여행 공유 게시글 모아보기")
    @GetMapping("/schedules/me")
    public ApiResponse<List<SchedulePostListViewResponse>> getMySchedulePostList(
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        Long memberId = authentication.getId();
        List<SchedulePostListViewResponse> schedulePosts =
                schedulePostService.getCertainMemberSchedulePostList(memberId);
        return ApiResponse.ok(schedulePosts);
    }

    @ApiOperation("특정 멤버가 작성한 여행 공유 게시글 모아보기")
    @GetMapping("/schedules/writers/{writerId}")
    public ApiResponse<List<SchedulePostListViewResponse>> getCertainMembersSchedulePostList(
            @PathVariable("writerId") Long writerId
    ) {
        List<SchedulePostListViewResponse> schedulePosts =
                schedulePostService.getCertainMemberSchedulePostList(writerId);
        return ApiResponse.ok(schedulePosts);
    }

    @ApiOperation("특정 여행 일정 공유 게시글 상세조회")
    @GetMapping("/schedules/{schedulePostId}")
    public ApiResponse<SchedulePostDetailResponse> getDetailSchedulePost(
            @PathVariable("schedulePostId") Long schedulePostId,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        SchedulePostDetailResponse schedulePostDetail =
                schedulePostService.getSchedulePostDetail(schedulePostId, authentication.getId());
        return ApiResponse.ok(schedulePostDetail);
    }

    @ApiOperation("(자신이 작성한)특정 여행 공유 게시글 삭제")
    @DeleteMapping("/schedules/{schedulePostId}")
    public ApiResponse<Void> deleteSchedulePost(
            @PathVariable("schedulePostId") Long schedulePostId,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        schedulePostService.deleteSchedulePost(authentication.getId(), schedulePostId);
        return ApiResponse.ok();
    }

    @ApiOperation("여행 공유 게시글 수정")
    @PutMapping("/schedules/{schedulePostId}")
    public ApiResponse<Void> modifySchedulePost(
            @PathVariable("schedulePostId") Long schedulePostId,
            @Valid @RequestBody SchedulePostCreationRequest request,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        schedulePostService.modifySchedulePost(authentication.getId(), schedulePostId, request);
        return ApiResponse.ok();
    }

    @ApiOperation("여행 공유 게시글 좋아요 토글")
    @PostMapping("/schedules/{schedulePostId}/liked")
    public ApiResponse<LikeToggleResponse> changeLikeFlag(
            @PathVariable("schedulePostId") Long schedulePostId,
            @Valid @RequestBody LikeToggleRequest request,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        Long likeCount =
                schedulePostService.toggleSchedulePostLiked(authentication.getId(), schedulePostId, request);
        return ApiResponse.ok(new LikeToggleResponse(likeCount));
    }

    @ApiOperation("좋아요 누른 게시글만 조회")
    @GetMapping("/schedules/liked")
    public ApiResponse<List<SchedulePostListViewResponse>> likedSchedulePostList(
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        return ApiResponse.ok(schedulePostService.getLikedSchedulePosts(authentication.getId()));
    }

    @ApiOperation("여행 공유 게시글에 작성된 댓글 조회하기")
    @GetMapping("/schedules/{schedulePostId}/comments")
    public ApiResponse<List<CommentReadResponse>> getSchedulePostComments(
            @PathVariable("schedulePostId") Long schedulePostId
    ) {
        List<CommentReadResponse> schedulePostComments =
                schedulePostService.getSchedulePostComments(schedulePostId);
        return ApiResponse.ok(schedulePostComments);
    }

    @ApiOperation("여행 공유 게시글에 댓글 작성하기")
    @PostMapping("/schedules/{schedulePostId}/comments")
    public ApiResponse<List<CommentReadResponse>> writeSchedulePostComment(
            @PathVariable("schedulePostId") Long schedulePostId,
            @Valid @RequestBody CommentCreationRequest request,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        List<CommentReadResponse> schedulePostComments =
                schedulePostService.writeSchedulePostComment(authentication.getId(), schedulePostId, request);
        return ApiResponse.created(schedulePostComments);
    }

    @ApiOperation("여행 공유 게시글에 작성된 댓글 삭제하기")
    @DeleteMapping("/schedules/{schedulePostId}/comments/{commentId}")
    public ApiResponse<Void> deleteSchedulePostComment(
            @PathVariable("schedulePostId") Long schedulePostId,
            @PathVariable("commentId") Long commentId,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        // TODO: 2021.12.15 Teru - 댓글에 대댓글이 작성되어 있는 상태에서 댓글이 삭제되면 어떻게 할 것인지 고민
        // 방법 1. 삭제된 댓글은 공란(삭제됨 표시)으로 두고, 아래 대댓글은 표시한다.
        // 방법 2. 삭제된 댓글에 있던 대댓글도 모두 삭제한다.
        // -> Henry 등의 의견으로 1이 좋을 것이라 생각되나, 삭제 관련 기능에서 오류가 발생합니다. (코멘트가 삭제되기 위해서는 해당 comment의 ID를 FK로 가지는 모든 대댓글이 먼저 삭제되어야 함)
        // 그래서 일단 방법 2 쪽으로 먼저 구현합니다. (게시글 삭제 -> 대댓글, 댓글, 좋아요 순으로 선행 삭제 / 댓글 삭제 -> 대댓글, 댓글 순으로 삭제)
        // -> TODO: 추후 연관관계 관련 문제 해결하고 방법 1로 교체
        schedulePostService.deleteSchedulePostComment(schedulePostId, commentId, authentication.getId());
        return ApiResponse.ok();
    }

    @ApiOperation("여행 공유 게시글에 작성한 댓글 수정하기")
    @PutMapping("/schedules/{schedulePostId}/comments/{commentId}")
    public ApiResponse<Void> modifySchedulePostComment(
            @PathVariable("schedulePostId") Long schedulePostId,
            @PathVariable("commentId") Long commentId,
            @Valid @RequestBody CommentCreationRequest request,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        schedulePostService.modifySchedulePostComment(schedulePostId, commentId, authentication.getId(), request);
        return ApiResponse.ok();
    }

    @ApiOperation("여행 공유 게시글에 작성된 댓글의 대댓글 조회하기")
    @GetMapping("/schedules/{schedulePostId}/comments/{commentId}/nestedComments")
    public ApiResponse<List<NestedCommentReadResponse>> getSchedulePostNestedComments(
            @PathVariable("schedulePostId") Long schedulePostId,
            @PathVariable("commentId") Long commentId,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        List<NestedCommentReadResponse> schedulePostNestedComments =
                schedulePostService.getSchedulePostNestedComments(schedulePostId, commentId, authentication.getId());
        return ApiResponse.ok(schedulePostNestedComments);
    }

    @ApiOperation("여행 공유 게시글에 작성된 댓글에 대댓글 작성하기 (전체 댓글 반환)")
    @PostMapping("/schedules/{schedulePostId}/comments/{commentId}/nestedComments")
    public ApiResponse<List<CommentReadResponse>> writeSchedulePostNestedComment(
            @PathVariable("schedulePostId") Long schedulePostId,
            @PathVariable("commentId") Long commentId,
            @Valid @RequestBody CommentCreationRequest request,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        List<CommentReadResponse> commentReadResponses =
                schedulePostService.writeNestedCommentToSchedulePostComment(
                        authentication.getId(),
                        schedulePostId,
                        commentId,
                        request
                );
        return ApiResponse.ok(commentReadResponses);
    }

    @ApiOperation("대댓글 수정하기")
    @PutMapping("/schedules/{schedulePostId}/comments/{commentId}/nestedComments/{nestedCommentId}")
    public ApiResponse<Void> modifySchedulePostNestedComment(
            @PathVariable("schedulePostId") Long schedulePostId,
            @PathVariable("commentId") Long commentId,
            @PathVariable("nestedCommentId") Long nestedCommentId,
            @Valid @RequestBody CommentCreationRequest request,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) {
        schedulePostService.modifySchedulePostNestedComment(
                authentication.getId(),
                schedulePostId,
                commentId,
                nestedCommentId,
                request
        );
        return ApiResponse.ok();
    }

    @ApiOperation("대댓글 삭제하기")
    @DeleteMapping("/schedules/{schedulePostId}/comments/{commentId}/nestedComments/{nestedCommentId}")
    public ApiResponse<Void> deleteSchedulePostNestedComment(
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
        return ApiResponse.ok();
    }
}
