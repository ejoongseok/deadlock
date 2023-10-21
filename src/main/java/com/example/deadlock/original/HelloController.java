package com.example.deadlock.original;

import com.example.deadlock.original.domain.BaseEntity;
import com.example.deadlock.original.service.LongTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class HelloController {
    private final LongTransactionService longTransactionService;

    public void hello() {
        longTransactionService.execute(new BaseEntity());
    }
}
