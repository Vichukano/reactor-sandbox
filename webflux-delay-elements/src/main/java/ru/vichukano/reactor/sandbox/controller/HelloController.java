package ru.vichukano.reactor.sandbox.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@RestController
public class HelloController {

    @GetMapping("/hello")
    public Mono<String> hello(@RequestParam String id) {
        log.info("Hello request for: {}", id);//reactor-nio threads, count == cores
        return Mono.just("Hello")
                .delayElement(Duration.ofSeconds(2))
                .doOnNext(h -> log.info("Fire response for: {}", id));//parallel thread, count == cores
    }

}
