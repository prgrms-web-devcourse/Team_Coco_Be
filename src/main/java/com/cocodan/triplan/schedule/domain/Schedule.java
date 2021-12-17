package com.cocodan.triplan.schedule.domain;

import com.cocodan.triplan.common.BaseEntity;
import com.cocodan.triplan.schedule.domain.vo.Theme;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Schedule extends BaseEntity {

    public static final int TITLE_MIN_LENGTH = 1;
    public static final int TITLE_MAX_LENGTH = 16;

    public static final int THEME_MAX_COUNT = 6;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleTheme> scheduleThemes = new ArrayList<>();

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Memo> memos = new ArrayList<>();

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Checklist> checklists = new ArrayList<>();

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Voting> votingList = new ArrayList<>();

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DailyScheduleSpot> dailyScheduleSpots = new ArrayList<>();

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleMember> scheduleMembers = new ArrayList<>();

    @Builder
    private Schedule(String title, LocalDate startDate, LocalDate endDate, Long memberId) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.memberId = memberId;
        ScheduleMember owner = createOwner(memberId);
        scheduleMembers.add(owner);
    }

    private ScheduleMember createOwner(Long memberId) {
        return ScheduleMember.builder()
                .schedule(this)
                .memberId(memberId)
                .build();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public List<ScheduleTheme> getScheduleThemes() {
        return scheduleThemes;
    }

    public List<Memo> getMemos() {
        return memos;
    }

    public List<Checklist> getChecklists() {
        return checklists;
    }

    public List<Voting> getVotingList() {
        return votingList;
    }

    public List<DailyScheduleSpot> getDailyScheduleSpots() {
        return dailyScheduleSpots;
    }

    public List<ScheduleMember> getScheduleMembers() {
        return scheduleMembers;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void removeAllSpots() {
        getDailyScheduleSpots().clear();
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateThemes(List<String> themes) {
        this.scheduleThemes.clear();
        themes.stream()
                .map(Theme::valueOf)
                .forEach(theme -> new ScheduleTheme(this, theme));
    }

    public void addMember(long friendId) {
        // TODO : [henry] 여행 멤버 수 제한 6명 검증
        ScheduleMember.builder()
                .memberId(friendId)
                .schedule(this)
                .build();
    }

    public void deleteScheduleMember(ScheduleMember deletedMember) {
        scheduleMembers.remove(deletedMember);
    }
}