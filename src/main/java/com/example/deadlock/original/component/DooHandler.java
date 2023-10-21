package com.example.deadlock.original.component;

import com.example.deadlock.original.domain.Doo;
import org.springframework.stereotype.Component;

@Component
class DooHandler {
    public void handle(Doo doo) {
        System.out.println("handle");
    }
}
