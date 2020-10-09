package com.example.demo.initial;

import com.example.demo.Document.Item;
import com.example.demo.repository.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
@Profile("!test")
public class Initializer implements CommandLineRunner {

    private ItemRepository itemRepository;

    @Override
    public void run(String... args) throws Exception {
        Flux.fromIterable(data())
                .flatMap(item -> itemRepository.save(item));

    }

    private List<Item> data() {
        return Arrays.asList(
                new Item(null, "a", 1.0),
                new Item(null, "ab", 2.0),
                new Item(null, "abc", 3.0),
                new Item(null, "abcd", 4.0));
    }
}
