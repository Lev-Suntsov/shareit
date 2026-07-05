package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExampleService {
    public int sum(int a, int b) {
        log.info("Got a={}, b={}", a, b);
        return a + b;
    }
}
