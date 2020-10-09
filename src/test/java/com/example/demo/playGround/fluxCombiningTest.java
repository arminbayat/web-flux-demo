package com.example.demo.playGround;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class fluxCombiningTest {

    @Test
    public void combine_using_merge() {
        Flux<String> stringFlux = Flux.just("A", "B", "C");
        Flux<String> stringFlux1 = Flux.just("D", "E", "F");
        
        Flux<String> merged = Flux.merge(stringFlux, stringFlux1);

        StepVerifier.create(merged.log())
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }
}
