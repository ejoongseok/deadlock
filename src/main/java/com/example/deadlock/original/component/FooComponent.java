package com.example.deadlock.original.component;

import com.example.deadlock.original.domain.Foo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FooComponent {
    public List<Foo> list(final String foo) {
        return List.of(new Foo());
    }
}
