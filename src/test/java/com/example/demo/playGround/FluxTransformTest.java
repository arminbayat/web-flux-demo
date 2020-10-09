package com.example.demo.playGround;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static reactor.core.scheduler.Schedulers.parallel;

public class FluxTransformTest {

    private final List<String> nameList = Arrays.asList("anna", "adam", "helen");

    @Test
    public void flux_map_to_upper_case() {
        Flux<String> stringFlux = Flux
                .fromIterable(nameList)
                .map(String::toUpperCase)
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("ANNA", "ADAM", "HELEN")
                .verifyComplete();
    }


    @Test
    public void flux_map_to_length() {
        Flux<Integer> stringFlux = Flux
                .fromIterable(nameList)
                .map(String::length)
                .log();

        StepVerifier.create(stringFlux)
                .expectNext(4, 4, 5)
                .verifyComplete();
    }

    @Test
    public void flux_map_to_length_repeat() {
        Flux<Integer> stringFlux = Flux
                .fromIterable(nameList)
                .map(String::length)
                .repeat(1)
                .log();

        StepVerifier.create(stringFlux)
                .expectNext(4, 4, 5, 4, 4, 5)
                .verifyComplete();
    }


    @Test
    public void flux_filter_and_map() {
        Flux<String> stringFlux = Flux
                .fromIterable(nameList)
                .filter(s -> s.length() > 4)
                .map(String::toUpperCase)
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("HELEN")
                .verifyComplete();
    }

    @Test
    public void test_flat_map() {
        Flux<String> objectFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
                .flatMap(s -> Flux.fromIterable(convertToList(s)))
                .log();

        StepVerifier.create(objectFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    @Test
    public void test_flat_map_parallel() {
        Flux<String> objectFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
                .window(2)
                .flatMap(s -> s.map(this::convertToList).subscribeOn(parallel()))
                .flatMap(Flux::fromIterable)
                .log();

        StepVerifier.create(objectFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    @SneakyThrows
    private List<String> convertToList(String s) {
        Thread.sleep(1000);
        return Arrays.asList(s, "AA");
    }
}
