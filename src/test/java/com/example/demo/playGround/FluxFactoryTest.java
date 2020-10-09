package com.example.demo.playGround;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxFactoryTest {

    private final List<String> nameList = Arrays.asList("anna", "adam", "helen");

    @Test
    public void create_flux_from_iterable() {
        Flux<String> stringFlux = Flux.fromIterable(nameList).log();
        StepVerifier.create(stringFlux).expectNext("anna", "adam", "helen").verifyComplete();
    }

    @Test
    public void create_flux_from_range() {
        Flux<Integer> integerFlux = Flux.range(1, 5).log();
        StepVerifier.create(integerFlux).expectNext(1, 2, 3, 4, 5).verifyComplete();
    }

    @Test
    public void create_flux_from_stream() {
        Flux<String> stringFlux = Flux.fromStream(nameList.stream()).log();
        StepVerifier.create(stringFlux).expectNext("anna", "adam", "helen").verifyComplete();
    }
}
