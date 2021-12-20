package com.cocodan.triplan.friend.domain.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendId implements Serializable {

    @Column(name = "from_id", nullable = false)
    private Long fromId;

    @Column(name = "to_id", nullable = false)
    private Long toId;

    public FriendId(Long fromId, Long toId) {
        this.fromId = fromId;
        this.toId = toId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FriendId friendId = (FriendId) o;

        if (!fromId.equals(friendId.fromId)) return false;
        return toId.equals(friendId.toId);
    }

    @Override
    public int hashCode() {
        int result = fromId.hashCode();
        result = 31 * result + toId.hashCode();
        return result;
    }
}
