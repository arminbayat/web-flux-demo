package com.example.demo.playGround;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.Supplier;

public class MonoFactoryTest {

    @Test
    public void create_mono_from_null() {
        Mono<Object> empty = Mono.empty().log();
        StepVerifier.create(empty).verifyComplete();
    }

    @Test
    public void create_mono_from_error() {
        Mono<Object> empty = Mono.error(RuntimeException::new);
        StepVerifier.create(empty).verifyError();
    }

    @Test
    public void create_mono_from_supplier() {

        Supplier<String> stringSupplier = () -> "mono";
        Mono<Object> fromSupplier = Mono.fromSupplier(stringSupplier);
        StepVerifier.create(fromSupplier).expectNext("mono").verifyComplete();
    }

}
