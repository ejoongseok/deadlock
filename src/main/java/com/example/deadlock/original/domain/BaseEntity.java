package com.example.deadlock.original.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class BaseEntity {
    @Id
    private Long id;
    public boolean isAlreadyCompleted() {
        return false;
    }

    public void setData(FooData data) {

    }

    public void complete() {
        System.out.println("complete");
    }

    public String getCode() {
        return "code";
    }

    public Long getRootId() {
        return 1L;
    }

    public String getFoo() {
        return "foo";
    }
}
