package com.cocodan.triplan.post.connection.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "connection_post_comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConnectionPostComment {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    // TODO: 2021.12.05 TP-28 Teru - Connect To Member
    // @ManyToOne
    // @JoinColumn(name = "member_id", referencedColumnName = "id")
    // private Member member;

    @ManyToOne
    @JoinColumn(name = "connection_post_id", referencedColumnName = "id")
    private ConnectionPost connectionPost;

    @Column(name = "content", nullable = false)
    private String content;

    public ConnectionPostComment(ConnectionPost connectionPost, String content) {
        this.connectionPost = connectionPost;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public ConnectionPost getConnectionPost() {
        return connectionPost;
    }

    public String getContent() {
        return content;
    }
}