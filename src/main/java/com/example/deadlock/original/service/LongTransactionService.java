package com.example.deadlock.original.service;

import com.example.deadlock.original.component.DooComponent;
import com.example.deadlock.original.component.DooHandler;
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
import com.example.deadlock.original.external.ExternalApi;
import com.example.deadlock.original.external.ExternalApi2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LongTransactionService {

    private final ChildRepository childRepository;
    private final RootRepository rootRepository;
    private final ExternalApi externalApi;
    private final FooComponent fooComponent;
    private final DooComponent dooComponent;
    private final DooHandler dooHandler;
    private final JoRepository joRepository;
    private final ExternalApi2 externalApi2;
    private final FooRepository fooRepository;
    private final BaseRepository baseRepository;

    @Transactional
    public void execute(final BaseEntity base) {
        if (base.isAlreadyCompleted()) return;
        final String code = base.getCode();
        final Child child = getChild(code);
        final RootEntity rootEntity = rootRepository.findById(base.getRootId()).orElseThrow();
        final List<Foo> fooList = fooComponent.list(base.getFoo());
        final List<Doo> dooAllList = new ArrayList<>();
        for (final Foo foo : fooList) {
            final List<Doo> dooList = dooComponent.list(foo.getId());
            dooAllList.addAll(dooList);
        }
        final List<JoDoo> joDooList = new ArrayList<>();
        for (final Doo doo : dooAllList) {
            final Long id = doo.getId();
            final Jo jo = joRepository.findById(id).orElseThrow(() -> new RuntimeException("not found"));
            joDooList.add(new JoDoo(jo, doo.getJoData()));
        }

        base.setData(child.getData());
        for (final JoDoo joDoo : joDooList) {
            final Jo jo = joDoo.jo();
            jo.handle(joDoo.joData());
        }
        for (final Doo doo : dooAllList) {
            doo.cool();
        }
        for (final Foo foo : fooList) {
            foo.complete();
        }
        base.complete();
        rootEntity.complete();

        externalApi.call(base, child);
        externalApi2.call(rootEntity.getId());
        for (final Doo doo : dooAllList) {
            dooHandler.save(doo);
        }
        rootRepository.save(rootEntity);
        for (final JoDoo joDoo : joDooList) {
            joRepository.save(joDoo.jo());
        }
        for (final Foo foo : fooList) {
            fooRepository.save(foo);
        }
        baseRepository.save(base);
    }

    private Child getChild(final String code) {
        return childRepository.findByCode(code).orElseThrow(() -> new RuntimeException("not found"));
    }

    private static final class JoDoo {
        private final Jo jo;
        private final Object joData;

        private JoDoo(final Jo jo, final Object joData) {
            this.jo = jo;
            this.joData = joData;
        }

        public Jo jo() {
            return jo;
        }

        public Object joData() {
            return joData;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == this) return true;
            if (null == obj || obj.getClass() != getClass()) return false;
            final var that = (JoDoo) obj;
            return Objects.equals(jo, that.jo) &&
                    Objects.equals(joData, that.joData);
        }

        @Override
        public int hashCode() {
            return Objects.hash(jo, joData);
        }

        @Override
        public String toString() {
            return "JoDoo[" +
                    "jo=" + jo + ", " +
                    "joData=" + joData + ']';
        }

    }
}
