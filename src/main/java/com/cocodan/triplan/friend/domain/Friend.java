package com.cocodan.triplan.friend.domain;

import com.cocodan.triplan.friend.domain.vo.FriendId;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friend {

    @EmbeddedId
    private FriendId friendId;

    public Friend(Long fromId, Long toId) {
        friendId = new FriendId(fromId, toId);
    }
}
