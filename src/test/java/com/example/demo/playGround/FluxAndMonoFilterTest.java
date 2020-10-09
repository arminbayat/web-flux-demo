package com.example.demo.playGround;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoFilterTest {

    private final List<String> nameList = Arrays.asList("anna", "adam", "helen");

    @Test
    public void filter_with_first_character() {
        Flux<String> stringFlux = Flux.fromIterable(nameList)
                .filter(s -> s.startsWith("h"))
                .log();
        StepVerifier.create(stringFlux).expectNext("helen").verifyComplete();
    }

    @Test
    public void filter_with_first_character_count_verifier() {
        Flux<String> stringFlux = Flux.fromIterable(nameList)
                .filter(s -> s.startsWith("a"))
                .log();
        StepVerifier.create(stringFlux).expectNextCount(2).verifyComplete();
    }
}
