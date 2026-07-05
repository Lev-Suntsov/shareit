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
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.function.Supplier;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestBookingClient {
    @Mock
    private RestTemplateBuilder restBuilder;

    @Mock
    private RestTemplate restTemplate;

    private BookingClient bookingClient;

    private final BookItemRequestDto bookDto = new BookItemRequestDto(1L,
            LocalDateTime.now().plusHours(1),
            LocalDateTime.now().plusDays(1));

    @BeforeEach
    void setUp() {
        when(restBuilder.uriTemplateHandler(any())).thenReturn(restBuilder);
        when(restBuilder.requestFactory(any(Supplier.class))).thenReturn(restBuilder);
        when(restBuilder.build()).thenReturn(restTemplate);

        bookingClient = new BookingClient("", restBuilder);

        BookingController bookingController = new BookingController(bookingClient);
    }

    @Test
    public void createBookingWithEmptyId() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        BookItemRequestDto dto = new BookItemRequestDto(
                null,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(1)
        );

        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("itemId"));
    }

    @Test
    public void createWithNegativeId() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        BookItemRequestDto dto = new BookItemRequestDto(
                -1L,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(1)
        );

        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("itemId"));
    }

    @Test
    public void shouldHaveViolationWhenStartIsInPast() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        BookItemRequestDto dto = new BookItemRequestDto(
                1L,
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusDays(1)
        );

        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(dto);

        assertThat(violations)
                .extracting(v -> v.getPropertyPath().toString())
                .contains("start");
    }

    @Test
    public void createBookingTest() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("ok");
        when(restTemplate.exchange(
                eq(""),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Object.class)
        )).thenReturn(ResponseEntity.ok(expectedResponse));
        ResponseEntity<Object> response = bookingClient.bookItem(1L, bookDto);

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
    void getBookingsMethod() {
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class),
                anyMap()
        )).thenReturn(ResponseEntity.ok("bookings"));

        ResponseEntity<Object> response = bookingClient.getBookings(1L, BookingState.ALL, 0, 10);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("bookings");

        verify(restTemplate).exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class),
                anyMap()
        );
    }
}