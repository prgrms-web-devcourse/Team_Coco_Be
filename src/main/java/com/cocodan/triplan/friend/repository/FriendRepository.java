package com.cocodan.triplan.friend.repository;

import com.cocodan.triplan.friend.domain.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    List<Long> findByFromId(Long fromId);
}
