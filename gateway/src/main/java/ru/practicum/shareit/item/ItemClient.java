package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

@Slf4j
@Service

public class ItemClient extends BaseClient {
    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + "/items"))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getByUserId(long userId) {
        return get("/", userId);
    }

    public ResponseEntity<Object> post(Long userId, ItemDtoCreate item) {
        return post("", userId, item);
    }

    public ResponseEntity<Object> delete(long itemId) {
        return delete("/" + itemId);
    }

    public ResponseEntity<Object> update(Long itemId, Long userId, ItemDtoRequestUpdate item) {
        return patch("/" + itemId, userId, item);
    }

    public ResponseEntity<Object> findById(Long itemId, Long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> search(String text) {
        return get("/" + text);
    }

    public ResponseEntity<Object> addComment(Long userId, Long itemId, CommentDto commentDto) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }
}
