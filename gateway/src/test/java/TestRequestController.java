import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.request.RequestClient;
import ru.practicum.shareit.request.RequestController;
import ru.practicum.shareit.request.RequestDto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.function.Supplier;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestRequestController {
    @Mock
    private RestTemplateBuilder builder;

    @Mock
    private RestTemplate restTemplate;

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    RequestController controller;

    RequestClient client;

    RequestDto request;

    @BeforeEach
    void setUp() {
        when(builder.uriTemplateHandler(any())).thenReturn(builder);
        when(builder.requestFactory(any(Supplier.class))).thenReturn(builder);
        when(builder.build()).thenReturn(restTemplate);

        client = new RequestClient("", builder);

        controller = new RequestController(client);

        request = new RequestDto(
                1L,
                "testDescription",
                1L,
                1L,
                LocalDateTime.now()
        );
    }

    @Test
    void createWithNullId() {
        request.setId(null);

        Set<ConstraintViolation<RequestDto>> violations = validator.validate(request);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("id"));
    }

    @Test
    void createWithNegativeId() {
        request.setId(-1L);

        Set<ConstraintViolation<RequestDto>> violations = validator.validate(request);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("id"));
    }

    @Test
    void createWithNullDescription() {
        request.setDescription(null);

        Set<ConstraintViolation<RequestDto>> violations = validator.validate(request);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("description"));
    }

    @Test
    void createWithNullItemId() {
        request.setItemId(null);

        Set<ConstraintViolation<RequestDto>> violations = validator.validate(request);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("itemId"));
    }

    @Test
    void createRequestTest() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("ok");

        when(restTemplate.exchange(
                eq(""),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok(expectedResponse));
        ResponseEntity<Object> response = client.save(request.getUserId(), request);
        AssertionsForClassTypes.assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        AssertionsForClassTypes.assertThat(response.getBody()).isEqualTo(expectedResponse);
        verify(restTemplate).exchange(
                eq(""),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Object.class));
    }

    @Test
    void getRequestTest() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("ok");

        when(restTemplate.exchange(
                eq(""),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok(expectedResponse));
        ResponseEntity<Object> response = client.get(request.getUserId());
        AssertionsForClassTypes.assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        AssertionsForClassTypes.assertThat(response.getBody()).isEqualTo(expectedResponse);
        verify(restTemplate).exchange(
                eq(""),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class));
    }

    @Test
    void getAllRequestTest() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("ok");

        when(restTemplate.exchange(
                eq("/all"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok(expectedResponse));
        ResponseEntity<Object> response = client.getAll(request.getUserId());
        AssertionsForClassTypes.assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        AssertionsForClassTypes.assertThat(response.getBody()).isEqualTo(expectedResponse);
        verify(restTemplate).exchange(
                eq("/all"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class));
    }

    @Test
    void getRequestById() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("ok");

        when(restTemplate.exchange(
                eq("/" + request.getId()),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok(expectedResponse));
        ResponseEntity<Object> response = client.findById(request.getId());
        AssertionsForClassTypes.assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        AssertionsForClassTypes.assertThat(response.getBody()).isEqualTo(expectedResponse);
        verify(restTemplate).exchange(
                eq("/" + request.getId()),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class));
    }
}

