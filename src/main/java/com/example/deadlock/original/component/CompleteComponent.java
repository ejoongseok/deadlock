package com.example.deadlock.original.component;

import com.example.deadlock.original.domain.BaseEntity;
import com.example.deadlock.original.domain.Child;
import com.example.deadlock.original.domain.Foo;
import com.example.deadlock.original.external.ExternalApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CompleteComponent {
    private final ExternalApi externalApi;
    private final FooComponent fooComponent;
    private final BarComponent barComponent;

    public void execute(BaseEntity base, Child child) {
        final List<Foo> fooList = fooComponent.list(base.getFoo());
        for (Foo foo : fooList) {
            barComponent.execute(foo);
            foo.complete();
        }
        externalApi.call(base, child);
    }
}
