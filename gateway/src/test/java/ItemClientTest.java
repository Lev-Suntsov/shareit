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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemDtoCreate;
import ru.practicum.shareit.user.UserDto;
import java.util.Set;
import java.util.function.Supplier;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemClientTest {
    @Mock
    private RestTemplateBuilder builder;

    @Mock
    private RestTemplate restTemplate;

    ItemClient client;
    ItemController controller;

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    ItemDtoCreate itemDtoCreate;
    UserDto userDto = new UserDto(
            1L,
            "testUser@gmail.com",
            "Lev"
    );

    @BeforeEach
    public void setUp() {
        when(builder.uriTemplateHandler(any())).thenReturn(builder);
        when(builder.requestFactory(any(Supplier.class))).thenReturn(builder);
        when(builder.build()).thenReturn(restTemplate);

        client = new ItemClient("", builder);

        controller = new ItemController(client);
         itemDtoCreate = new ItemDtoCreate(
                1L,
                Boolean.FALSE,
                "TestItemName",
                "testItemDescription",
                 1L);
    }

    @Test
    void createItemWithNullAvailable() {
        itemDtoCreate = new ItemDtoCreate(
                    1L,
                    null,
                    "TestItemName",
                    "testItemDescription",
                1L);
            Set<ConstraintViolation<ItemDtoCreate>> violations = validator.validate(itemDtoCreate);
            assertThat(violations)
                    .anyMatch(v -> v.getPropertyPath().toString().equals("available"));
        }

        @Test
        void createItemTest() {
            ResponseEntity<Object> expectedResponse = ResponseEntity.ok("ok");
            when(restTemplate.exchange(
                    eq(""),
                    eq(HttpMethod.POST),
                    any(HttpEntity.class),
                    eq(Object.class)
            )).thenReturn(ResponseEntity.ok(expectedResponse));
            ResponseEntity<Object> response = client.post(userDto.getId(), itemDtoCreate);
            assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
            assertThat(response.getBody()).isEqualTo(expectedResponse);
            verify(restTemplate).exchange(
                    eq(""),
                    eq(HttpMethod.POST),
                    any(HttpEntity.class),
                    eq(Object.class)
            );
        }

    @Test
    void deleteItemTest() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("ok");

        when(restTemplate.exchange(
                eq("/" + itemDtoCreate.getId()),
                eq(HttpMethod.DELETE),
                any(HttpEntity.class),
                eq(Object.class)
        )).thenReturn(expectedResponse);

        ResponseEntity<Object> actualResponse = client.delete(itemDtoCreate.getId());

        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo("ok");

        verify(restTemplate).exchange(
                eq("/" + itemDtoCreate.getId()),
                eq(HttpMethod.DELETE),
                any(HttpEntity.class),
                eq(Object.class)
        );
    }
}
