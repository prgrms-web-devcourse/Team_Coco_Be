package com.cocodan.triplan.connection.domain;

import com.cocodan.triplan.connection.ConnectionRequestStatus;
import com.cocodan.triplan.post.connection.domain.ConnectionPost;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "connections")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Connection {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "connection_post_id", referencedColumnName = "id")
    private ConnectionPost connectionPost;

    // TODO: 2021.12.05 Teru - 회원 엔티티와 연결

    @Column(name = "request_status")
    @Enumerated(value = EnumType.STRING)
    private ConnectionRequestStatus connectionRequestStatus;
}
