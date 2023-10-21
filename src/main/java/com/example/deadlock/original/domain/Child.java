package com.example.deadlock.original.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Child {
    @Id
    private Long id;
    private String code;

    public FooData getData() {
        return new FooData();
    }
}
