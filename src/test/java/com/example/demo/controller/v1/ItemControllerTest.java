package com.example.demo.controller.v1;

import com.example.demo.Document.Item;
import com.example.demo.constans.ItemUrls;
import com.example.demo.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class ItemControllerTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(item -> itemRepository.save(item))
                .blockLast();
    }

    private List<Item> data() {
        return Arrays.asList(
                new Item(null, "a", 1.0),
                new Item(null, "b", 2.0),
                new Item(null, "c", 3.0),
                new Item("ABC", "d", 4.0));
    }

    @Test
    void findAllItems() {
        webTestClient
                .get()
                .uri(ItemUrls.ITEMS_END_POINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Item.class)
                .hasSize(4);
    }

    @Test
    void findById() {
        webTestClient
                .get()
                .uri(ItemUrls.ITEMS_END_POINT_V1.concat("/{id}"), "ABC")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price", 4.0);
    }

    @Test
    void findById_not_found() {
        webTestClient
                .get()
                .uri(ItemUrls.ITEMS_END_POINT_V1.concat("/{id}"), "A")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void createItem() {
        Item item = new Item(null, "qq", 1.0);
        webTestClient
                .post()
                .uri(ItemUrls.ITEMS_END_POINT_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.price").isEqualTo(1.0)
                .jsonPath("$.description").isEqualTo("qq");
    }

    @Test
    void deleteItem() {
        webTestClient
                .delete()
                .uri(ItemUrls.ITEMS_END_POINT_V1.concat("/{id}"), "ABC")
                .exchange()
                .expectStatus().isOk();

    }

    @Test
    void updateItem() {
        Item item = new Item(null, "d", 10.0);
        webTestClient
                .put()
                .uri(ItemUrls.ITEMS_END_POINT_V1.concat("/{id}"), "ABC")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price").isEqualTo(10.0);
    }

    @Test
    void update_item_not_found() {
        Item item = new Item(null, "d", 10.0);
        webTestClient
                .put()
                .uri(ItemUrls.ITEMS_END_POINT_V1.concat("/{id}"), "A")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isNotFound();
    }

}