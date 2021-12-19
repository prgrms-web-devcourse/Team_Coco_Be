package com.cocodan.triplan.schedule.domain;

import com.cocodan.triplan.common.BaseEntity;
import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.google.common.base.Preconditions.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleMember extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", referencedColumnName = "id")
    private Schedule schedule;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Builder
    private ScheduleMember(Schedule schedule, Long memberId) {
        checkArgument(schedule != null, "Schedule is required");
        checkArgument(memberId != null, "MemberId is required");
        checkArgument(memberId > 0, "MemberId must be positive, you supplied %d", memberId);

        this.schedule = schedule;
        this.memberId = memberId;
        schedule.getScheduleMembers().add(this);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScheduleMember that = (ScheduleMember) o;

        if (!id.equals(that.id)) return false;
        if (!schedule.equals(that.schedule)) return false;
        return memberId.equals(that.memberId);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + schedule.hashCode();
        result = 31 * result + memberId.hashCode();
        return result;
    }
}
