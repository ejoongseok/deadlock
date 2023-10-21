package com.example.deadlock.original.service;

import com.example.deadlock.original.component.DooComponent;
import com.example.deadlock.original.component.FooComponent;
import com.example.deadlock.original.domain.BaseEntity;
import com.example.deadlock.original.domain.BaseRepository;
import com.example.deadlock.original.domain.Child;
import com.example.deadlock.original.domain.ChildRepository;
import com.example.deadlock.original.domain.Doo;
import com.example.deadlock.original.domain.Foo;
import com.example.deadlock.original.domain.FooRepository;
import com.example.deadlock.original.domain.Jo;
import com.example.deadlock.original.domain.JoRepository;
import com.example.deadlock.original.domain.RootEntity;
import com.example.deadlock.original.domain.RootRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryHandler {
    private final ChildRepository childRepository;
    private final RootRepository rootRepository;
    private final FooComponent fooComponent;
    private final DooComponent dooComponent;
    private final JoRepository joRepository;

    private final FooRepository fooRepository;
    private final BaseRepository baseRepository;


    public JoRepository getJoRepository() {
        return joRepository;
    }

    public FooRepository getFooRepository() {
        return fooRepository;
    }

    public BaseRepository getBaseRepository() {
        return baseRepository;
    }

    List<Foo> getFoos(final BaseEntity base) {
        final List<Foo> fooList = fooComponent.list(base.getFoo());
        return fooList;
    }

    RootEntity getRootEntity(final BaseEntity base) {
        final RootEntity rootEntity = rootRepository.findById(base.getRootId()).orElseThrow();
        return rootEntity;
    }

    List<Doo> getDoos(final List<Foo> fooList) {
        final List<Doo> dooAllList = new ArrayList<Doo>();
        for (final Foo foo : fooList) {
            final List<Doo> dooList = dooComponent.list(foo.getId());
            dooAllList.addAll(dooList);
        }
        return dooAllList;
    }

    List<LongTransactionService.JoDoo> getJoDoos(final List<Doo> dooAllList) {
        final List<LongTransactionService.JoDoo> joDooList = new ArrayList<LongTransactionService.JoDoo>();
        for (final Doo doo : dooAllList) {
            final Long id = doo.getId();
            final Jo jo = joRepository.findById(id).orElseThrow(() -> new RuntimeException("not found"));
            joDooList.add(new LongTransactionService.JoDoo(jo, doo.getJoData()));
        }
        return joDooList;
    }

    Child getChild(final String code) {
        return childRepository.findByCode(code).orElseThrow(() -> new RuntimeException("not found"));
    }
}