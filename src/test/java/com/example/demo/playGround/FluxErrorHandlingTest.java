package com.example.demo.playGround;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxErrorHandlingTest {

    @Test
    public void test_general() {
        Flux<String> stringFlux = Flux
                .just("a", "b", "c")
                .concatWith(Flux.error(RuntimeException::new))
                .concatWith(Flux.just("d"))
                .log();

        StepVerifier
                .create(stringFlux)
                .expectNext("a", "b", "c")
                .verifyError(RuntimeException.class);

    }

    @Test
    public void test_general_handel_error() {
        Flux<String> stringFlux = Flux
                .just("a", "b", "c")
                .concatWith(Flux.error(RuntimeException::new))
                .concatWith(Flux.just("d"))
                .onErrorResume(throwable -> {
                    System.out.println("Error happened");
                    return Flux.just("default0");
                })
                .log();

        StepVerifier
                .create(stringFlux)
                .expectNext("a", "b", "c")
                .expectNext("default0")
                .verifyComplete();

    }

    @Test
    public void test_general_handel_error_on_return() {
        Flux<String> stringFlux = Flux
                .just("a", "b", "c")
                .concatWith(Flux.error(RuntimeException::new))
                .concatWith(Flux.just("d"))
                .onErrorReturn("default0")
                .log();

        StepVerifier
                .create(stringFlux)
                .expectNext("a", "b", "c")
                .expectNext("default0")
                .verifyComplete();

    }

    @Test
    public void test_general_handel_error_on_return_retry() {
        Flux<String> stringFlux = Flux
                .just("a", "b", "c")
                .concatWith(Flux.error(RuntimeException::new))
                .concatWith(Flux.just("d"))
                .retry(1)
                .log();

        StepVerifier
                .create(stringFlux)
                .expectNext("a", "b", "c")
                .expectNext("a", "b", "c")
                .verifyError(RuntimeException.class);

    }
}
