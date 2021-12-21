package com.cocodan.triplan.post.schedule.service;

import com.cocodan.triplan.exception.common.NotFoundException;
import com.cocodan.triplan.member.domain.vo.GenderType;
import com.cocodan.triplan.member.service.MemberService;
import com.cocodan.triplan.post.schedule.domain.SchedulePost;
import com.cocodan.triplan.post.schedule.dto.request.SchedulePostCommentRequest;
import com.cocodan.triplan.post.schedule.dto.request.SchedulePostLikeRequest;
import com.cocodan.triplan.post.schedule.dto.request.SchedulePostRequest;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostCommentResponse;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostDetailResponse;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostNestedCommentResponse;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostResponse;
import com.cocodan.triplan.post.schedule.vo.Ages;
import com.cocodan.triplan.post.schedule.vo.SchedulePostSortingRule;
import com.cocodan.triplan.schedule.domain.vo.Theme;
import com.cocodan.triplan.schedule.dto.request.DailyScheduleSpotCreationRequest;
import com.cocodan.triplan.schedule.dto.request.Position;
import com.cocodan.triplan.schedule.dto.request.ScheduleCreationRequest;
import com.cocodan.triplan.schedule.dto.response.DailyScheduleSpotResponse;
import com.cocodan.triplan.schedule.dto.response.ScheduleDetailResponse;
import com.cocodan.triplan.schedule.dto.response.ScheduleSpotResponse;
import com.cocodan.triplan.schedule.service.ScheduleService;
import com.cocodan.triplan.spot.domain.vo.City;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SchedulePostServiceTest {

    @Autowired
    SchedulePostService schedulePostService;

    @Autowired
    SchedulePostSearchService postSearchService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    MemberService memberService;

    private static Long testMemberId;

    private static final String EMAIL = "KimLeePark@gmail.com";
    private static final String NAME = "김이박";
    private static final String BIRTH = "1990-11-11";
    private static final String GENDER = GenderType.MALE.getTypeStr();
    private static final String NICKNAME = "TestNickname";
    private static final String PROFILE_IMAGE = "https://wwww.someonesownserver.org/img/1";
    private static final String PASSWORD = "asdf123";
    private static final Long GROUP_ID = 1L;

    private static final String title1 = "1번 여행 게시글";

    private static final String content1 = "맥북 프로 사세요! 두 번 사세요!";

    private static final String title2 = "2번 여행 게시글";

    private static final String content2 = "블랙 프라이데이에 서버용 컴퓨터를 맞추세요!";

    @BeforeAll
    void setup() {
        testMemberId = memberService.create(
                EMAIL,
                NAME,
                BIRTH,
                GENDER,
                NICKNAME,
                PROFILE_IMAGE,
                PASSWORD,
                GROUP_ID
        ).getId();
    }

    private ScheduleCreationRequest createScheduleCreation() {
        return new ScheduleCreationRequest(
                "title",
                LocalDate.of(2021, 12, 1),
                LocalDate.of(2021, 12, 3),
                List.of("activity", "food"),
                List.of(
                        new DailyScheduleSpotCreationRequest(1L, "address1", "roadAddress1", "010-1111-2222", "불국사1", new Position(37.1234, 125.3333), 1, 1),
                        new DailyScheduleSpotCreationRequest(2L, "address2", "roadAddress2", "010-1111-2223", "불국사2", new Position(37.1234, 125.3333), 1, 2),
                        new DailyScheduleSpotCreationRequest(3L, "address3", "roadAddress3", "010-1111-2224", "불국사3", new Position(37.1234, 125.3333), 1, 3),
                        new DailyScheduleSpotCreationRequest(4L, "address4", "roadAddress4", "010-1111-2225", "불국사4", new Position(37.1234, 125.3333), 2, 1),
                        new DailyScheduleSpotCreationRequest(5L, "address5", "roadAddress5", "010-1111-2226", "불국사5", new Position(37.1234, 125.3333), 2, 2),
                        new DailyScheduleSpotCreationRequest(6L, "address6", "roadAddress6", "010-1111-2227", "불국사6", new Position(37.1234, 125.3333), 2, 3),
                        new DailyScheduleSpotCreationRequest(7L, "address7", "roadAddress7", "010-1111-2228", "불국사7", new Position(37.1234, 125.3333), 3, 1),
                        new DailyScheduleSpotCreationRequest(8L, "address8", "roadAddress8", "010-1111-2229", "불국사8", new Position(37.1234, 125.3333), 3, 2)
                ),
                List.of());
    }

    private Long createSchedulePost1() {
        ScheduleCreationRequest scheduleCreationRequest = createScheduleCreation();
        Long createdScheduleId = scheduleService.saveSchedule(scheduleCreationRequest, testMemberId);
        SchedulePostRequest request = new SchedulePostRequest(title1, content1, "서울", createdScheduleId);
        return schedulePostService.createSchedulePost(testMemberId, request);
    }

    private Long createSchedulePost2() {
        ScheduleCreationRequest scheduleCreationRequest = createScheduleCreation();
        Long createdScheduleId = scheduleService.saveSchedule(scheduleCreationRequest, testMemberId);
        SchedulePostRequest request = new SchedulePostRequest(title2, content2, "부산", createdScheduleId);
        return schedulePostService.createSchedulePost(testMemberId, request);
    }

    @Test
    @DisplayName("생성된 여행 공유 게시글 리스트를 정상적으로 조회 할 수 있다.")
    @Transactional
    void getRecentSchedulePostList() {
        // 여행 생성
        ScheduleCreationRequest scheduleCreationRequest = createScheduleCreation();
        Long createdScheduleId = scheduleService.saveSchedule(scheduleCreationRequest, testMemberId);

        // 여행 공유 게시글 생성
        Long schedulePost1Id = createSchedulePost1();

        // 여행 공유 게시글 조회
        SchedulePost schedulePost = schedulePostService.findById(schedulePost1Id);
        List<SchedulePostResponse> posts = postSearchService.getSchedulePosts("", City.ALL, Theme.ALL, SchedulePostSortingRule.RECENT);
        assertThat(posts.size()).isEqualTo(1);
        assertThat(posts.get(0).getNickname()).isEqualTo(NICKNAME);
        assertThat(posts.get(0).getTitle()).isEqualTo(schedulePost.getTitle());
        assertThat(posts.get(0).getGenderType().getTypeStr()).isEqualTo(GENDER);
        assertThat(posts.get(0).getCity()).isEqualTo(City.SEOUL.toString());
        assertThat(posts.get(0).getStartDate()).isEqualTo("2021-12-01");
        assertThat(posts.get(0).getEndDate()).isEqualTo("2021-12-03");
        assertThat(posts.get(0).getThemes()).contains(Theme.ACTIVITY, Theme.FOOD);

        // TODO: 다양한 조건으로 테스트 추가
    }

    @Test
    @DisplayName("여행 공유 게시글 생성 로직이 정상적으로 동작한다.")
    @Transactional
    void createSchedulePost() {
        // 게시글 만들기
        Long schedulePost1Id = createSchedulePost1();
        SchedulePost schedulePost = schedulePostService.findById(schedulePost1Id);
        // 검증
        assertThat(schedulePost.getMember().getId()).isEqualTo(testMemberId);
        assertThat(memberService.getOne(schedulePost.getMember().getId()).getEmail()).isEqualTo(EMAIL);
        assertThat(memberService.getOne(schedulePost.getMember().getId()).getName()).isEqualTo(NAME);
        assertThat(memberService.getOne(schedulePost.getMember().getId()).getBirth()).isEqualTo(BIRTH);
        assertThat(memberService.getOne(schedulePost.getMember().getId()).getNickname()).isEqualTo(NICKNAME);
        assertThat(memberService.getOne(schedulePost.getMember().getId()).getProfileImage()).isEqualTo(PROFILE_IMAGE);
        assertThat(schedulePost.getTitle()).isEqualTo(title1);
        assertThat(schedulePost.getContent()).isEqualTo(content1);
        assertThat(schedulePost.getCity()).isEqualTo(City.SEOUL);
    }

    @Test
    @DisplayName("여행 공유 게시글을 상세 조회 할 수 있다.")
    @Transactional
    void getSchedulePostDetail() {
        // 게시글 만들기
        Long createdSchedulePostId = createSchedulePost1();
        SchedulePost schedulePost = schedulePostService.findById(createdSchedulePostId);
        long initialViews = schedulePost.getViews();
        // 게시글 상세조회 정보
        SchedulePostDetailResponse schedulePostDetail = schedulePostService.getSchedulePostDetail(createdSchedulePostId, testMemberId);
        // 검증
        ScheduleDetailResponse schedule = scheduleService.getSchedule(schedulePostDetail.getScheduleId());
        assertThat(schedulePostDetail.getTitle()).isEqualTo(title1);
        assertThat(schedulePostDetail.getContent()).isEqualTo(content1);
        assertThat(schedulePostDetail.getCity()).isEqualTo(City.SEOUL.toString());
        assertThat(schedulePostDetail.getCreatedAt()).isEqualTo(schedulePost.getCreatedDate().toString());
        assertThat(schedulePostDetail.getViews()).isEqualTo(schedulePost.getViews());
        assertThat(initialViews + 1).isEqualTo(schedulePost.getViews());
        assertThat(schedulePostDetail.getLikeCount()).isEqualTo(schedulePost.getLiked());
        assertThat(schedulePostDetail.getIsLiked()).isEqualTo(false);
        assertThat(schedulePostDetail.getStartDate()).isEqualTo(schedule.getScheduleSimpleResponse().getStartDate());
        assertThat(schedulePostDetail.getEndDate()).isEqualTo(schedule.getScheduleSimpleResponse().getEndDate());
        assertThat(schedulePostDetail.getGender()).isEqualTo(schedulePost.getMember().getGender());
        assertThat(schedulePostDetail.getNickname()).isEqualTo(schedulePost.getMember().getNickname());
        assertThat(schedulePostDetail.getAges()).isEqualTo(Ages.from(schedulePost.getMember().getBirth()));
        assertThat(schedulePostDetail.getScheduleId()).isEqualTo(schedulePost.getScheduleId());

        // TODO: 2021.12.10 Teru - equals method를 오버라이드 하지 않고, 그냥 DTO의 toString() 을 통해서 String 비교로 검증하도록 수정.
        List<Long> spotIdsFromDailyScheduleSpots = schedulePostDetail.getDailyScheduleSpots().stream()
                .map(DailyScheduleSpotResponse::getSpotId)
                .sorted()
                .collect(Collectors.toList());
        List<Long> spotIdsFromScheduleSpotResponses = schedule.getSpotResponseList().stream()
                .map(ScheduleSpotResponse::getSpotId)
                .sorted()
                .collect(Collectors.toList());
        int size = spotIdsFromDailyScheduleSpots.size();
        for (int i = 0; i < size; i++) {
            assertThat(spotIdsFromDailyScheduleSpots.get(i).equals(spotIdsFromScheduleSpotResponses.get(i)));
        }
    }

    @Test
    @DisplayName("생성된 공유 게시글을 삭제할 수 있다")
    @Transactional
    void deleteSchedulePost() {
        Long createdSchedulePostId = createSchedulePost1();
        SchedulePost post = schedulePostService.findById(createdSchedulePostId);

        // 게시글 생성 확인
        assertThat(post.getId()).isEqualTo(createdSchedulePostId);

        // 게시글 삭제하기
        schedulePostService.deleteSchedulePost(testMemberId, createdSchedulePostId);

        // 게시글이 삭제되었는지 검증하기
        Assertions.assertThrows(NotFoundException.class,
                () -> schedulePostService.getSchedulePostDetail(createdSchedulePostId, testMemberId)
        );
    }

    @Test
    @DisplayName("작성한 공유 게시글을 수정할 수 있다")
    @Transactional
    void modifySchedulePost() {
        // 여행 공유 게시글 생성
        Long createdSchedulePostId = createSchedulePost1();
        SchedulePost post = schedulePostService.findById(createdSchedulePostId);

        // 새로운 여행 공유 게시글 생성 (수정용)
        ScheduleCreationRequest scheduleCreationRequest = createScheduleCreation();
        Long newScheduleId = scheduleService.saveSchedule(scheduleCreationRequest, testMemberId);

        // 게시글 수정
        final String newTitle = "가자 우주로!";
        final String newContent = "저에게 시간과 예산이 조금만 더 있었더라면...";
        SchedulePostRequest modifyRequest = new SchedulePostRequest(newTitle, newContent, "부산", newScheduleId);
        schedulePostService.modifySchedulePost(testMemberId, post.getId(), modifyRequest);

        // 검증
        SchedulePost modifiedPost = schedulePostService.findById(createdSchedulePostId);
        assertThat(modifiedPost.getTitle()).isEqualTo(newTitle);
        assertThat(modifiedPost.getContent()).isEqualTo(newContent);
        assertThat(modifiedPost.getCity()).isEqualTo(City.BUSAN);
        assertThat(modifiedPost.getScheduleId()).isEqualTo(newScheduleId);
    }

    @Test
    @DisplayName("작성된 공유 게시글에 좋아요 토글을 할 수 있다")
    @Transactional
    void toggleSchedulePostLiked() {
        Long createdSchedulePostId = createSchedulePost1();
        SchedulePost post = schedulePostService.findById(createdSchedulePostId);
        // 좋아요 누르기 전 좋아요 수
        long beforeLiked = post.getLiked();

        // 좋아요 누르기
        SchedulePostLikeRequest doSchedulePostLike = new SchedulePostLikeRequest(true);
        Long afterLiked = schedulePostService.toggleSchedulePostLiked(testMemberId, createdSchedulePostId, doSchedulePostLike);
        // 좋아요 취소
        SchedulePostLikeRequest doSchedulePostLikeAgain = new SchedulePostLikeRequest(false);
        Long afterLikedAgain = schedulePostService.toggleSchedulePostLiked(testMemberId, createdSchedulePostId, doSchedulePostLikeAgain);
        
        // 좋아요 누른 후 좋아요 수
        assertThat(beforeLiked + 1).isEqualTo(afterLiked);
        // 좋아요 취소된 후 좋아요 수
        assertThat(beforeLiked).isEqualTo(afterLikedAgain);
    }

    @Test
    @DisplayName("좋아요를 누른 게시글만 모아서 볼 수 있다")
    @Transactional
    void getLikedSchedulePostsOnly() {
        Long createdSchedulePostId1 = createSchedulePost1();
        Long createdSchedulePostId2 = createSchedulePost2();
        // 좋아요 한 게시글 없음
        List<SchedulePostResponse> emptySchedulePostList = schedulePostService.getLikedSchedulePosts(testMemberId);
        // 1번 여행 좋아요!
        SchedulePostLikeRequest doSchedulePostLike1 = new SchedulePostLikeRequest(true);
        schedulePostService.toggleSchedulePostLiked(testMemberId, createdSchedulePostId1, doSchedulePostLike1);
        List<SchedulePostResponse> schedulePostListAfterLikeTrip1 = schedulePostService.getLikedSchedulePosts(testMemberId);
        // 2번 여행도 좋아요!
        SchedulePostLikeRequest doSchedulePostLike2 = new SchedulePostLikeRequest(true);
        schedulePostService.toggleSchedulePostLiked(testMemberId, createdSchedulePostId2, doSchedulePostLike2);
        List<SchedulePostResponse> schedulePostListAfterLikeTrip2 = schedulePostService.getLikedSchedulePosts(testMemberId);

        // then
        assertThat(emptySchedulePostList).isEmpty();
        assertThat(schedulePostListAfterLikeTrip1.size()).isEqualTo(1);
        List<SchedulePost> post1 = new ArrayList<>();
        post1.add(schedulePostService.findById(createdSchedulePostId1));
        SchedulePostResponse response1 = schedulePostService.convertToSchedulePostResponseList(post1).get(0);

        assertThat(schedulePostListAfterLikeTrip1.get(0).getProfileImageUrl()).isEqualTo(response1.getProfileImageUrl());
        assertThat(schedulePostListAfterLikeTrip1.get(0).getNickname()).isEqualTo(response1.getNickname());
        assertThat(schedulePostListAfterLikeTrip1.get(0).getTitle()).isEqualTo(response1.getTitle());
        assertThat(schedulePostListAfterLikeTrip1.get(0).getAges()).isEqualTo(response1.getAges());
        assertThat(schedulePostListAfterLikeTrip1.get(0).getGenderType()).isEqualTo(response1.getGenderType());
        assertThat(schedulePostListAfterLikeTrip1.get(0).getCity()).isEqualTo(response1.getCity());
        assertThat(schedulePostListAfterLikeTrip1.get(0).getThemes()).isEqualTo(response1.getThemes());
        assertThat(schedulePostListAfterLikeTrip1.get(0).getStartDate()).isEqualTo(response1.getStartDate());
        assertThat(schedulePostListAfterLikeTrip1.get(0).getEndDate()).isEqualTo(response1.getEndDate());

        assertThat(schedulePostListAfterLikeTrip2.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("여행 공유 게시글에 댓글 작성 및 조회할 수 있다.")
    @Transactional
    void writeCommentToSchedulePost() {
        Long createdSchedulePostId1 = createSchedulePost1();
        SchedulePostCommentRequest comment1 = new SchedulePostCommentRequest("타트타팟틋타팟틋타훗툿타들숨틋틋타흡틋트타치크틋틋타타타타찻차흙흙파치크풋풋파흡파");
        List<SchedulePostCommentResponse> comments = schedulePostService.writeSchedulePostComment(testMemberId, createdSchedulePostId1, comment1);

        assertThat(comments.size()).isEqualTo(1);
        assertThat(comments.get(0).getContent()).isEqualTo("타트타팟틋타팟틋타훗툿타들숨틋틋타흡틋트타치크틋틋타타타타찻차흙흙파치크풋풋파흡파");
        assertThat(comments.get(0).getNickname()).isEqualTo(NICKNAME);
    }

    @Test
    @DisplayName("작성한 댓글을 삭제할 수 있다")
    @Transactional
    void deleteCommentFromSchedulePost() {
        // 댓글 작성
        Long createdSchedulePostId1 = createSchedulePost1();
        SchedulePostCommentRequest comment1 = new SchedulePostCommentRequest("타트타팟틋타팟틋타훗툿타들숨틋틋타흡틋트타치크틋틋타타타타찻차흙흙파치크풋풋파흡파");
        List<SchedulePostCommentResponse> comments = schedulePostService.writeSchedulePostComment(testMemberId, createdSchedulePostId1, comment1);
        // 댓글 작성 확인
        assertThat(comments.size()).isEqualTo(1);
        assertThat(comments.get(0).getContent()).isEqualTo("타트타팟틋타팟틋타훗툿타들숨틋틋타흡틋트타치크틋틋타타타타찻차흙흙파치크풋풋파흡파");
        assertThat(comments.get(0).getNickname()).isEqualTo(NICKNAME);

        // 댓글 삭제
        schedulePostService.deleteSchedulePostComment(createdSchedulePostId1, comments.get(0).getCommentId(), testMemberId);
        // 댓글 삭제 확인
        List<SchedulePostCommentResponse> commentsAfterDeletion = schedulePostService.getSchedulePostComments(createdSchedulePostId1);
        assertThat(commentsAfterDeletion.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("작성한 댓글을 수정할 수 있다")
    @Transactional
    void modifyCommentOfASchedulePost() {
        // 댓글 작성
        Long createdSchedulePostId1 = createSchedulePost1();
        SchedulePostCommentRequest comment1 = new SchedulePostCommentRequest("타트타팟틋타팟틋타훗툿타들숨틋틋타흡틋트타치크틋틋타타타타찻차흙흙파치크풋풋파흡파");
        List<SchedulePostCommentResponse> comments = schedulePostService.writeSchedulePostComment(testMemberId, createdSchedulePostId1, comment1);
        // 댓글 작성 확인
        assertThat(comments.size()).isEqualTo(1);
        assertThat(comments.get(0).getContent()).isEqualTo("타트타팟틋타팟틋타훗툿타들숨틋틋타흡틋트타치크틋틋타타타타찻차흙흙파치크풋풋파흡파");
        assertThat(comments.get(0).getNickname()).isEqualTo(NICKNAME);

        // 댓글 수정
        SchedulePostCommentRequest modifyRequest = new SchedulePostCommentRequest("無無明 亦無無明盡 乃至 無老死 亦無老死盡");
        schedulePostService.modifySchedulePostComment(createdSchedulePostId1, comments.get(0).getCommentId(), testMemberId, modifyRequest);
        // 댓글 수정 확인
        List<SchedulePostCommentResponse> schedulePostComments = schedulePostService.getSchedulePostComments(createdSchedulePostId1);
        assertThat(schedulePostComments.size()).isEqualTo(1);
        assertThat(schedulePostComments.get(0).getContent()).isEqualTo("無無明 亦無無明盡 乃至 無老死 亦無老死盡");
    }

    @Test
    @DisplayName("댓글에 대댓글을 달 수 있다")
    @Transactional
    void writeNestedCommentsToCommentOfSchedulePost() {
        // 댓글 작성
        Long createdSchedulePostId1 = createSchedulePost1();
        SchedulePostCommentRequest commentRequest = new SchedulePostCommentRequest("타트타팟틋타팟틋타훗툿타들숨틋틋타흡틋트타치크틋틋타타타타찻차흙흙파치크풋풋파흡파");
        List<SchedulePostCommentResponse> comments = schedulePostService.writeSchedulePostComment(testMemberId, createdSchedulePostId1, commentRequest);

        // 대댓글 작성
        SchedulePostCommentRequest nestedCommentRequest = new SchedulePostCommentRequest("無無明 亦無無明盡 乃至 無老死 亦無老死盡");
        schedulePostService.writeNestedCommentToSchedulePostComment(
                testMemberId,
                createdSchedulePostId1,
                comments.get(0).getCommentId(),
                nestedCommentRequest
        );

        // 대댓글 확인
        SchedulePostDetailResponse schedulePostDetail = schedulePostService.getSchedulePostDetail(createdSchedulePostId1, testMemberId);
        assertThat(schedulePostDetail.getComments().size()).isEqualTo(1);
        assertThat(schedulePostDetail.getComments().get(0).getContent()).isEqualTo("타트타팟틋타팟틋타훗툿타들숨틋틋타흡틋트타치크틋틋타타타타찻차흙흙파치크풋풋파흡파");
        assertThat(schedulePostDetail.getComments().get(0).getNestedComments().size()).isEqualTo(1);
        assertThat(schedulePostDetail.getComments().get(0).getNestedComments().get(0).getContent()).isEqualTo("無無明 亦無無明盡 乃至 無老死 亦無老死盡");
    }

    @Test
    @DisplayName("댓글에 달린 대댓글을 조회할 수 있다")
    @Transactional
    void getNestedCommentsOfACommentOfASchedulePost() {
        // 댓글 작성
        Long createdSchedulePostId1 = createSchedulePost1();
        SchedulePostCommentRequest commentRequest = new SchedulePostCommentRequest("타트타팟틋타팟틋타훗툿타들숨틋틋타흡틋트타치크틋틋타타타타찻차흙흙파치크풋풋파흡파");
        List<SchedulePostCommentResponse> comments = schedulePostService.writeSchedulePostComment(testMemberId, createdSchedulePostId1, commentRequest);

        // 대댓글 작성
        SchedulePostCommentRequest nestedCommentRequest = new SchedulePostCommentRequest("無無明 亦無無明盡 乃至 無老死 亦無老死盡");
        schedulePostService.writeNestedCommentToSchedulePostComment(
                testMemberId,
                createdSchedulePostId1,
                comments.get(0).getCommentId(),
                nestedCommentRequest
        );
        // 1차 확인
        List<SchedulePostNestedCommentResponse> nestedComments = schedulePostService.getSchedulePostNestedComments(createdSchedulePostId1, comments.get(0).getCommentId(), testMemberId);
        assertThat(nestedComments.get(0).getContent()).isEqualTo("無無明 亦無無明盡 乃至 無老死 亦無老死盡");

        SchedulePostCommentRequest nestedCommentRequest2 = new SchedulePostCommentRequest("아이클리어 루테인지아잔틴");
        schedulePostService.writeNestedCommentToSchedulePostComment(
                testMemberId,
                createdSchedulePostId1,
                comments.get(0).getCommentId(),
                nestedCommentRequest2
        );

        // 2차 확인
        nestedComments = schedulePostService.getSchedulePostNestedComments(createdSchedulePostId1, comments.get(0).getCommentId(), testMemberId);
        assertThat(nestedComments.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("대댓글을 수정할 수 있다")
    @Transactional
    void modifySchedulePostNestedComment() {
        // 댓글 작성
        Long createdSchedulePostId1 = createSchedulePost1();
        SchedulePostCommentRequest commentRequest = new SchedulePostCommentRequest("타트타팟틋타팟틋타훗툿타들숨틋틋타흡틋트타치크틋틋타타타타찻차흙흙파치크풋풋파흡파");
        List<SchedulePostCommentResponse> comments = schedulePostService.writeSchedulePostComment(testMemberId, createdSchedulePostId1, commentRequest);

        // 대댓글 작성
        SchedulePostCommentRequest nestedCommentRequest = new SchedulePostCommentRequest("無無明 亦無無明盡 乃至 無老死 亦無老死盡");
        comments = schedulePostService.writeNestedCommentToSchedulePostComment(
                testMemberId,
                createdSchedulePostId1,
                comments.get(0).getCommentId(),
                nestedCommentRequest
        );

        // 대댓글 수정
        SchedulePostCommentRequest nestedCommentModifyRequest = new SchedulePostCommentRequest("나랏말싸미 듕귁에달아 문자와로 서로 사맛디 아니할새 이런 젼챠로 어린 백셩이 니르고져 할 배 이셔도 못 할 노미 하니라");
        schedulePostService.modifySchedulePostNestedComment(
                testMemberId,
                createdSchedulePostId1,
                comments.get(0).getCommentId(),
                comments.get(0).getNestedComments().get(0).getNestedCommentId(),
                nestedCommentModifyRequest
        );

        // 대댓글 확인
        SchedulePostDetailResponse schedulePostDetail = schedulePostService.getSchedulePostDetail(createdSchedulePostId1, testMemberId);
        assertThat(schedulePostDetail.getComments().get(0).getNestedComments().get(0).getContent()).isEqualTo("나랏말싸미 듕귁에달아 문자와로 서로 사맛디 아니할새 이런 젼챠로 어린 백셩이 니르고져 할 배 이셔도 못 할 노미 하니라");
    }

    @Test
    @DisplayName("대댓글을 삭제할 수 있다")
    @Transactional
    void deleteSchedulePostNestedComment() {
        // 댓글 작성
        Long createdSchedulePostId1 = createSchedulePost1();
        SchedulePostCommentRequest commentRequest = new SchedulePostCommentRequest("타트타팟틋타팟틋타훗툿타들숨틋틋타흡틋트타치크틋틋타타타타찻차흙흙파치크풋풋파흡파");
        List<SchedulePostCommentResponse> comments = schedulePostService.writeSchedulePostComment(testMemberId, createdSchedulePostId1, commentRequest);

        // 대댓글 작성
        SchedulePostCommentRequest nestedCommentRequest = new SchedulePostCommentRequest("無無明 亦無無明盡 乃至 無老死 亦無老死盡");
        comments = schedulePostService.writeNestedCommentToSchedulePostComment(
                testMemberId,
                createdSchedulePostId1,
                comments.get(0).getCommentId(),
                nestedCommentRequest
        );

        // 작성된 대댓글 확인
        assertThat(comments.get(0).getNestedComments().size()).isEqualTo(1);

        // 대댓글 삭제
        schedulePostService.deleteSchedulePostNestedComment(
                testMemberId,
                createdSchedulePostId1,
                comments.get(0).getCommentId(),
                comments.get(0).getNestedComments().get(0).getNestedCommentId()
        );

        // 대댓글 삭제 확인
        comments = schedulePostService.getSchedulePostComments(createdSchedulePostId1);
        assertThat(comments.get(0).getNestedComments().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("특정 사용자가 작성한 여행 공유 게시글을 모아 볼 수 있다")
    @Transactional
    void getSchedulePostListOfACertainMember() {
        ScheduleCreationRequest scheduleCreationRequest = createScheduleCreation();
        Long createdScheduleId = scheduleService.saveSchedule(scheduleCreationRequest, testMemberId);

        // 게시글 생성1
        SchedulePostRequest request1 = new SchedulePostRequest("1번 여행 넘모 신나요~", "신나씐나신나씐나", "서울", createdScheduleId);
        Long createdSchedulePostId1 = schedulePostService.createSchedulePost(testMemberId, request1);
        SchedulePost post1 = schedulePostService.findById(createdSchedulePostId1);

        // 생성된 게시글 디테일 확인
        assertThat(schedulePostService.getCertainMemberSchedulePostList(testMemberId).size()).isEqualTo(1);
        assertThat(schedulePostService.getCertainMemberSchedulePostList(testMemberId).get(0).getTitle()).isEqualTo("1번 여행 넘모 신나요~");
        assertThat(schedulePostService.getCertainMemberSchedulePostList(testMemberId).get(0).getPostId()).isEqualTo(createdSchedulePostId1);
        assertThat(schedulePostService.getCertainMemberSchedulePostList(testMemberId).get(0).getCity()).isEqualTo(City.SEOUL.toString());
        assertThat(schedulePostService.getCertainMemberSchedulePostList(testMemberId).get(0).getWriterId()).isEqualTo(testMemberId);

        // 게시글 생성 2
        SchedulePostRequest request2 = new SchedulePostRequest("1번 여행 다녀왔어요~", "갔다왔어요~", "서울", createdScheduleId);
        Long createdSchedulePostId2 = schedulePostService.createSchedulePost(testMemberId, request2);
        SchedulePost post2 = schedulePostService.findById(createdSchedulePostId2);

        // 생성된 게시글 갯수 확인
        assertThat(schedulePostService.getCertainMemberSchedulePostList(testMemberId).size()).isEqualTo(2);
        // 존재하지 않는 유저 게시글 조회 요청
        assertThat(schedulePostService.getCertainMemberSchedulePostList(-1L).size()).isEqualTo(0);
    }

    @Test
    @DisplayName("선택 가능한 도시 목록을 정상적으로 받아올 수 있다")
    void getAvailableCities() {
        List<String> cities = schedulePostService.getAvailableCities();

        System.out.println(cities);
        
        assertThat(cities.size()).isEqualTo(City.values().length - 1);
    }
}
