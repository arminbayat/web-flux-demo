package com.example.demo.playGround;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxTest {

    @Test
    public void general_test_flux() {

        Flux<String> stringFlux =
                Flux.just("spring", "spring boot", "spring reactive")
                        .concatWith(Flux.error(RuntimeException::new))
                        .log();

        stringFlux.subscribe(System.out::println, System.err::println);
    }

    @Test
    public void test_flux_element_count_without_error() {
        Flux<String> stringFlux = Flux.just("spring", "spring boot", "spring reactive").log();

        StepVerifier.create(stringFlux).expectNextCount(3).verifyComplete();
    }

    @Test
    public void test_flux_element_without_error() {
        Flux<String> stringFlux = Flux.just("spring", "spring boot", "spring reactive").log();

        StepVerifier.create(stringFlux)
                .expectNext("spring", "spring boot", "spring reactive")
                .verifyComplete();
    }

    @Test
    public void test_flux_element_with_error() {
        Flux<String> stringFlux = Flux.just("spring", "spring boot", "spring reactive")
                .concatWith(Flux.error(RuntimeException::new))
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("spring", "spring boot", "spring reactive")
                .verifyError(RuntimeException.class);
    }

    @Test
    public void test_flux_element_with_specific_error_message() {
        Flux<String> stringFlux = Flux.just("spring", "spring boot", "spring reactive")
                .concatWith(Flux.error(new RuntimeException("Error")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("spring", "spring boot", "spring reactive")
                .expectErrorMessage("Error")
                .verify();
    }
}


