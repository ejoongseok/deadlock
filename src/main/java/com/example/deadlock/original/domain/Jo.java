package com.example.deadlock.original.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Jo {
    @Id
    private Long id;
    public void handle(Object joData) {
        System.out.println("handle");
    }
}
