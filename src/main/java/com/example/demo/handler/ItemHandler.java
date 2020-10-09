package com.example.demo.handler;

import com.example.demo.Document.Item;
import com.example.demo.repository.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class ItemHandler {

    private final ItemRepository itemRepository;

    public Mono<ServerResponse> findAllItem(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(itemRepository.findAll(), Item.class);

    }
}
