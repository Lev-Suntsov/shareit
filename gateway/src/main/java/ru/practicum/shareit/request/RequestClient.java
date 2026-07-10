package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;


@Service
public class RequestClient extends BaseClient {
    @Autowired
    public RequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + "/requests"))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> save(Long userId, RequestDto dto) {
        return post("", userId, dto);
    }

    public ResponseEntity<Object> get(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getAll(Long userId) {
        return get("/all", userId);
    }

    public ResponseEntity<Object> findById(Long requestId) {
        return get("/" + requestId);
    }
}
