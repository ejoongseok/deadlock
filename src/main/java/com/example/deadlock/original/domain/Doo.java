package com.example.deadlock.original.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Doo {
    @Id
    private Long id;
    public Long getId() {
        return 1L;
    }

    public Object getJoData() {
        return new Jo();
    }

    public void cool() {
        System.out.println("cool");
    }
}
