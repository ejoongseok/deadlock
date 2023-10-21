package com.example.deadlock.original.service;

import com.example.deadlock.original.component.DooHandler;
import com.example.deadlock.original.domain.BaseEntity;
import com.example.deadlock.original.domain.BaseRepository;
import com.example.deadlock.original.domain.Doo;
import com.example.deadlock.original.domain.Foo;
import com.example.deadlock.original.domain.FooRepository;
import com.example.deadlock.original.domain.JoRepository;
import com.example.deadlock.original.domain.RootEntity;
import com.example.deadlock.original.domain.RootRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SaveHandler {
    private final DooHandler dooHandler;
    private final RootRepository rootRepository;
    private final JoRepository joRepository;
    private final FooRepository fooRepository;
    private final BaseRepository baseRepository;

    @Transactional
    public void extracted(final BaseEntity base, final List<Doo> dooAllList, final RootEntity rootEntity, final List<LongTransactionService.JoDoo> joDooList, final List<Foo> fooList) {
        for (final Doo doo : dooAllList) {
            dooHandler.save(doo);
        }
        rootRepository.save(rootEntity);
        for (final LongTransactionService.JoDoo joDoo : joDooList) {
            joRepository.save(joDoo.jo());
        }
        for (final Foo foo : fooList) {
            fooRepository.save(foo);
        }
        baseRepository.save(base);
    }
}