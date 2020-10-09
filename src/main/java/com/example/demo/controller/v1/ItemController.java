package com.example.demo.controller.v1;

import com.example.demo.Document.Item;
import com.example.demo.repository.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.example.demo.constans.ItemUrls.ITEMS_END_POINT_V1;

@RestController
@AllArgsConstructor
public class ItemController {
    private final ItemRepository itemRepository;

    @GetMapping(ITEMS_END_POINT_V1)
    public Flux<Item> findAllItems() {
        return itemRepository.findAll();
    }

    @GetMapping(ITEMS_END_POINT_V1 + "/{id}")
    public Mono<ResponseEntity<Item>> findById(@PathVariable String id) {
        return itemRepository
                .findById(id)
                .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(ITEMS_END_POINT_V1)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Item> createItem(@RequestBody Item item) {
        return itemRepository.save(item);
    }

    @DeleteMapping(ITEMS_END_POINT_V1 + "/{id}")
    public Mono<Void> deleteItem(@PathVariable String id) {
        return itemRepository.deleteById(id);
    }

    @PutMapping(ITEMS_END_POINT_V1 + "/{id}")
    public Mono<ResponseEntity<Item>> updateItem(@PathVariable String id,
                                                 @RequestBody Item item) {

        return itemRepository.findById(id)
                .flatMap(item1 -> {
                    item1.setPrice(item.getPrice());
                    item1.setDescription(item.getDescription());
                    return itemRepository.save(item1);
                })
                .map(o -> new ResponseEntity<>(o, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
