package com.example.deadlock.original.external;

import com.example.deadlock.original.domain.BaseEntity;
import com.example.deadlock.original.domain.Child;
import org.springframework.stereotype.Component;

@Component
public class ExternalApi {
    public void call(BaseEntity base, Child child) {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
