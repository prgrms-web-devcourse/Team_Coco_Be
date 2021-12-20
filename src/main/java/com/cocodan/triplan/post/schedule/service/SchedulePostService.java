package com.cocodan.triplan.post.schedule.service;

import com.cocodan.triplan.exception.common.ForbiddenException;
import com.cocodan.triplan.exception.common.NotFoundException;
import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.member.repository.MemberRepository;
import com.cocodan.triplan.post.schedule.domain.Like;
import com.cocodan.triplan.post.schedule.domain.SchedulePost;
import com.cocodan.triplan.post.schedule.domain.SchedulePostComment;
import com.cocodan.triplan.post.schedule.domain.SchedulePostNestedComment;
import com.cocodan.triplan.post.schedule.dto.request.SchedulePostCommentRequest;
import com.cocodan.triplan.post.schedule.dto.request.SchedulePostLikeRequest;
import com.cocodan.triplan.post.schedule.dto.request.SchedulePostRequest;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostCommentResponse;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostDetailResponse;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostNestedCommentResponse;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostResponse;
import com.cocodan.triplan.post.schedule.repository.SchedulePostCommentRepository;
import com.cocodan.triplan.post.schedule.repository.SchedulePostLikeRepository;
import com.cocodan.triplan.post.schedule.repository.SchedulePostNestedCommentRepository;
import com.cocodan.triplan.post.schedule.repository.SchedulePostRepository;
import com.cocodan.triplan.schedule.domain.Schedule;
import com.cocodan.triplan.schedule.repository.ScheduleRepository;
import com.cocodan.triplan.spot.domain.vo.City;
import com.cocodan.triplan.util.ExceptionMessageUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SchedulePostService {

    private final MemberRepository memberRepository;
    private final ScheduleRepository scheduleRepository;

    private final SchedulePostLikeRepository schedulePostLikeRepository;

    private final SchedulePostCommentRepository schedulePostCommentRepository;

    private final SchedulePostNestedCommentRepository schedulePostNestedCommentRepository;

    private final SchedulePostRepository schedulePostRepository;

    public SchedulePostService(
            MemberRepository memberRepository,
            ScheduleRepository scheduleRepository,
            SchedulePostLikeRepository schedulePostLikeRepository,
            SchedulePostCommentRepository schedulePostCommentRepository,
            SchedulePostNestedCommentRepository schedulePostNestedCommentRepository,
            SchedulePostRepository schedulePostRepository
    ) {
        this.memberRepository = memberRepository;
        this.scheduleRepository = scheduleRepository;
        this.schedulePostLikeRepository = schedulePostLikeRepository;
        this.schedulePostCommentRepository = schedulePostCommentRepository;
        this.schedulePostNestedCommentRepository = schedulePostNestedCommentRepository;
        this.schedulePostRepository = schedulePostRepository;
    }

    @Transactional(readOnly = true)
    public SchedulePost findById(Long id) {
        return schedulePostRepository.findById(id).orElseThrow(
                () -> new RuntimeException("There is no such schedule post of ID : " + id)
        );
    }


    @Transactional
    public Long createSchedulePost(Long memberId, SchedulePostRequest request) {
        Member member = getMember(memberId);
        Schedule schedule = getSchedule(request.getScheduleId());

        SchedulePost post = SchedulePost.builder()
                .member(member)
                .schedule(schedule)
                .title(request.title)
                .content(request.content)
                .views(0L)
                .liked(0L)
                .city(City.from(request.city))
                .build();

        SchedulePost savedSchedulePost = schedulePostRepository.save(post);
        return savedSchedulePost.getId();
    }

    public List<String> getAvailableCities() {
        return Arrays.stream(City.values())
                .filter(city -> !city.equals(City.ALL))
                .map(City::toString)
                .collect(Collectors.toList());
    }

    @Transactional
    public SchedulePostDetailResponse getSchedulePostDetail(Long schedulePostId, Long memberId) {
        nullCheck(schedulePostId, memberId);

        SchedulePost schedulePost = getSchedulePost(schedulePostId);

        // TODO: 2021.12.13 Teru - 조회수에 대한 동시성 문제를 어떻게 해야 잘 해결할 수 있을지 고민... 현재는 별다른 처리를 해두지 않은 상태
        schedulePost.increaseViews();
        schedulePostRepository.save(schedulePost);

        List<SchedulePostCommentResponse> comments = getSchedulePostComments(schedulePostId);
        Optional<Like> isLiked = getLike(memberId, schedulePostId);
        return SchedulePostDetailResponse.of(schedulePost, comments, isLiked.isPresent());
    }

    @Transactional
    public void deleteSchedulePost(Long memberId, Long schedulePostId) {
        SchedulePost schedulePost = validateAuthorities(memberId, schedulePostId);
        schedulePostRepository.delete(schedulePost);
    }

    @Transactional
    public void modifySchedulePost(Long memberId, Long schedulePostId, SchedulePostRequest request) {
        SchedulePost schedulePost = validateAuthorities(memberId, schedulePostId);

        schedulePost.updateTitle(request.getTitle());
        schedulePost.updateContent(request.getContent());
        schedulePost.updateCity(City.from(request.getCity()));

        Schedule schedule = getSchedule(request.getScheduleId());

        validateOwnership(memberId, schedule);
        schedulePost.updateSchedule(schedule);

        schedulePostRepository.save(schedulePost);
    }

    @Transactional
    public Long toggleSchedulePostLiked(Long memberId, Long schedulePostId, SchedulePostLikeRequest request) {
        Optional<Like> likeData = getLike(memberId, schedulePostId);
        SchedulePost post = getSchedulePostForLikeUpdate(schedulePostId);

        if (likeData.isEmpty() && request.getFlag()) {
            Member member = getMember(memberId);
            Like like = new Like(member, post);
            schedulePostLikeRepository.save(like);
            post.increaseLiked();
        } else if (likeData.isPresent() && !request.getFlag()) {
            schedulePostLikeRepository.delete(likeData.get());
            post.decreaseLiked();
        }

        return schedulePostRepository.save(post).getId();
    }

    @Transactional(readOnly = true)
    public List<SchedulePostResponse> getLikedSchedulePosts(Long memberId) {
        List<Like> likeData = getAllLike(memberId);
        List<SchedulePost> schedulePosts = likeData.stream()
                .map(Like::getSchedulePost)
                .collect(Collectors.toList());
        return convertToSchedulePostResponseList(schedulePosts);
    }

    @Transactional(readOnly = true)
    public List<SchedulePostCommentResponse> getSchedulePostComments(Long schedulePostId) {
        nullCheck(schedulePostId);

        SchedulePost schedulePost = getSchedulePost(schedulePostId);

        return getCommentsOf(schedulePost).stream()
                .map(comment -> SchedulePostCommentResponse.of(
                                comment,
                                getNestedCommentsOf(comment)
                                        .stream()
                                        .map(SchedulePostNestedCommentResponse::from)
                                        .collect(Collectors.toList())
                        )
                )
                .collect(Collectors.toList());
    }

    @Transactional
    public List<SchedulePostCommentResponse> writeSchedulePostComment(Long memberId, Long schedulePostId, SchedulePostCommentRequest request) {
        nullCheck(memberId, schedulePostId);

        SchedulePost schedulePost = getSchedulePost(schedulePostId);
        Member member = getMember(memberId);

        SchedulePostComment comment = SchedulePostComment.builder()
                .member(member)
                .schedulePost(schedulePost)
                .content(request.getContent())
                .build();

        schedulePostCommentRepository.save(comment);

        return getSchedulePostComments(schedulePostId);
    }

    @Transactional
    public void deleteSchedulePostComment(Long schedulePostId, Long commentId, Long memberId) {
        validateCommentOwnership(schedulePostId, commentId, memberId);
        schedulePostCommentRepository.deleteById(commentId);
    }

    @Transactional
    public void modifySchedulePostComment(Long schedulePostId, Long commentId, Long memberId, SchedulePostCommentRequest request) {
        validateCommentOwnership(schedulePostId, commentId, memberId);
        SchedulePostComment comment = getComment(commentId);
        comment.updateContent(request.getContent());
        schedulePostCommentRepository.save(comment);
    }

    @Transactional
    public List<SchedulePostCommentResponse> writeNestedCommentToSchedulePostComment(
            Long memberId,
            Long schedulePostId,
            Long commentId,
            SchedulePostCommentRequest request
    ) {
        nullCheck(memberId, schedulePostId, commentId);

        SchedulePostComment comment = getComment(commentId);
        Member member = getMember(memberId);

        SchedulePostNestedComment nestedComment = SchedulePostNestedComment.builder()
                .parentComment(comment)
                .member(member)
                .content(request.getContent())
                .build();

        schedulePostNestedCommentRepository.save(nestedComment);
        return getSchedulePostComments(schedulePostId);
    }

    @Transactional(readOnly = true)
    public List<SchedulePostNestedCommentResponse> getSchedulePostNestedComments(Long schedulePostId, Long commentId, Long memberId) {
        nullCheck(schedulePostId, commentId, memberId);

        SchedulePostComment comment = getComment(commentId);
        List<SchedulePostNestedComment> nestedComments = getNestedCommentsOf(comment);
        return nestedComments.stream()
                .map(SchedulePostNestedCommentResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void modifySchedulePostNestedComment(
            Long memberId,
            Long schedulePostId,
            Long commentId,
            Long nestedCommentId,
            SchedulePostCommentRequest request
    ) {
        validateNestedCommentOwnership(memberId, schedulePostId, commentId, nestedCommentId);
        SchedulePostNestedComment nestedComment = getNestedComment(nestedCommentId);
        nestedComment.updateContent(request.getContent());
    }

    @Transactional
    public void deleteSchedulePostNestedComment(Long memberId, Long schedulePostId, Long commentId, Long nestedCommentId) {
        validateNestedCommentOwnership(memberId, schedulePostId, commentId, nestedCommentId);
        SchedulePostNestedComment nestedComment = getNestedComment(nestedCommentId);
        schedulePostNestedCommentRepository.delete(nestedComment);
    }

    @Transactional(readOnly = true)
    public List<SchedulePostResponse> getCertainMemberSchedulePostList(Long memberId) {
        nullCheck(memberId);

        List<SchedulePost> schedulePosts = getSchedulePostsByMemberId(memberId);
        return convertToSchedulePostResponseList(schedulePosts);
    }

    // private methods...
    private void validateNestedCommentOwnership(Long memberId, Long schedulePostId, Long commentId, Long nestedCommentId) {
        nullCheck(memberId, schedulePostId, commentId, nestedCommentId);

        getSchedulePost(schedulePostId);
        getComment(commentId);
        SchedulePostNestedComment nestedComment = getNestedComment(nestedCommentId);
        validateOwnership(memberId, nestedComment);
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> new NotFoundException(Member.class, memberId)
        );
    }

    private Schedule getSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new NotFoundException(Schedule.class, scheduleId)
        );
    }

    private SchedulePost getSchedulePost(Long schedulePostId) {
        return schedulePostRepository.findById(schedulePostId).orElseThrow(
                () -> new NotFoundException(SchedulePost.class, schedulePostId)
        );
    }

    private SchedulePost getSchedulePostForLikeUpdate(Long schedulePostId) {
        return schedulePostRepository.findByIdForLikedCountUpdate(schedulePostId).orElseThrow(
                () -> new NotFoundException(SchedulePost.class, schedulePostId)
        );
    }

    private SchedulePostComment getComment(Long commentId) {
        return schedulePostCommentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException(SchedulePostComment.class, commentId)
        );
    }

    private SchedulePostNestedComment getNestedComment(Long nestedCommentId) {
        return schedulePostNestedCommentRepository.findById(nestedCommentId).orElseThrow(
                () -> new NotFoundException(SchedulePostNestedComment.class, nestedCommentId)
        );
    }

    private List<SchedulePostNestedComment> getNestedCommentsOf(SchedulePostComment comment) {
        return schedulePostNestedCommentRepository.findAllByCommentId(comment.getId());
    }

    private List<SchedulePostComment> getCommentsOf(SchedulePost schedulePost) {
        return schedulePostCommentRepository.findAllBySchedulePostId(schedulePost.getId());
    }

    private Optional<Like> getLike(Long memberId, Long schedulePostId) {
        return schedulePostLikeRepository.findByMemberIdAndSchedulePostId(memberId, schedulePostId);
    }

    private List<Like> getAllLike(Long memberId) {
        return schedulePostLikeRepository.findAllByMemberId(memberId);
    }

    private List<SchedulePostResponse> convertToSchedulePostResponseList(List<SchedulePost> schedulePosts) {
        return schedulePosts.stream().map(SchedulePostResponse::from).collect(Collectors.toList());
    }

    private List<SchedulePost> getSchedulePostsByMemberId(Long memberId) {
        return  schedulePostRepository.findAllByMemberId(memberId);
    }

    private SchedulePost validateAuthorities(Long memberId, Long schedulePostId) {
        SchedulePost schedulePost = getSchedulePost(schedulePostId);
        validateOwnership(memberId, schedulePost);
        return schedulePost;
    }

    private void validateOwnership(Long memberId, Schedule schedule) {
        nullCheck(memberId);

        if (!memberId.equals(schedule.getMemberId())) {
            throw new ForbiddenException(Schedule.class, schedule.getId(), memberId);
        }
    }

    private void validateCommentOwnership(Long schedulePostId, Long commentId, Long memberId) {
        nullCheck(schedulePostId, commentId, memberId);

        SchedulePost schedulePost = getSchedulePost(schedulePostId);
        SchedulePostComment comment = getComment(commentId);
        validateOwnership(memberId, comment);
    }

    private void validateOwnership(Long memberId, SchedulePost schedulePost) {
        if (!memberId.equals(schedulePost.getMember().getId())) {
            throw new ForbiddenException(SchedulePost.class, schedulePost.getId(), memberId);
        }
    }

    private void validateOwnership(Long memberId, SchedulePostComment comment) {
        if (!memberId.equals(comment.getMember().getId())) {
            throw new ForbiddenException(SchedulePostComment.class, comment.getId(), memberId);
        }
    }

    private void validateOwnership(Long memberId, SchedulePostNestedComment nestedComment) {
        if (!memberId.equals(nestedComment.getMember().getId())) {
            throw new ForbiddenException(SchedulePostNestedComment.class, nestedComment.getId(), memberId);
        }
    }

    private void nullCheck(Object... args) {
        for (Object obj : args) {
            if (obj == null) {
                throw new IllegalArgumentException(
                        ExceptionMessageUtils
                                .getMessage("exception.argument_not_valid"));
            }
        }
    }
}
