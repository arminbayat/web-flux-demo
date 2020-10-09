package com.example.demo.repository;

import com.example.demo.Document.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@DataMongoTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    private final static List<Item> items =
            Arrays.asList(
                    new Item(null, "a", 1.0),
                    new Item("ABC", "ss", 2.0)
            );

    @BeforeEach
    void setUp() {
        itemRepository
                .deleteAll()
                .thenMany(Flux.fromIterable(items))
                .flatMap(s -> itemRepository.save(s))
                .doOnNext(System.out::println)
                .blockLast();
    }

    @Test
    public void findAllItem() {

        StepVerifier.create(itemRepository.findAll())
                .expectSubscription()
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void findItemByItem() {
        StepVerifier.create(itemRepository.findById("ABC"))
                .expectSubscription()
                .expectNextMatches(item -> item.getDescription().equals("ss"))
                .verifyComplete();
    }

    @Test
    public void findByDescription() {
        StepVerifier
                .create(itemRepository.findByDescription("ss"))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void saveItem() {
        Item item = new Item(null, "bb", 100.00);

        Mono<Item> save = itemRepository.save(item);
        StepVerifier.create(save)
                .expectSubscription()
                .expectNextMatches(item1 -> (item1.getId() != null && item1.getDescription().equals("bb")))
                .verifyComplete();
    }

    @Test
    public void updateItem() {
        Flux<Item> itemFlux = itemRepository
                .findByDescription("a")
                .map(item -> {
                    item.setPrice(20.0);
                    return item;
                })
                .flatMap(item -> itemRepository.save(item));

        StepVerifier.create(itemFlux)
                .expectSubscription()
                .expectNextMatches(item -> item.getPrice() == 20.0)
                .verifyComplete();
    }
}