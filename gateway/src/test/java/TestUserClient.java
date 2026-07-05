import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
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
import ru.practicum.shareit.user.UserClient;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserDto;

import java.util.Set;
import java.util.function.Supplier;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@ExtendWith(MockitoExtension.class)

public class TestUserClient {
    @Mock
    private RestTemplateBuilder builder;

    @Mock
    private RestTemplate restTemplate;

    UserClient client;

    UserController controller;

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    UserDto user;

    @BeforeEach
    public void setUp() {
        when(builder.uriTemplateHandler(any())).thenReturn(builder);
        when(builder.requestFactory(any(Supplier.class))).thenReturn(builder);
        when(builder.build()).thenReturn(restTemplate);

        client = new UserClient("", builder);
        controller = new UserController(client);

        user = new UserDto(
                1L,
                "UserTestEmail@gmail.com",
                "testUser"
        );
    }

    @Test
    void testIdNullValidation() {
        user.setId(null);

        Set<ConstraintViolation<UserDto>> violations = validator.validate(user);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("id"));
    }

    @Test
    void testIdNegativeValidate() {
        user.setId(-1L);

        Set<ConstraintViolation<UserDto>> violations = validator.validate(user);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("id"));
    }

    @Test
    void testEmailNullValidate() {
        user.setEmail(null);

        Set<ConstraintViolation<UserDto>> violations = validator.validate(user);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    void testIncorrectEmail() {
        user.setEmail("UserTestEmail");

        Set<ConstraintViolation<UserDto>> violations = validator.validate(user);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    void createUserTest() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("ok");

        when(restTemplate.exchange(
                eq(""),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok(expectedResponse));
        ResponseEntity<Object> response = client.create(user);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(expectedResponse);
        verify(restTemplate).exchange(
                eq(""),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Object.class));
    }

    @Test
    void updateUserTest() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("ok");

        when(restTemplate.exchange(
                eq("/" + user.getId()),
                eq(HttpMethod.PATCH),
                any(HttpEntity.class),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok(expectedResponse));
        ResponseEntity<Object> response = client.update(user.getId(), user);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(expectedResponse);
        verify(restTemplate).exchange(
                eq("/" + user.getId()),
                eq(HttpMethod.PATCH),
                any(HttpEntity.class),
                eq(Object.class
        ));
    }

    @Test
    void getUserByIdTest() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("ok");

        when(restTemplate.exchange(
                eq("/" + user.getId()),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok(expectedResponse));
        ResponseEntity<Object> response = client.getById(user.getId());
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(expectedResponse);
        verify(restTemplate).exchange(
                eq("/" + user.getId()),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class
                ));
    }

    @Test
    void deleteUserTest() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("ok");

        when(restTemplate.exchange(
                eq("/" + user.getId()),
                eq(HttpMethod.DELETE),
                any(HttpEntity.class),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok(expectedResponse));
        ResponseEntity<Object> response = client.delete(user.getId());
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(expectedResponse);
        verify(restTemplate).exchange(
                eq("/" + user.getId()),
                eq(HttpMethod.DELETE),
                any(HttpEntity.class),
                eq(Object.class
                ));
    }
}
