package com.example.deadlock.original.service;

import com.example.deadlock.original.component.DooComponent;
import com.example.deadlock.original.component.DooHandler;
import com.example.deadlock.original.component.FooComponent;
import com.example.deadlock.original.component.UpdateComponent;
import com.example.deadlock.original.domain.BaseEntity;
import com.example.deadlock.original.domain.Child;
import com.example.deadlock.original.domain.ChildRepository;
import com.example.deadlock.original.domain.Doo;
import com.example.deadlock.original.domain.Foo;
import com.example.deadlock.original.domain.Jo;
import com.example.deadlock.original.domain.JoRepository;
import com.example.deadlock.original.domain.RootEntity;
import com.example.deadlock.original.domain.RootRepository;
import com.example.deadlock.original.external.ExternalApi;
import com.example.deadlock.original.external.ExternalApi2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LongTransactionService {

    private final ChildRepository childRepository;
    private final RootRepository rootRepository;
    private final UpdateComponent updateComponent;
    private final ExternalApi externalApi;
    private final FooComponent fooComponent;
    private final DooComponent dooComponent;
    private final DooHandler dooHandler;
    private final JoRepository joRepository;
    private final ExternalApi2 externalApi2;

    @Transactional
    public void execute(final BaseEntity base) {
        if (base.isAlreadyCompleted()) return;
        final String code = base.getCode();
        final Child child = getChild(code);
        base.setData(child.getData());
        final List<Foo> fooList = fooComponent.list(base.getFoo());
        for (final Foo foo : fooList) {
            final List<Doo> dooList = dooComponent.list(foo.getId());
            for (final Doo doo : dooList) {
                final Long id = doo.getId();
                final Jo jo = joRepository.findById(id).orElseThrow(() -> new RuntimeException("not found"));
                jo.handle(doo.getJoData());

                doo.cool();
            }
            dooList.forEach(doo -> dooHandler.handle(doo));
            foo.complete();
        }
        externalApi.call(base, child);
        base.complete();
        final RootEntity rootEntity = rootRepository.findById(base.getRootId()).orElseThrow();
        rootEntity.complete();
        rootRepository.save(rootEntity);
        externalApi2.call(rootEntity.getId());
    }

    private Child getChild(final String code) {
        return childRepository.findByCode(code).orElseThrow(() -> new RuntimeException("not found"));
    }

}
