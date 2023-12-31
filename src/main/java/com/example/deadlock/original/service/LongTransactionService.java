package com.example.deadlock.original.service;

import com.example.deadlock.original.domain.BaseEntity;
import com.example.deadlock.original.domain.Child;
import com.example.deadlock.original.domain.Doo;
import com.example.deadlock.original.domain.Foo;
import com.example.deadlock.original.domain.Jo;
import com.example.deadlock.original.domain.RootEntity;
import com.example.deadlock.original.external.ExternalApi;
import com.example.deadlock.original.external.ExternalApi2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LongTransactionService {

    private final ExternalApi externalApi;
    private final ExternalApi2 externalApi2;
    private final QueryHandler queryHandler;
    private final SaveHandler saveHandler;

    public void execute(final BaseEntity base) {
        if (base.isAlreadyCompleted()) return;
        final String code = base.getCode();
        final Child child = queryHandler.getChild(code);
        final RootEntity rootEntity = queryHandler.getRootEntity(base);
        final List<Foo> fooList = queryHandler.getFoos(base);
        final List<Doo> dooAllList = queryHandler.getDoos(fooList);
        final List<JoDoo> joDooList = queryHandler.getJoDoos(dooAllList);

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
        saveHandler.extracted(base, dooAllList, rootEntity, joDooList, fooList);
    }

    static final class JoDoo {
        private final Jo jo;
        private final Object joData;

        JoDoo(final Jo jo, final Object joData) {
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
