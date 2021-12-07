package com.cocodan.triplan.post.schedule.service;

import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.post.schedule.domain.SchedulePost;
import com.cocodan.triplan.post.schedule.dto.SchedulePostResponse;
import com.cocodan.triplan.post.schedule.repository.SchedulePostRepository;
import com.cocodan.triplan.member.service.MemberService;
import com.cocodan.triplan.schedule.domain.Schedule;
import com.cocodan.triplan.schedule.domain.ScheduleTag;
import com.cocodan.triplan.schedule.domain.vo.Tag;
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
    public List<SchedulePostResponse> getRecentSchedulePostList(Integer pageIndex) {
        List<SchedulePost> schedulePosts =
                schedulePostRepository.findAllByOrderByCreatedDateDesc(PageRequest.of(pageIndex, PAGE_SIZE));
        return schedulePosts.stream().map(schedulePost -> {
            Member member = memberService.findById(schedulePost.getMemberId());
            Schedule schedule = scheduleService.findById(schedulePost.getScheduleId());
            City city = schedulePost.getCity();
            List<Tag> themes = schedule.getScheduleTags().stream()
                    .map(ScheduleTag::getTag).collect(Collectors.toList());
            String title = schedulePost.getTitle();

            return SchedulePostResponse.from(member, schedule, city, themes, title);
        }).collect(Collectors.toList());
    }
}
