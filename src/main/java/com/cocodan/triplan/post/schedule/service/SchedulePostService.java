package com.cocodan.triplan.post.schedule.service;

import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.member.dto.response.MemberGetOneResponse;
import com.cocodan.triplan.member.repository.MemberRepository;
import com.cocodan.triplan.post.schedule.domain.SchedulePost;
import com.cocodan.triplan.post.schedule.dto.request.SchedulePostCreateRequest;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostDetailResponse;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostResponse;
import com.cocodan.triplan.post.schedule.repository.SchedulePostRepository;
import com.cocodan.triplan.member.service.MemberService;
import com.cocodan.triplan.post.schedule.vo.SchedulePostSortingRule;
import com.cocodan.triplan.schedule.domain.Schedule;
import com.cocodan.triplan.schedule.domain.ScheduleThema;
import com.cocodan.triplan.schedule.domain.vo.Theme;
import com.cocodan.triplan.schedule.repository.ScheduleRepository;
import com.cocodan.triplan.schedule.service.ScheduleService;
import com.cocodan.triplan.spot.domain.vo.City;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SchedulePostService {

    private final int PAGE_SIZE = 10;

    private final MemberService memberService;

    private final MemberRepository memberRepository;
    private final ScheduleRepository scheduleRepository;

    private final SchedulePostRepository schedulePostRepository;

    public SchedulePostService(MemberService memberService, ScheduleService scheduleService, MemberRepository memberRepository, ScheduleRepository scheduleRepository, SchedulePostRepository schedulePostRepository) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
        this.scheduleRepository = scheduleRepository;
        this.schedulePostRepository = schedulePostRepository;
    }

    @Transactional(readOnly = true)
    public SchedulePost findById(Long id) {
        return schedulePostRepository.findById(id).orElseThrow(
                () -> new RuntimeException("There is no such schedule post of ID : " + id)
        );
    }

    public Long createSchedulePost(Long memberId, SchedulePostCreateRequest request) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new RuntimeException("Invalid User Detected")
        );
        SchedulePost post = SchedulePost.builder()
                .member(member)
                .schedule(scheduleRepository.findById(request.getScheduleId()).orElseThrow(
                        () -> new RuntimeException("There is no such schedule (ID : " + request.getScheduleId() + ")")
                ))
                .title(request.title)
                .content(request.content)
                .views(0L)
                .liked(0L)
                .city(City.of(request.city))
                .build();

        SchedulePost savedSchedulePost = schedulePostRepository.save(post);
        return savedSchedulePost.getSchedule().getId();
    }

    @Transactional(readOnly = true)
    public List<SchedulePostResponse> getSchedulePosts(
            String search,
            City city,
            Theme theme,
            SchedulePostSortingRule sortRule,
            Integer pageIndex
    ) {
        // TODO: 2021.12.09
        //  1. 동적 쿼리 적용 등으로 아래 로직 개선...
        //  2. theme의 경우 현재 쿼리단계에서 직접 접근이 불가능함. Schedule 도메인쪽에서 수정을 해주거나, 테마필터 없이 조회 후 서비스단에서 테마로 필터링을 해 주어야 한다.
        //  3. 좋아요, 댓글 기능 구현 후 좋아요 많은순, 댓글 많은순으로 조회하는 기능도 추가
        if (city == City.ALL) {
            // 지정된 특정 도시 없음
            if (!search.isEmpty()) {
                // 검색어 없음
                switch (sortRule) {
                    case RECENT:
                        List<SchedulePost> recentPosts = schedulePostRepository.findAllByOrderByCreatedDateDesc(PageRequest.of(pageIndex, PAGE_SIZE));
                        return convertToSchedulePostResponseList(recentPosts);
                    case VIEWS:
                        List<SchedulePost> famousPosts = schedulePostRepository.findAllByOrderByViewsDesc(PageRequest.of(pageIndex, PAGE_SIZE));
                        return convertToSchedulePostResponseList(famousPosts);
                    case COMMENTS:
                    case LIKE:
                }
            } else {
                // 검색어 있음
                switch (sortRule) {
                    case RECENT:
                        List<SchedulePost> recentPosts = schedulePostRepository.findAllByTitleOrContentContainingOrderByCreatedDateDesc(search, search, PageRequest.of(pageIndex, PAGE_SIZE));
                        return convertToSchedulePostResponseList(recentPosts);
                    case VIEWS:
                        List<SchedulePost> famousPosts = schedulePostRepository.findAllByTitleOrContentContainingOrderByViewsDesc(search, search,PageRequest.of(pageIndex, PAGE_SIZE));
                        return convertToSchedulePostResponseList(famousPosts);
                    case COMMENTS:
                    case LIKE:
                }
            }
        } else {
            // 지정된 특정 도시 있음
            if (!search.isEmpty()) {// 검색어 없음
                switch (sortRule) {
                    case RECENT:
                        List<SchedulePost> recentPosts = schedulePostRepository.findAllByCityOrderByCreatedDateDesc(city, PageRequest.of(pageIndex, PAGE_SIZE));
                        return convertToSchedulePostResponseList(recentPosts);
                    case VIEWS:
                        List<SchedulePost> famousPosts = schedulePostRepository.findAllByCityOrderByViewsDesc(city, PageRequest.of(pageIndex, PAGE_SIZE));
                        return convertToSchedulePostResponseList(famousPosts);
                    case COMMENTS:
                    case LIKE:
                }
            } else {
                // 검색어 있음
                switch (sortRule) {
                    case RECENT:
                        List<SchedulePost> recentPosts = schedulePostRepository.findAllByCityAndTitleOrContentContainingOrderByCreatedDateDesc(city, search, search, PageRequest.of(pageIndex, PAGE_SIZE));
                        return convertToSchedulePostResponseList(recentPosts);
                    case VIEWS:
                        List<SchedulePost> famousPosts = schedulePostRepository.findAllByCityAndTitleOrContentContainingOrderByViewsDesc(city, search, search, PageRequest.of(pageIndex, PAGE_SIZE));
                        return convertToSchedulePostResponseList(famousPosts);
                    case COMMENTS:
                    case LIKE:
                }
            }
        }
        // 도달해서는 안되는 부분 -> 댓글, 좋아요 구현되면 해결될 것으로 예상됨
        return Collections.emptyList();
    }

    @Transactional(readOnly = true)
    public SchedulePostDetailResponse getSchedulePostDetail(Long postId) {
        SchedulePost schedulePost = schedulePostRepository.findById(postId).orElseThrow(
                () -> new RuntimeException("No such post found (ID : " + postId + ")")
        );

        return SchedulePostDetailResponse.from(schedulePost);
    }

    private List<SchedulePostResponse> convertToSchedulePostResponseList(List<SchedulePost> schedulePosts) {
        return schedulePosts.stream().map(schedulePost -> {
            MemberGetOneResponse memberResponse = memberService.getOne(schedulePost.getMember().getId());
            Schedule schedule = schedulePost.getSchedule();
            City city = schedulePost.getCity();
            List<Theme> themes = schedule.getScheduleThemas().stream()
                    .map(ScheduleThema::getThema).collect(Collectors.toList());
            String title = schedulePost.getTitle();

            return SchedulePostResponse.from(memberResponse, schedule, city, themes, title);
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public void validateRemovable(Long memberId, Long schedulePostId) {
        SchedulePost schedulePost = validateExistence(schedulePostId);
        validateOwnership(memberId, schedulePost);
    }

    private void validateOwnership(Long memberId, SchedulePost schedulePost) {
        if (!memberId.equals(schedulePost.getMember().getId())) {
            throw new RuntimeException("Invalid access for schedule post. Only owner can access to it");
        }
    }

    private SchedulePost validateExistence(Long schedulePostId) {
        return schedulePostRepository.findById(schedulePostId).orElseThrow(
                () -> new RuntimeException("No schedule post found (ID : " + schedulePostId + ")")
        );

    }

    public void deleteSchedulePost(Long schedulePostId) {
        schedulePostRepository.deleteById(schedulePostId);
    }
}
