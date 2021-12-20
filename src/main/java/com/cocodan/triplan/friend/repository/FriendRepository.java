package com.cocodan.triplan.friend.repository;

import com.cocodan.triplan.friend.domain.Friend;
import com.cocodan.triplan.friend.domain.vo.FriendId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, FriendId> {

    @Query(value = "SELECT f.friendId.toId FROM Friend f WHERE f.friendId.fromId = :fromId")
    List<Long> findByFromId(Long fromId);

    @Query(value = "SELECT f FROM Friend f WHERE f.friendId.fromId = :fromId AND f.friendId.toId = :toId")
    Optional<Friend> findByFromIdAndToId(Long fromId, Long toId);
}
