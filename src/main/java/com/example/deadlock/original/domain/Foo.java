package com.example.deadlock.original.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Foo {
    @Id
    private Long id;
    public void complete() {
        System.out.println("complete");
    }

    public Long getId() {
        return 1L;
    }
}
