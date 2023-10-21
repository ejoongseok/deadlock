package com.example.deadlock.original.external;

import org.springframework.stereotype.Component;

@Component
public class ExternalApi2 {
    public void call(Long id) {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
