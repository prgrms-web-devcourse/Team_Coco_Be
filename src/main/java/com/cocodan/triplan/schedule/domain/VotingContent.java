package com.cocodan.triplan.schedule.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.StringJoiner;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VotingContent {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "count")
    private int count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voting_id", referencedColumnName = "id")
    private Voting voting;

    @Builder
    public VotingContent(String content, Voting voting) {
        this.content = content;
        this.voting = voting;
    }

    public void doVoting(boolean flag){
        if (flag) {
            count++;
            return;
        }

        if (count <= 0) {
            throw new RuntimeException("");
        }
        count--;
    }

    public void cancelVoting(){

    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public int getCount() {
        return count;
    }

    public Voting getVoting() {
        return voting;
    }
}
