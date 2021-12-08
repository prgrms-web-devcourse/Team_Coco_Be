package com.cocodan.triplan.post.schedule.service;

import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.post.schedule.domain.SchedulePost;
import com.cocodan.triplan.post.schedule.dto.request.SchedulePostCreatieRequest;
import com.cocodan.triplan.post.schedule.dto.response.SchedulePostResponse;
import com.cocodan.triplan.post.schedule.repository.SchedulePostRepository;
import com.cocodan.triplan.member.service.MemberService;
import com.cocodan.triplan.schedule.domain.Schedule;
import com.cocodan.triplan.schedule.domain.ScheduleThema;
import com.cocodan.triplan.schedule.domain.vo.Thema;
import com.cocodan.triplan.schedule.service.ScheduleService;
import com.cocodan.triplan.spot.domain.vo.City;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SchedulePostService {

    private final int PAGE_SIZE = 10;

    private final MemberService memberService;

    private final ScheduleService scheduleService;

    private final SchedulePostRepository schedulePostRepository;

    public SchedulePostService(MemberService memberService, ScheduleService scheduleService, SchedulePostRepository schedulePostRepository) {
        this.memberService = memberService;
        this.scheduleService = scheduleService;
        this.schedulePostRepository = schedulePostRepository;
    }

    @Transactional(readOnly = true)
    public SchedulePost findById(Long id) {
        return schedulePostRepository.findById(id).orElseThrow(
                () -> new RuntimeException("There is no such schedule post of ID : " + id)
        );
    }

    @Transactional(readOnly = true)
    public List<SchedulePostResponse> getRecentSchedulePostList(Integer pageIndex) {
        List<SchedulePost> schedulePosts =
                schedulePostRepository.findAllByOrderByCreatedDateDesc(PageRequest.of(pageIndex, PAGE_SIZE));
        return schedulePosts.stream().map(schedulePost -> {
            Member member = memberService.findById(schedulePost.getMemberId());
            Schedule schedule = scheduleService.findById(schedulePost.getScheduleId());
            City city = schedulePost.getCity();
            List<Thema> themes = schedule.getScheduleThemas().stream()
                    .map(ScheduleThema::getThema).collect(Collectors.toList());
            String title = schedulePost.getTitle();

            return SchedulePostResponse.from(member, schedule, city, themes, title);
        }).collect(Collectors.toList());
    }

    public Long createSchedulePost(Member member, SchedulePostCreatieRequest request) {
        SchedulePost post = SchedulePost.builder()
                .memberId(member.getId())
                .scheduleId(request.getScheduleId())
                .title(request.title)
                .content(request.content)
                .views(0L)
                .liked(0L)
                .city(City.of(request.city))
                .build();
        SchedulePost savedSchedulePost = schedulePostRepository.save(post);
        return savedSchedulePost.getScheduleId();
    }
}
