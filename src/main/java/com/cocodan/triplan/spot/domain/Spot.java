package com.cocodan.triplan.spot.domain;

import javax.persistence.*;

@Entity
public class Spot {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

}
