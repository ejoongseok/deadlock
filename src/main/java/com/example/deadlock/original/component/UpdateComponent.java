package com.example.deadlock.original.component;

import com.example.deadlock.original.domain.RootEntity;
import com.example.deadlock.original.domain.RootRepository;
import com.example.deadlock.original.external.ExternalApi2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateComponent {
    private final RootRepository rootRepository;
    private final ExternalApi2 externalApi2;

    public void execute(RootEntity rootEntity) {
        rootRepository.save(rootEntity);
        externalApi2.call(rootEntity.getId());
    }
}
