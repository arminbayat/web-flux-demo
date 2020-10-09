package com.example.demo.playGround;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class MonoTest {

    @Test
    public void general_mono_test() {
        Mono<String> stringMono = Mono.just("Spring").log();
        StepVerifier.create(stringMono).expectNext("Spring").verifyComplete();
    }

    @Test
    public void general_mono_error_test() {
        StepVerifier.create(Mono.error(RuntimeException::new).log())
                .verifyError(RuntimeException.class);
    }
}
