package com.cocodan.triplan.friend.service;

import com.cocodan.triplan.exception.common.NoFriendsException;
import com.cocodan.triplan.friend.domain.Friend;
import com.cocodan.triplan.friend.repository.FriendRepository;
import com.cocodan.triplan.member.domain.Member;
import com.cocodan.triplan.member.dto.response.MemberSimpleResponse;
import com.cocodan.triplan.member.repository.MemberRepository;
import com.cocodan.triplan.util.ExceptionMessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;

    private final MemberRepository memberRepository;

    @Transactional
    public void addFollowing(Long toId, Long fromId) {
        if (toId.equals(fromId)) {
            throw new IllegalArgumentException(ExceptionMessageUtils.getMessage("friend_myself"));
        }

        Friend friend = new Friend(fromId, toId);

        Optional<Friend> optionalFriend = friendRepository.findByFromIdAndToId(fromId, toId);
        if (optionalFriend.isPresent()) {
            throw new IllegalArgumentException(ExceptionMessageUtils.getMessage("already_friend"));
        }

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

    @Transactional
    public void deleteFollowing(Long toId, Long fromId) {
        Friend friend = friendRepository.findByFromIdAndToId(fromId, toId)
                .orElseThrow(() -> new NoFriendsException(toId, fromId));

        friendRepository.delete(friend);
    }
}
