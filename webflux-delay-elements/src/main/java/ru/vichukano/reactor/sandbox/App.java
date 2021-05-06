package ru.vichukano.reactor.sandbox;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Slf4j
@EnableWebFlux
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Component
    class Runner implements ApplicationRunner {
        private final WebClient webClient;

        @Autowired
        public Runner(WebClient webClient) {
            this.webClient = webClient;
        }

        @Override
        public void run(ApplicationArguments args) throws Exception {
            log.info("Run reactor sandbox. Processor cores: {}", Runtime.getRuntime().availableProcessors());
            Flux<String> stream = Flux.just("one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten");
            stream.map(s -> {
                log.info("Start request for: {}", s);
                return s;//main thread
            }).flatMap(s -> {
                log.info("Start to GET request for: {}", s);//main thread
                return webClient.get()
                        .uri("/hello/?id=" + s)
                        .retrieve()
                        .bodyToMono(String.class)
                        .map(h -> s + " " + h)
                        .doOnNext(r -> log.info("Receive: {}", r));//reactor-nio thread
            })
                    .map(s -> {
                        log.info("Map {} to UPPER", s);//reactor-nio thread
                        return s.toUpperCase();
                    })
                    .doOnNext(s -> log.info("Final result: {}", s))//reactor-nio thread
                    .onErrorContinue((e, m) -> log.error("Exception while processing message", e))
                    .subscribe();
        }

    }
}
