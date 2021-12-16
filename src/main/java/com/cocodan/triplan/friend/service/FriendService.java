package com.cocodan.triplan.friend.service;

import com.cocodan.triplan.friend.domain.Friend;
import com.cocodan.triplan.friend.repository.FriendRepository;
import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.member.dto.response.MemberSimpleResponse;
import com.cocodan.triplan.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;

    private final MemberRepository memberRepository;

    @Transactional
    public void addFollowing(Long followerId, Long followingId) {
        Friend friend = new Friend(followerId, followingId);

        friendRepository.save(friend);
    }

    @Transactional(readOnly = true)
    public List<MemberSimpleResponse> getMyFollowing(Long id) {

        List<Long> myFollowingIds = friendRepository.findByFromId(id);

        List<Member> followings = memberRepository.findByIdIn(myFollowingIds);

        return followings.stream()
                .map(MemberSimpleResponse::from)
                .collect(Collectors.toList());
    }
}
