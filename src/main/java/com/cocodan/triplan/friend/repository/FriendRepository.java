package com.cocodan.triplan.friend.repository;

import com.cocodan.triplan.friend.domain.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Query(value = "SELECT f.toId FROM Friend f WHERE f.fromId = :fromId")
    List<Long> findByFromId(Long fromId);

    Optional<Friend> findByFromIdAndToId(Long fromId, Long toId);
}
